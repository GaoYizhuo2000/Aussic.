package au.edu.anu.Aussic.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.controller.observer.RuntimeObserver;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

public class LoginActivity extends AppCompatActivity {
    private EditText username, password;
    private Button buttonSignIn, buttonSignUp;
    private FirebaseAuth mAuth;
    private ToggleButton viewable;

    private static final String TAG = "EmailPassword";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (EditText) findViewById(R.id.UserName);
        password = (EditText) findViewById(R.id.UserPassword);
        buttonSignIn = (Button) findViewById(R.id.SignIN);
        buttonSignUp = (Button) findViewById(R.id.SignUP);
        viewable = (ToggleButton) findViewById(R.id.toggleButton);


        viewable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });


        buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user_name = username.getText().toString().trim();
                String pass_word = password.getText().toString().trim();
                if (user_name.isEmpty()) {
                    username.setError("Email is empty");
                    username.requestFocus();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(user_name).matches()) {
                    username.setError("Enter the valid email");
                    username.requestFocus();
                    return;
                }
                if (pass_word.isEmpty()) {
                    password.setError("Password is empty");
                    password.requestFocus();
                    return;
                }
                if (pass_word.length() < 6) {
                    password.setError("Length of password is more than 6");
                    password.requestFocus();
                    return;
                }
                mAuth.signInWithEmailAndPassword(user_name, pass_word).addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "signInWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    // Load current usr into runtime
                                    FirestoreDao firestoreDao = new FirestoreDaoImpl();
                                    firestoreDao.getUserdata(user)
                                            .thenAccept(userdata -> {
                                                // Loading the usr icon url
                                                String iconUrl = (String) userdata.get("iconUrl");
                                                User newUsr = new User(user_name, iconUrl);

                                                for(String songID : (List<String>)userdata.get("favorites")) newUsr.addFavorites(songID);
                                                for(String songID : (List<String>)userdata.get("likes")) newUsr.addLikes(songID);
                                                RuntimeObserver.currentUser = newUsr;
                                            });

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "signInWithEmail:failure", task.getException());
                                    Toast.makeText(LoginActivity.this, "Wrong Email Or Password!",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        }
                        );
            }
        });

        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        if (user != null) {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.putExtra("user", user);
            startActivity(intent);
        }
    }
}