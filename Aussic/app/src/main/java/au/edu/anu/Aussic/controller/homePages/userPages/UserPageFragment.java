package au.edu.anu.Aussic.controller.homePages.userPages;

import android.app.Dialog;
import android.content.Intent;
//import android.location.Location;
//import android.location.LocationListener;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import android.Manifest;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentAdapter;
import au.edu.anu.Aussic.controller.Runtime.Adapter.CommentItem;
import au.edu.anu.Aussic.controller.Runtime.observer.OnDataChangeListener;
import au.edu.anu.Aussic.controller.entityPages.SongActivity;
import au.edu.anu.Aussic.controller.homePages.userPages.FavouriteSongActivity;
import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.userAction.Comment;

/**
 * @author: u7516507, Evan Cheung
 * @author: u7603590, Jiawei Niu
 * @author: u7581818, Oscar Wei
 */

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link UserPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class UserPageFragment extends Fragment implements OnDataChangeListener {
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;

    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView email,location;
    private Button favorites, getLocation;
    private ImageView userPhoto;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Dialog dialog;


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

        // Retrieve arguments passed to the fragment if available.
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        RuntimeObserver.addOnDataChangeListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_userpage, container, false);

        email = rootView.findViewById(R.id.userEmail);
        favorites = rootView.findViewById(R.id.favoritesListButton);
        userPhoto = rootView.findViewById(R.id.userPhoto);
        location = rootView.findViewById(R.id.userLocation);
        getLocation = rootView.findViewById(R.id.getUserLocationButton);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        // Set up location request for continuous updates
        locationRequest = LocationRequest.create();
        locationRequest.setInterval(2000);  // 2 seconds
        locationRequest.setFastestInterval(1000);  // 5 seconds
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    if (location != null) {
                        String cityName = getCityNameByCoordinates(location.getLatitude(), location.getLongitude());
                        updateLocationInUI(cityName);
                    }
                }
            }
        };

        // Get Location button click
        getLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkLocationPermission()){
                    startLocationUpdates();
                } else {
                    requestLocationPermission();
                }
            }
        });

        // Directly load user data from runtime storage
        User usr = RuntimeObserver.currentUser;
        email.append(usr.username);
        Picasso.get().load(usr.iconUrl).into(userPhoto);

        setUpDialogue();

        // Change user photo button click
        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProfile();
            }
        });

        // Favourite song list button click
        favorites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavouriteSongActivity.class);
                startActivity(intent);
            }
        });

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        }
        return rootView;
    }

    /**
     * Starts requesting location updates.
     */
    private void startLocationUpdates() {
        try {
            fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());

        } catch (SecurityException e) {
            Toast.makeText(getActivity(), "Location permission was revoked by the user.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Update the displayed city name based on location.
     *
     * @param cityName The name of the city.
     */
    private void updateLocationInUI(String cityName) {
        location.setText("Location: " + cityName);
    }

    /**
     * Checks if location permission is granted.
     *
     * @return true if location permission is granted, false otherwise.
     */
    private boolean checkLocationPermission() {
        return ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * Requests location permission from the user.
     */
    private void requestLocationPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION)) {
            Toast.makeText(getActivity(), "Please allow location permission for proper functioning.", Toast.LENGTH_SHORT).show();
        }
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                startLocationUpdates();
            } else {
                Toast.makeText(getActivity(), "Location permission denied.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Determines the city name based on given latitude and longitude coordinates.
     *
     * @param lat Latitude of the location.
     * @param lon Longitude of the location.
     * @return The name of the city or an empty string if the city name couldn't be determined.
     */
    private String getCityNameByCoordinates(double lat, double lon) {
        String cityName = "";
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 10);
            if (addresses != null && !addresses.isEmpty()) {
                for (Address address : addresses) {
                    if (address.getLocality() != null && !address.getLocality().isEmpty()) {
                        cityName = address.getLocality();
                        break;
                    } else if (address.getAdminArea() != null && !address.getAdminArea().isEmpty()) {
                        cityName = address.getAdminArea();
                        break;
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cityName;
    }

    @Override
    public void onPause() {
        super.onPause();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }
    @Override
    public void onDestroy(){
        super.onDestroy();
        fusedLocationProviderClient.removeLocationUpdates(locationCallback);
    }

    private void setUpDialogue(){
        this.dialog = new Dialog(getContext());
        this.dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.dialog.setContentView(R.layout.change_profile_layout);
        ImageView imageView1 = dialog.findViewById(R.id.p1);
        ImageView imageView2 = dialog.findViewById(R.id.p2);
        ImageView imageView3 = dialog.findViewById(R.id.p3);
        ImageView imageView4 = dialog.findViewById(R.id.p4);
        ImageView imageView5 = dialog.findViewById(R.id.p5);
        ImageView imageView6 = dialog.findViewById(R.id.p6);
        ImageView imageView7 = dialog.findViewById(R.id.p7);
        ImageView imageView8 = dialog.findViewById(R.id.p8);

        FirestoreDao firestoreDao = new FirestoreDaoImpl();


        //imageView1.setClickable();
        imageView1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2Fdefault.jpg?alt=media");
                dialog.dismiss();
            }
        });

        imageView2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F2.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F3.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F4.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F5.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F6.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F7.png?alt=media");
                dialog.dismiss();
            }
        });

        imageView8.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                firestoreDao.updateUserImage("https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2F8.png?alt=media");
                dialog.dismiss();
            }
        });



    }

    private void showProfile() {
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

    @Override
    public void onDataChangeResponse() {
        Picasso.get().load(RuntimeObserver.currentUser.iconUrl).into(userPhoto);
    }
}