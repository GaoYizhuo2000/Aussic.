package au.edu.anu.Aussic.controller.homePages.P2P;

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
import au.edu.anu.Aussic.controller.Runtime.Adapter.CardGenreAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.GeneralItem;
import au.edu.anu.Aussic.controller.Runtime.Adapter.ListUsrAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.MessagesAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.OnGeneralItemClickListener;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.Genre;
import au.edu.anu.Aussic.models.entity.User;

/**
 * @author: u7516507, Evan Cheung
 */

/**
 * A simple {@link Fragment} subclass.
 */
public class MessagesFragment extends Fragment implements OnGeneralItemClickListener, OnDataChangeListener {

    /** Recycler view to display list of messages. */
    private RecyclerView recyclerView;

    public MessagesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        RuntimeObserver.addOnDataChangeListener(this);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_messages, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        this.recyclerView = view.findViewById(R.id.messages_recyclerView);

        onDataChangeResponse();
    }


    /**
     * Responds to realtime changes in data, updates the list of available sessions.
     */
    @Override
    public void onDataChangeResponse() {
        if(!RuntimeObserver.currentSessionsAvailableUsers.isEmpty() && !RuntimeObserver.currentUserSessions.isEmpty()) {
            List<GeneralItem> sessionList = new ArrayList<>();

            for (User user : RuntimeObserver.currentSessionsAvailableUsers)  sessionList.add(new GeneralItem(user));

            if (this.recyclerView.getAdapter() == null) this.recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
            if(recyclerView.getAdapter() == null)  this.recyclerView.setAdapter(new ListUsrAdapter(sessionList, this));
            else if(recyclerView.getAdapter().getItemCount() != sessionList.size()) this.recyclerView.setAdapter(new ListUsrAdapter(sessionList, this));
        }
    }
}