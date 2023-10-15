package au.edu.anu.Aussic.controller.homePages;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;


import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.View;
import android.view.ViewGroup;

import java.io.InputStreamReader;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.Runtime.observer.OnMediaChangeListener;
import au.edu.anu.Aussic.controller.homePages.P2P.MessagesFragment;
import au.edu.anu.Aussic.controller.homePages.userPages.UserPageFragment;
import au.edu.anu.Aussic.controller.loginPages.LoginActivity;
import au.edu.anu.Aussic.controller.searchPages.SearchActivity;
import au.edu.anu.Aussic.controller.Runtime.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;
import au.edu.anu.Aussic.models.userAction.UserAction;
import au.edu.anu.Aussic.models.userAction.UserActionFactory;



public class HomeActivity extends AppCompatActivity implements OnMediaChangeListener {

    FloatingActionButton fab;
    DrawerLayout drawerLayout;
    BottomNavigationView bottomNavigationView;
    NavigationView navigationView;
    private HomeFragment homeFragment = new HomeFragment();
    private UserPageFragment userPageFragment = new UserPageFragment();
    private MessagesFragment messagesFragment = new MessagesFragment();
    private Handler timerHandler = new Handler();
    private JsonObject jsonObject = null;
    private JsonArray jsonArray;
    private int arrayLength = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        RuntimeObserver.addOnMediaChangeListener(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab =findViewById(R.id.fab);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        Toolbar toolbar = findViewById(R.id.toolbar);

        SearchView searchView = findViewById(R.id.searchView);

        // Set an OnClickListener on the SearchView
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Start the SearchActivity
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });
        // Set the OnClickListener for all children views of SearchView
        setOnClickListenerForAllChildren(searchView, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, SearchActivity.class);
                startActivity(intent);
            }
        });


        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open_nav,R.string.close_nav);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        if(savedInstanceState == null){
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_layout, homeFragment).commit();
            navigationView.setCheckedItem(R.id.nav_home);
        }

//        replaceFragment(homeFragment);

        bottomNavigationView.setBackground(null);
        bottomNavigationView.setOnItemSelectedListener(item -> {

                if(item.getItemId() == R.id.home) replaceFragment(homeFragment);

                else if(item.getItemId() == R.id.user_profile) replaceFragment(userPageFragment);

            return true;
        });

        navigationView.setNavigationItemSelectedListener(menuItem -> {
            // Closing the drawer after selecting
            drawerLayout.closeDrawer(GravityCompat.START);
            if(menuItem.getItemId() == R.id.nav_home) replaceFragment(homeFragment);

            else if(menuItem.getItemId() == R.id.nav_messages) replaceFragment(messagesFragment);

            else if(menuItem.getItemId() == R.id.nav_logout) {
                // Finish the Home activity and return back to the login activity
                Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }

            return true;
        });

        if(RuntimeObserver.getCurrentMediaPlayer() != null){
            RuntimeObserver.getCurrentMediaPlayer().start();
            fab.setImageResource(R.drawable.ic_bottom_stop);
        }



        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (RuntimeObserver.getCurrentMediaPlayer() != null) {
                    if (RuntimeObserver.getCurrentMediaPlayer().isPlaying()) {
                        RuntimeObserver.getCurrentMediaPlayer().pause();
                        fab.setImageResource(R.drawable.ic_bottom_play);
                    } else {
                        RuntimeObserver.getCurrentMediaPlayer().start();
                        fab.setImageResource(R.drawable.ic_bottom_stop);
                    }
                    RuntimeObserver.notifyOnMediaChangeListeners();
                }
            }
        });

        loadJsonObjectFromRawResource(R.raw.useractions);
        timerHandler.postDelayed(timerRunnable, 10000);
    }
    // Outside Oncreate
    private void replaceFragment(Fragment fragment) {
        // Find the existing fragment
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_layout, fragment);
        fragmentTransaction.commit();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            // load show user behavior every 10 secs
            loadShowData();

            // Schedule the next execution
            timerHandler.postDelayed(this, 5000);
        }
    };
    public void loadJsonObjectFromRawResource(int resourceId) {
        try {
            // Open the resource stream
            InputStreamReader reader = new InputStreamReader(getResources().openRawResource(resourceId));

            // Parse the string content to a JsonObject using Gson
            jsonArray = JsonParser.parseReader(reader).getAsJsonArray();
            arrayLength = jsonArray.size();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadShowData() {
        FirestoreDao firestoreDao = new FirestoreDaoImpl();
        firestoreDao.getRandomUseraction().thenAccept(useraction -> {
            Gson gson = new Gson();
            JsonObject jsonObject = gson.toJsonTree(useraction).getAsJsonObject();
            UserAction userAction = UserActionFactory.createUserAction(jsonObject);
            userAction.update();
           // Toast.makeText(this, userAction.getToastMessage(), Toast.LENGTH_SHORT).show();

        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(RuntimeObserver.getCurrentMediaPlayer() != null){
            RuntimeObserver.getCurrentMediaPlayer().release();
            RuntimeObserver.setMediaPlayer(null);
        }
    }

    private void setOnClickListenerForAllChildren(View parent, View.OnClickListener listener) {
        if (!(parent instanceof ViewGroup)) {
            return;
        }
        ViewGroup viewGroup = (ViewGroup) parent;
        for (int i = 0; i < viewGroup.getChildCount(); i++) {
            View child = viewGroup.getChildAt(i);
            child.setOnClickListener(listener);
            setOnClickListenerForAllChildren(child, listener);
        }
    }

    @Override
    public void onMediaChangeResponse() {
        if(RuntimeObserver.getCurrentMediaPlayer() != null){
            if ((RuntimeObserver.getCurrentMediaPlayer().isPlaying())) this.fab.setImageResource(R.drawable.ic_bottom_stop);
            else this.fab.setImageResource(R.drawable.ic_bottom_play);
        }
    }
}