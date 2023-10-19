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
    /** ImageView for displaying the round song image. */
    private ImageView roundImageView;

    /** TextView for displaying the song's name. */
    private TextView nameText;

    /** TextView for displaying the artist's name. */
    private TextView artistText;

    /** ImageView to control song playback (play/pause). */
    private ImageView play;

    /** ImageView to mark song as liked. */
    private ImageView like;

    /** ImageView to mark song as favorite. */
    private ImageView fav;

    /** ImageView to open the comments section for the song. */
    private ImageView comment;

    /** Dialog to show the comments section. */
    private Dialog dialog;

    /** Adapter for managing and displaying comments in a RecyclerView. */
    private CommentAdapter commentAdapter;

    /** Button for navigating back to the home screen. */
    private Button goBacktoHome;

    /** RecyclerView for displaying the list of comments. */
    RecyclerView commentsRecyclerView;

    /** Data access object for Firestore interactions. */
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

        // Set up finish for back button
        goBacktoHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Set up page turning for artist text
        this.artistText.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                RuntimeObserver.currentDisplayingArtist = new Artist(RuntimeObserver.getCurrentSong().getArtistName(), RuntimeObserver.getCurrentSong().getUrlToImage());
                Intent intent = new Intent(SongActivity.this, ArtistActivity.class);
                startActivity(intent);
            }
        });


        // Set up round image and relevant info for the song
        if (RuntimeObserver.getCurrentSong() != null) setTheSong(Functions.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName(), RuntimeObserver.getCurrentSong().getArtistName());

        // Set up play/pause icon for the player
        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying()) this.play.setImageResource(R.drawable.ic_song_pause);

        // Set up like/unlike for the like button and show relevant data
        if(RuntimeObserver.currentUser.getLikes().contains(RuntimeObserver.getCurrentSong().getId())){
            this.like.setImageResource(R.drawable.ic_song_like);
            TextView likeText = findViewById(R.id.like_count);
            int likeCount = RuntimeObserver.getCurrentSong().getLikes();
            likeText.setText(likeCount > 99 ? "99+" : "" + likeCount);
        }


        // Set up fav/unfav for the like button and show relevant data
        if(RuntimeObserver.currentUser.getFavorites().contains(RuntimeObserver.getCurrentSong().getId())){
            this.fav.setImageResource(R.drawable.ic_song_fav);
            TextView favText = findViewById(R.id.fav_count);
            int favCount = RuntimeObserver.getCurrentSong().getFavorites();
            favText.setText(favCount > 99 ? "99+" : "" + favCount);
        }

        // Set up click listener for the play button
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

        // Set up click listener for the like button
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

        // Set up click listener for the fav button
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

        // Set up comment dialogue
        setUpComments();

        // Set up click listener for the comment button
        this.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                showBottomComments();
            }
        });

        // react to any data change when created
        onDataChangeResponse();
    }

    /**
     * Updates the UI to display the current song's details.
     *
     * @param imageUrl  URL for the song's image.
     * @param songName  Name of the song.
     * @param artistName Name of the artist.
     */
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

    /**
     * Initializes and sets up the comments section.
     */
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

    /**
     * Displays the comments section at the bottom of the screen.
     */
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


    /**
     * React to any realtime listener and show relevant data
     */
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