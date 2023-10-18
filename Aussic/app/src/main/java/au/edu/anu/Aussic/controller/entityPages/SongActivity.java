package au.edu.anu.Aussic.controller.entityPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.Functions;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.userAction.Comment;
import au.edu.anu.Aussic.models.userAction.Favorites;
import au.edu.anu.Aussic.models.userAction.Like;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7552399, Yizhuo Gao
 * @author: u7603590, Jiawei Niu
 */

public class SongActivity extends AppCompatActivity implements OnDataChangeListener {
    private ImageView roundImageView;
    private TextView nameText;
    private TextView artistText;
    private ImageView play;
    private ImageView like;
    private ImageView fav;
    private ImageView comment;
    private Dialog dialog;
    private CommentAdapter commentAdapter;

    private Button goBacktoHome;
    RecyclerView commentsRecyclerView;
    FirestoreDao firestoreDao = new FirestoreDaoImpl();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song);

        RuntimeObserver.addOnDataChangeListener(this);

        this.roundImageView = findViewById(R.id.song_spinning_round_image);
        this.nameText = findViewById(R.id.song_song_name);
        this.artistText = findViewById(R.id.song_artist_name);
        this.play = findViewById(R.id.song_fab);
        this.like = findViewById(R.id.song_like);
        this.fav = findViewById(R.id.song_fav);
        this.comment = findViewById(R.id.song_comment);
        this.goBacktoHome = findViewById(R.id.goBacktoHome);

        goBacktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        this.artistText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RuntimeObserver.currentDisplayingArtist = new Artist(RuntimeObserver.getCurrentSong().getArtistName(), RuntimeObserver.getCurrentSong().getUrlToImage());
                Intent intent = new Intent(SongActivity.this, ArtistActivity.class);
                startActivity(intent);
            }
        });


        if (RuntimeObserver.getCurrentSong() != null) setTheSong(Functions.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName(), RuntimeObserver.getCurrentSong().getArtistName());

        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying()) this.play.setImageResource(R.drawable.ic_song_pause);

        if(RuntimeObserver.currentUser.getLikes().contains(RuntimeObserver.getCurrentSong().getId())){
            this.like.setImageResource(R.drawable.ic_song_like);
            TextView likeText = findViewById(R.id.like_count);
            int likeCount = RuntimeObserver.getCurrentSong().getLikes();
            likeText.setText(likeCount > 99 ? "99+" : "" + likeCount);
        }


        if(RuntimeObserver.currentUser.getFavorites().contains(RuntimeObserver.getCurrentSong().getId())){
            this.fav.setImageResource(R.drawable.ic_song_fav);
            TextView favText = findViewById(R.id.fav_count);
            int favCount = RuntimeObserver.getCurrentSong().getFavorites();
            favText.setText(favCount > 99 ? "99+" : "" + favCount);
        }

        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (RuntimeObserver.getCurrentMediaPlayer() != null) {
                    if (RuntimeObserver.getCurrentMediaPlayer().isPlaying()) {
                        RuntimeObserver.getCurrentMediaPlayer().pause();
                        play.setImageResource(R.drawable.ic_song_play);
                        roundImageView.clearAnimation();
                    } else {
                        RuntimeObserver.getCurrentMediaPlayer().start();
                        play.setImageResource(R.drawable.ic_song_pause);
                        roundImageView.startAnimation(AnimationUtils.loadAnimation(SongActivity.this, R.anim.spinning));
                    }
                    RuntimeObserver.notifyOnMediaChangeListeners();
                }
            }
        });

        this.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!RuntimeObserver.currentUser.getLikes().contains(RuntimeObserver.getCurrentSong().getId())){
                    firestoreDao.updateUserLikes(RuntimeObserver.getCurrentSong().getId())
                            .thenAccept(msg -> {
                                if(msg == null){
                                    new Like("like", FirebaseAuth.getInstance().getCurrentUser().getEmail(), RuntimeObserver.getCurrentSong().getSongName(), Integer.parseInt(RuntimeObserver.getCurrentSong().getId())).update();
                                }else {
                                    Toast.makeText(SongActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

        this.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                if(!RuntimeObserver.currentUser.getFavorites().contains(RuntimeObserver.getCurrentSong().getId())){
                    firestoreDao.updateUserFavorites(RuntimeObserver.getCurrentSong().getId())
                            .thenAccept(msg -> {
                                if(msg == null){
                                    new Favorites("favorites", FirebaseAuth.getInstance().getCurrentUser().getEmail(), RuntimeObserver.getCurrentSong().getSongName(), Integer.parseInt(RuntimeObserver.getCurrentSong().getId())).update();
                                    RuntimeObserver.musicSearchEngine.addSong(RuntimeObserver.getCurrentSong());
                                }else {
                                    Toast.makeText(SongActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });
        setUpComments();
        this.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showBottomComments();
            }
        });
        onDataChangeResponse();


    }

    private void setTheSong(String imageUrl,String songName, String artistName){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(1200 * 0.8), (int)(1200 * 0.8)))
                .circleCrop()
                .into(this.roundImageView);
        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying())   this.roundImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.spinning));
        else this.roundImageView.clearAnimation();
        nameText.setText(Functions.adjustLength(songName));
        artistText.setText(Functions.adjustLength(artistName));
    }

    private void setUpComments(){
        this.dialog = new Dialog(this);
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(R.layout.layout_comments);  // This is the layout for the comments.

        // Views from the comments layout
        EditText commentInput = dialog.findViewById(R.id.song_comment_input);
        this.commentsRecyclerView = dialog.findViewById(R.id.song_comments_list);

        List<CommentItem> commentList = RuntimeObserver.getCurrentSong().getCommentItems();

        // Setup the RecyclerView and its adapter

        this.commentAdapter = new CommentAdapter(commentList);
        this.commentsRecyclerView.setAdapter(commentAdapter);
        this.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

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

                    // Optional: Clear the EditText after sending the comment
                    commentInput.setText("");

                } else {
                    Toast.makeText(SongActivity.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void showBottomComments() {
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


    @Override
    public void onDataChangeResponse(){
        if(RuntimeObserver.getCurrentSong() != null){

            TextView likeText = findViewById(R.id.like_count);
            int likeCount = RuntimeObserver.getCurrentSong().getLikes();
            likeText.setText(likeCount > 99 ? "99+" : "" + likeCount);
            likeText.setTextColor(getResources().getColor(com.firebase.ui.storage.R.color.common_google_signin_btn_text_light));
            if(RuntimeObserver.currentUser.getLikes().contains(RuntimeObserver.getCurrentSong().getId())){
                this.like.setImageResource(R.drawable.ic_song_like);
                likeText.setTextColor(getResources().getColor(R.color.white));
            }

            TextView favText = findViewById(R.id.fav_count);
            int favCount = RuntimeObserver.getCurrentSong().getFavorites();
            favText.setTextColor(getResources().getColor(com.firebase.ui.storage.R.color.common_google_signin_btn_text_light));
            favText.setText(favCount > 99 ? "99+" : "" + favCount);
            if(RuntimeObserver.currentUser.getFavorites().contains(RuntimeObserver.getCurrentSong().getId())){
                this.fav.setImageResource(R.drawable.ic_song_fav);
                favText.setTextColor(getResources().getColor(R.color.white));
            }



            List<CommentItem> commentList = RuntimeObserver.getCurrentSong().getCommentItems();
            // Setup the RecyclerView and its adapter

            this.commentAdapter = new CommentAdapter(commentList);
            this.commentsRecyclerView.setAdapter(commentAdapter);
            this.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            this.commentsRecyclerView.setAdapter(commentAdapter);
            this.commentsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        }
    }

}