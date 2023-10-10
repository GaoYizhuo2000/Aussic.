package au.edu.anu.Aussic.controller.homePages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPageFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView email;
    private Button favorites, songList;
    private ImageView userPhoto;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public UserPageFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment LibraryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static UserPageFragment newInstance(String param1, String param2) {
        UserPageFragment fragment = new UserPageFragment();
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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userpage, container, false);

        email = rootView.findViewById(R.id.userEmail);
        favorites = rootView.findViewById(R.id.favoritesListButton);
        songList = rootView.findViewById(R.id.songListButton);
        userPhoto = rootView.findViewById(R.id.userPhoto);
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getUserdata(FirebaseAuth.getInstance().getCurrentUser())
                .thenAccept(userdata -> {
                    String username = (String) userdata.get("username");
                    email.append(username);
                    String iconUrl = (String) userdata.get("iconUrl");
                    Picasso.get().load(iconUrl ).into(userPhoto);

                });


        // Example: Click on the profile image to change it
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Implement functionality to change the profile image
                Toast.makeText(getContext(), "Change profile image feature not implemented yet.", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for collections list
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to the collections page or implement another functionality
                Toast.makeText(getContext(), "Viewing collections!", Toast.LENGTH_SHORT).show();
            }
        });

        // Click listener for playlist
        songList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Navigate to the playlists page or implement another functionality
                Toast.makeText(getContext(), "Viewing playlists!", Toast.LENGTH_SHORT).show();
            }
        });
        return rootView;
       // return inflater.inflate(R.layout.fragment_userpage, container, false);
    }
}