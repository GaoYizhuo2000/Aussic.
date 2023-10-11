package au.edu.anu.Aussic.controller.songPages;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.Adapter.CardAdapter;
import au.edu.anu.Aussic.models.observer.MediaObserver;

public class SongActivity extends AppCompatActivity {
    private ImageView roundImageView;
    private TextView nameText;
    private TextView artistText;
    private ImageView play;
    private ImageView like;
    private ImageView fav;
    private ImageView comment;


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

        if (MediaObserver.getCurrentSong() != null)
        setTheSong(CardAdapter.makeImageUrl(200, 200, MediaObserver.getCurrentSong().getUrlToImage()), MediaObserver.getCurrentSong().getSongName(), MediaObserver.getCurrentSong().getArtistName());

        if(MediaObserver.getCurrentMediaPlayer().isPlaying()) this.play.setImageResource(R.drawable.ic_song_pause);
        this.play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (MediaObserver.getCurrentMediaPlayer() != null) {
                    if (MediaObserver.getCurrentMediaPlayer().isPlaying()) {
                        MediaObserver.getCurrentMediaPlayer().pause();
                        play.setImageResource(R.drawable.ic_song_play);
                        roundImageView.clearAnimation();
                        MediaObserver.notifyListeners();
                    } else {
                        MediaObserver.getCurrentMediaPlayer().start();
                        play.setImageResource(R.drawable.ic_song_pause);
                        roundImageView.startAnimation(AnimationUtils.loadAnimation(SongActivity.this, R.anim.spinning));
                        MediaObserver.notifyListeners();
                    }
                }
            }
        });

        this.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                like.setImageResource(R.drawable.ic_song_like);
            }
        });

        this.fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                fav.setImageResource(R.drawable.ic_song_fav);
            }
        });
        this.comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){

            }
        });






    }

    private void setTheSong(String imageUrl,String songName, String artistName){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(1200 * 0.8), (int)(1200 * 0.8)))
                .circleCrop()
                .into(this.roundImageView);
        if(MediaObserver.getCurrentMediaPlayer().isPlaying())   this.roundImageView.startAnimation(AnimationUtils.loadAnimation(this, R.anim.spinning));
        else this.roundImageView.clearAnimation();
        nameText.setText(CardAdapter.adjustLength(songName));
        artistText.setText(CardAdapter.adjustLength(artistName));
    }
}