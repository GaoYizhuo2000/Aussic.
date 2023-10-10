package au.edu.anu.Aussic.controller.searchPages;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.Adapter.CardAdapter;
import au.edu.anu.Aussic.controller.homePages.Adapter.ItemSpec;
import au.edu.anu.Aussic.controller.homePages.Adapter.ListAdapter;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.observer.ChangeListener;
import au.edu.anu.Aussic.models.observer.MediaObserver;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link GeneralSearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GeneralSearchFragment extends Fragment implements ChangeListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView searchRecyclerView;

    public GeneralSearchFragment() {
        // Required empty public constructor
        MediaObserver.addChangeListener(this);
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AllTypesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static GeneralSearchFragment newInstance(String param1, String param2) {
        GeneralSearchFragment fragment = new GeneralSearchFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.searchRecyclerView = view.findViewById(R.id.search_list_recyclerView);
        onChange();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_general_search, container, false);
    }

    @Override
    public void onChange() {
        if(MediaObserver.getCurrentSongList() != null && !MediaObserver.getCurrentSongList().isEmpty()) {
            List<ItemSpec> itemList = new ArrayList<>();
            for (Song song : MediaObserver.getCurrentSongList())
                itemList.add(new ItemSpec(CardAdapter.adjustLength(song.getSongName()), CardAdapter.makeImageUrl(200, 200, song.getUrlToImage()), song.getArtistName()));

            this.searchRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            this.searchRecyclerView.setAdapter(new ListAdapter(itemList));
        }
    }
}