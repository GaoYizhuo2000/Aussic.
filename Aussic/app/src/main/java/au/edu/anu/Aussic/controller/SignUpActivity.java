package au.edu.anu.Aussic.controller;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import au.edu.anu.Aussic.R;
import au.edu.anu.Aussic.controller.homePages.HomeActivity;
import au.edu.anu.Aussic.models.entity.User;
import au.edu.anu.Aussic.models.firebase.FirestoreDao;
import au.edu.anu.Aussic.models.firebase.FirestoreDaoImpl;

import androidx.annotation.NonNull;

import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private Button buttonSignUp;
    private EditText usernameSignUp, passwordSignUp;
    private FirebaseAuth mAuth;
    private static final String TAG = "EmailPassword";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        usernameSignUp=findViewById(R.id.signUpUsername);
        passwordSignUp=findViewById(R.id.signUpPassword);
        buttonSignUp=findViewById(R.id.SignUp);

        mAuth=FirebaseAuth.getInstance();
        buttonSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = usernameSignUp.getText().toString().trim();
                String password= passwordSignUp.getText().toString().trim();
                if(email.isEmpty())
                {
                    usernameSignUp.setError("Email is empty");
                    usernameSignUp.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches())
                {
                    usernameSignUp.setError("Enter the valid email address");
                    usernameSignUp.requestFocus();
                    return;
                }
                if(password.isEmpty())
                {
                    passwordSignUp.setError("Enter the password");
                    passwordSignUp.requestFocus();
                    return;
                }
                if(password.length()<6) {
                    passwordSignUp.setError("Length of the password should be more than 6");
                    passwordSignUp.requestFocus();
                    return;
                }
//
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Log.d(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    //create userdata and upload to firestore

                                    FirestoreDao firestoreDao = new FirestoreDaoImpl();
                                    firestoreDao.addUserdata(new User(email, "https://firebasestorage.googleapis.com/v0/b/aussic-52582.appspot.com/o/icon%2Fdefault.jpg?alt=media"));

                                    updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    updateUI(null);
                                }
                            }
                        });
            }
        });
    }

    private void updateUI(FirebaseUser user) {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.putExtra("user", user);
        startActivity(intent);
    }
}
