package au.edu.anu.Aussic.controller.homePages;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardGenreAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.Functions;
import au.edu.anu.Aussic.controller.Runtime.observer.OnMediaChangeListener;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements OnGeneralItemClickListener, OnMediaChangeListener {
    /** RecyclerView to display genres in a card format. */
    private RecyclerView cardRecyclerView;

    /** RecyclerView to display songs in a list format. */
    private RecyclerView listRecyclerView;

    /** TextView below the round image displaying song name. */
    private TextView roundUnderText;

    /** Circular ImageView displaying the currently playing song's image. */
    private ImageView roundImage;

    /** Flag indicating if the view is currently alive or destroyed. */
    private boolean isViewAlive;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onDestroyView() {
        this.isViewAlive = false;
        super.onDestroyView();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.isViewAlive = true;

        this.cardRecyclerView = view.findViewById(R.id.card_recyclerView);
        this.listRecyclerView = view.findViewById(R.id.list_recyclerView);
        this.roundUnderText = view.findViewById(R.id.round_image_name);
        this.roundImage = view.findViewById(R.id.spinning_round_image);
        this.roundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), SongActivity.class);
                startActivity(intent);
            }
        });

        setViewList();
        onMediaChangeResponse();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnMediaChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    /**
     * Sets up the data for card and list views.
     */
    public void setViewList(){
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<GeneralItem> songList = new ArrayList<>();
        List<GeneralItem> genreList = new ArrayList<>();
        for(Song song : RuntimeObserver.getCurrentSongList()) songList.add(new GeneralItem(song));
        for (Genre genre : RuntimeObserver.currentGenreList) genreList.add(new GeneralItem(genre));

        // Set up the RecyclerView with the fetched data
        if(RuntimeObserver.getCurrentSong() != null) setRoundImage(Functions.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName());
        cardRecyclerView.setAdapter(new CardGenreAdapter(genreList, this));

        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listRecyclerView.setAdapter(new ListSongAdapter(songList, this));
    }

    /**
     * Sets and styles the round image using the provided image URL and song name.
     *
     * @param imageUrl URL of the image to be displayed.
     * @param songName Name of the song associated with the image.
     */
    private void setRoundImage(String imageUrl,String songName){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(1200 * 0.8), (int)(1200 * 0.8)))
                .circleCrop()
                .into(this.roundImage);
        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying())   this.roundImage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinning));
        roundUnderText.setText(Functions.adjustLength(songName));
    }


    /**
     * Updates the animation of the round image based on media playback status.
     */
    @Override
    public void onMediaChangeResponse() {
        if(this.isViewAlive && RuntimeObserver.getCurrentMediaPlayer() != null){
            if ((RuntimeObserver.getCurrentMediaPlayer().isPlaying())) this.roundImage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinning));
            else this.roundImage.clearAnimation();
        }
    }
}