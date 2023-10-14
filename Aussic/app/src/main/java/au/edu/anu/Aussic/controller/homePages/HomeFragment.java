package au.edu.anu.Aussic.controller.homePages;

import android.content.Intent;
import android.media.MediaPlayer;
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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListAdapter;
import au.edu.anu.Aussic.controller.songPages.SongActivity;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnItemSpecClickListener;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment implements OnItemSpecClickListener, OnDataArrivedListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView cardRecyclerView;
    private RecyclerView listRecyclerView;
    private TextView roundUnderText;

    public HomeFragment() {
        // Required empty public constructor
        RuntimeObserver.homeFragment = this;
        RuntimeObserver.addOnDataArrivedListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        this.cardRecyclerView = view.findViewById(R.id.card_recyclerView);
        this.listRecyclerView = view.findViewById(R.id.list_recyclerView);
        this.roundUnderText = view.findViewById(R.id.round_image_name);
        RuntimeObserver.roundImage = view.findViewById(R.id.spinning_round_image);
        RuntimeObserver.roundImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intent = new Intent(getContext(), SongActivity.class);
                startActivity(intent);
            }
        });


        if(RuntimeObserver.getCurrentSongList() == null || RuntimeObserver.getCurrentSongList().isEmpty()){}
        else setViewList();



    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false);
    }

    public void setViewList(){
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<ItemSpec> itemList = new ArrayList<>();
        for(Song song : RuntimeObserver.getCurrentSongList()) itemList.add(new ItemSpec(CardAdapter.adjustLength(song.getSongName()), CardAdapter.makeImageUrl(200, 200, song.getUrlToImage()), song.getArtistName(), song));

        // Set up the RecyclerView with the fetched data
        cardRecyclerView.setAdapter(new CardAdapter(itemList, this));
        if(RuntimeObserver.getCurrentSong() != null) setRoundImage(CardAdapter.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName());

        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listRecyclerView.setAdapter(new ListAdapter(itemList, this));
    }
    public void setViewList(List<Map<String, Object>> maps){
        cardRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<ItemSpec> itemList = new ArrayList<>();

        //for(Map<String, Object> map : maps) RuntimeObserver.getCurrentSongList().add(GsonSongLoader.loadSong(map));
        for(Song song : RuntimeObserver.getCurrentSongList()) itemList.add(new ItemSpec(CardAdapter.adjustLength(song.getSongName()), CardAdapter.makeImageUrl(200, 200, song.getUrlToImage()), song.getArtistName(), song));

        // Set up the RecyclerView with the fetched data
        cardRecyclerView.setAdapter(new CardAdapter(itemList, this));
        if(RuntimeObserver.getCurrentSong() != null) setRoundImage(CardAdapter.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName());

        listRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        listRecyclerView.setAdapter(new ListAdapter(itemList, this));
    }

    private void setRoundImage(String imageUrl,String songName){

        Glide.with(this)
                .load(imageUrl)
                .apply(new RequestOptions().override((int)(1200 * 0.8), (int)(1200 * 0.8)))
                .circleCrop()
                .into(RuntimeObserver.roundImage);
        if(RuntimeObserver.getCurrentMediaPlayer().isPlaying())   RuntimeObserver.roundImage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinning));
        roundUnderText.setText(CardAdapter.adjustLength(songName));
    }

    @Override
    public void onItemClicked(ItemSpec itemSpec) throws IOException {
        RuntimeObserver.setCurrentSong(itemSpec.getSong());
        RuntimeObserver.getCurrentMediaPlayer().pause();
        RuntimeObserver.getCurrentMediaPlayer().release();
        RuntimeObserver.setMediaPlayer(new MediaPlayer());
        RuntimeObserver.getCurrentMediaPlayer().setDataSource(itemSpec.getSong().getUrlToListen());
        RuntimeObserver.getCurrentMediaPlayer().prepare();
        RuntimeObserver.getCurrentMediaPlayer().setLooping(true);
        RuntimeObserver.getCurrentMediaPlayer().start();
        setRoundImage(CardAdapter.makeImageUrl(200, 200, RuntimeObserver.getCurrentSong().getUrlToImage()), RuntimeObserver.getCurrentSong().getSongName());
        Intent intent = new Intent(getContext(), SongActivity.class);
        // add more extras if necessary
        startActivity(intent);
    }

    @Override
    public void onDataArrivedResponse() {
        if(RuntimeObserver.getCurrentMediaPlayer() != null){
            if ((RuntimeObserver.getCurrentMediaPlayer().isPlaying())) RuntimeObserver.roundImage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinning));
            else RuntimeObserver.roundImage.clearAnimation();
        }

    }
}