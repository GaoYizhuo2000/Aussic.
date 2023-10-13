package au.edu.anu.Aussic.controller.songPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.Adapter.CardAdapter;
import au.edu.anu.Aussic.controller.homePages.Adapter.CommentAdapter;
import au.edu.anu.Aussic.controller.homePages.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.controller.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.userAction.Comment;
import au.edu.anu.Aussic.models.userAction.Favorites;
import au.edu.anu.Aussic.models.userAction.Like;
import au.edu.anu.Aussic.models.userAction.UserAction;
import au.edu.anu.Aussic.models.userAction.UserActionFactory;

public class SongActivity extends AppCompatActivity {
    private ImageView roundImageView;
    private TextView nameText;
    private TextView artistText;
    private ImageView play;
    private ImageView like;
    private ImageView fav;
    private ImageView comment;
    FirestoreDao firestoreDao = new FirestoreDaoImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        this.roundImageView = findViewById(R.id.song_spinning_round_image);
        this.nameText = findViewById(R.id.song_song_name);
        this.artistText = findViewById(R.id.song_artist_name);
        this.play = findViewById(R.id.song_fab);
        this.like = findViewById(R.id.song_like);
        this.fav = findViewById(R.id.song_fav);
        this.comment = findViewById(R.id.song_comment);

        if (RuntimeObserver.getCurrentSong() != null) setTheSong(CardAdapter.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName(), RuntimeObserver.getCurrentSong().getArtistName());

        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying()) this.play.setImageResource(R.drawable.ic_song_pause);
        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RuntimeObserver.getCurrentMediaPlayer() != null) {
                    if (RuntimeObserver.getCurrentMediaPlayer().isPlaying()) {
                        RuntimeObserver.getCurrentMediaPlayer().pause();
                        play.setImageResource(R.drawable.ic_song_play);
                        roundImageView.clearAnimation();
                        RuntimeObserver.notifyListeners();
                    } else {
                        RuntimeObserver.getCurrentMediaPlayer().start();
                        play.setImageResource(R.drawable.ic_song_pause);
                        roundImageView.startAnimation(AnimationUtils.loadAnimation(SongActivity.this, R.anim.spinning));
                        RuntimeObserver.notifyListeners();
                    }
                }
            }
        });

        this.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                like.setImageResource(R.drawable.ic_song_like);
                firestoreDao.updateUserLikes(RuntimeObserver.getCurrentSong().getId())
                        .thenAccept(msg -> {
                            if(msg == null){
                                new Like("like", FirebaseAuth.getInstance().getCurrentUser().getEmail(), RuntimeObserver.getCurrentSong().getSongName(), Integer.parseInt(RuntimeObserver.getCurrentSong().getId())).update();
                            }else {
                                Toast.makeText(SongActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        this.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                fav.setImageResource(R.drawable.ic_song_fav);
                firestoreDao.updateUserFavorites(RuntimeObserver.getCurrentSong().getId())
                        .thenAccept(msg -> {
                            if(msg == null){
                                new Favorites("favorites", FirebaseAuth.getInstance().getCurrentUser().getEmail(), RuntimeObserver.getCurrentSong().getSongName(), Integer.parseInt(RuntimeObserver.getCurrentSong().getId())).update();
                            }else {
                                Toast.makeText(SongActivity.this,msg,Toast.LENGTH_SHORT).show();
                            }
                });
            }
        });
        this.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showBottomComments();
            }
        });






    }

    private void setTheSong(String imageUrl,String songName, String artistName){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(1200 * 0.8), (int)(1200 * 0.8)))
                .circleCrop()
                .into(this.roundImageView);
        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying())   this.roundImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.spinning));
        else this.roundImageView.clearAnimation();
        nameText.setText(CardAdapter.adjustLength(songName));
        artistText.setText(CardAdapter.adjustLength(artistName));
    }

    private void showBottomComments() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.layout_comments);  // This is the layout for the comments.

        // Views from the comments layout
        EditText commentInput = dialog.findViewById(R.id.song_comment_input);
        RecyclerView commentsRecyclerView = dialog.findViewById(R.id.rv_comments_list);
        // If you have a RecyclerView adapter, initialize it here and set it to the RecyclerView

        // We'll use the EditText's 'imeOptions' for the comment submission.
        // First, set the EditText's IME action label to "Submit" and actionId to actionSend in the XML.
        commentInput.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    // Handle the comment submission here.
                    String commentText = commentInput.getText().toString().trim();
                    if (!commentText.isEmpty()) {
                        // Add the comment to your comments list, update the RecyclerView, etc.
                        dialog.dismiss();
                    } else {
                        Toast.makeText(SongActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                    }
                    return true;
                }
                return false;
            }
        });

//        // Your data list for the comments
//        List<CommentItem> commentList = new ArrayList<>();




        // get and load comments
//        firestoreDao.getComment(RuntimeObserver.getCurrentSong().getId())
//                .thenAccept(details -> {
//                    for(Map<String, Object> comment : details) {
//                        CommentItem newComment = new CommentItem("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2Fdefault.jpg?alt=media", (String) comment.get("uid"), (String) comment.get("content"));
//                        commentList.add(newComment);
//                        commentAdapter.notifyItemInserted(commentList.size() - 1);
//                    }
//
//                });
        List<CommentItem> commentList = RuntimeObserver.getCurrentSong().getComments();

        // Setup the RecyclerView and its adapter

        CommentAdapter commentAdapter = new CommentAdapter(commentList);
        commentsRecyclerView.setAdapter(commentAdapter);
        commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Find and set an OnClickListener to the button
        Button sendButton = dialog.findViewById(R.id.the_btn_send);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Retrieve the comment from the EditText
                String commentText = commentInput.getText().toString().trim();


                if (!commentText.isEmpty()) {
                    //update songs comment in firestore
                    new Comment("comment", FirebaseAuth.getInstance().getCurrentUser().getEmail(), RuntimeObserver.getCurrentSong().getSongName()
                            , Integer.parseInt(RuntimeObserver.getCurrentSong().getId()), commentText ).update();



                    // Create a new CommentItem object and add it to the commentList
                    // Assuming CommentItem is a class that represents a comment with user avatar, name, and content
                    CommentItem newComment = new CommentItem(RuntimeObserver.currentUser.iconUrl, RuntimeObserver.currentUser.username, commentText);
                    commentList.add(newComment);

                    // Update runtime song's comment value
                    RuntimeObserver.getCurrentSong().addComment(newComment);

                    // Notify the adapter that an item was added to the end of the list
                    commentAdapter.notifyItemInserted(commentList.size() - 1);

                    // Optional: Clear the EditText after sending the comment
                    commentInput.setText("");

                    // Optional: Scroll the RecyclerView to show the new comment
                    commentsRecyclerView.smoothScrollToPosition(commentList.size() - 1);
                } else {
                    Toast.makeText(SongActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });


        dialog.show();

        // Set the dimensions and appearance of the dialog
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

        // Allow the dialog to be canceled by touching outside its bounds
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
    }

}