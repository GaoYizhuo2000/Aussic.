package au.edu.anu.Aussic.controller.searchPages;

import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.models.entity.Media;

public class SearchActivity extends AppCompatActivity {
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        FloatingActionButton fab = findViewById(R.id.search_fab);

        this.mediaPlayer = Media.mediaPlayer;
        if(this.mediaPlayer.isPlaying()) fab.setImageResource(R.drawable.ic_bottom_stop);
        else fab.setImageResource(R.drawable.ic_bottom_play);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mediaPlayer.isPlaying()) {
                    mediaPlayer.pause();
                    fab.setImageResource(R.drawable.ic_bottom_play);
                }
                else {
                    mediaPlayer.start();
                    fab.setImageResource(R.drawable.ic_bottom_stop);
                }
            }
        });

    }
}