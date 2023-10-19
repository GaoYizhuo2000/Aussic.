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
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardGenreAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListArtistAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListSongAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListUsrAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataArrivedListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Artist;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.Song;
import au.edu.anu.Aussic.models.entity.User;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 * The filter of User results displaying
 */
public class UserSearchFragment extends Fragment implements OnDataArrivedListener, OnGeneralItemClickListener {

    private TextView users;
    private RecyclerView searchUserRecyclerView;

    public UserSearchFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnDataArrivedListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_user_search, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.users = view.findViewById(R.id.search_users_users);
        this.searchUserRecyclerView = view.findViewById(R.id.search_list_user_user_recyclerView);

        onDataArrivedResponse();
    }

    @Override
    public void onDataArrivedResponse() {

        List<GeneralItem> userList = new ArrayList<>();

        for (User user : RuntimeObserver.currentSearchResultUsers) userList.add(new GeneralItem(user));

        if(userList.isEmpty()) users.setText("Users:...\nno results...");
        else users.setText("Users:...");
        this.searchUserRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        this.searchUserRecyclerView.setAdapter(new ListUsrAdapter(userList, this));
    }
}