package au.edu.anu.Aussic.controller.homePages;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.databinding.ActivityHomePageBinding;
import au.edu.anu.Aussic.models.userAction.*;

public class homePageActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityHomePageBinding binding;
    private Handler timerHandler = new Handler();

    private JsonObject jsonObject = null;
    private JsonArray jsonArray;
    private int arrayLength = 0;
    private int currentID = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomePageBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarHomePage.toolbar);
        binding.appBarHomePage.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_slideshow)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        // Start the timer
        loadJsonObjectFromRawResource(R.raw.useractions);
        timerHandler.postDelayed(timerRunnable, 10000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home_page, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home_page);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            // load show user behavior every 10 secs
            loadShowData();

            // Schedule the next execution
            timerHandler.postDelayed(this, 10000);
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
        // mimic user behavior every 10 seconds
        jsonObject = jsonArray.get(currentID).getAsJsonObject();
        UserAction userAction = UserActionFactory.createUserAction(jsonObject);
        Toast.makeText(this, userAction.getToastMessage(), Toast.LENGTH_SHORT).show();
        currentID += 1;
        if(currentID >= arrayLength) currentID = currentID % arrayLength;
    }
}