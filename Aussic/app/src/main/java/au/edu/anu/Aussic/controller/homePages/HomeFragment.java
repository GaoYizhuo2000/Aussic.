package au.edu.anu.Aussic.controller.homePages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.Adapter.CardAdapter;
import au.edu.anu.Aussic.controller.homePages.Adapter.CardSpec;
import au.edu.anu.Aussic.models.SongLoader.GsonSongLoader;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.observer.MediaObserver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView recyclerView;
    private TextView roundUnderText;

    public HomeFragment() {
        // Required empty public constructor
        MediaObserver.homeFragment = this;
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


        this.recyclerView = view.findViewById(R.id.recyclerView);
        this.roundUnderText = view.findViewById(R.id.round_image_name);
        MediaObserver.roundImage = view.findViewById(R.id.spinning_round_image);


        if(MediaObserver.getCurrentSongList() == null || MediaObserver.getCurrentSongList().isEmpty()){
            FirestoreDao firestoreDao = new FirestoreDaoImpl();
            firestoreDao.getRandomSongs(100).thenAccept(results->{
                List<Map<String, Object>> maps = new ArrayList<>();
                maps.addAll(results);
                setCardViewList(maps);
            });
        } else {
            setCardViewList();
        }


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

    public void setCardViewList(){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<CardSpec> cardList = new ArrayList<>();
        for(Song song : MediaObserver.getCurrentSongList()) cardList.add(new CardSpec(CardAdapter.adjustLength(song.getSongName()), CardAdapter.makeImageUrl(200, 200, song.getUrlToImage())));

        // Set up the RecyclerView with the fetched data
        recyclerView.setAdapter(new CardAdapter(cardList));
        if(MediaObserver.getCurrentSong() != null) setRoundImage(CardAdapter.makeImageUrl(200, 200, MediaObserver.getCurrentSong().getUrlToImage()), MediaObserver.getCurrentSong().getSongName());
    }
    public void setCardViewList(List<Map<String, Object>> maps){
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        List<CardSpec> cardList = new ArrayList<>();

        for(Map<String, Object> map : maps) MediaObserver.getCurrentSongList().add(GsonSongLoader.loadSong(map));
        for(Song song : MediaObserver.getCurrentSongList()) cardList.add(new CardSpec(CardAdapter.adjustLength(song.getSongName()), CardAdapter.makeImageUrl(200, 200, song.getUrlToImage())));

        // Set up the RecyclerView with the fetched data
        recyclerView.setAdapter(new CardAdapter(cardList));
        if(MediaObserver.getCurrentSong() != null) setRoundImage(CardAdapter.makeImageUrl(200, 200, MediaObserver.getCurrentSong().getUrlToImage()), MediaObserver.getCurrentSong().getSongName());
    }

    private void setRoundImage(String imageUrl,String songName){

        Glide.with(this)
                .load(imageUrl)
                .circleCrop()
                .into(MediaObserver.roundImage);
        if(MediaObserver.getCurrentMediaPlayer().isPlaying())   MediaObserver.roundImage.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.spinning));
        roundUnderText.setText(CardAdapter.adjustLength(songName));
    }
}