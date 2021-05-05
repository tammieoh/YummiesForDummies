package com.example.yummiesfordummies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

public class EmailPasswordActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private ImageView imageView;
//    private DatabaseReference userRef;
    private Button button_create;
    private Button button_login;
    private EditText editText_email;
    private EditText editText_password;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emailpassword);

        imageView = findViewById(R.id.imageView_main);
        button_create = findViewById(R.id.button_create);
        button_login = findViewById(R.id.button_login);
        editText_email = findViewById(R.id.editText_email);
        editText_password = findViewById(R.id.editText_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // database initialize;
        database = FirebaseDatabase.getInstance().getReference();

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        String main_image = "file:///android_asset/images/chef.jpeg";
        Picasso.get().load(main_image).into(imageView);

        button_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(editText_email.getText()) || TextUtils.isEmpty(editText_password.getText())) {
                    Toast.makeText(EmailPasswordActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    String email = editText_email.getText().toString();
                    String password = editText_password.getText().toString();
//                    if(database.child("users") == null) {
//                        Toast.makeText(EmailPasswordActivity.this, "Account doesn't exist", Toast.LENGTH_SHORT).show();
//                    }
//                    else {
//                        if(database.child(email).child("password").equals(password)) {
//                            signIn(email, password);
//                        }
//                        else {
//                            Toast.makeText(EmailPasswordActivity.this, "Password is incorrect", Toast.LENGTH_SHORT).show();
//                        }
//                    }
                    signIn(email, password);
                }
            }
        });

        button_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmailPasswordActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

//        FileInputStream serviceAccount =
//                null;
//        try {
//            serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        FirebaseOptions options = new FirebaseOptions.Builder()
//                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
//                .setDatabaseUrl("https://durable-student-295822-default-rtdb.firebaseio.com")
//                .build();
//
//        FirebaseApp.initializeApp(options);

//    @Override
//    public void onStart() {
//        super.onStart();
//        // check if user is signed in (non-null)
////        FirebaseUser currentUser = mAuth.getCurrentUser();
////        if(currentUser != null) {
////            reload();
////        }
//    }

    private void signIn(String email, String password) {
        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent intent = new Intent(EmailPasswordActivity.this, MainActivity.class);
                            SharedPreferences.Editor myEdit = sharedPreferences.edit();
                            myEdit.putString("userID", user.getUid());
                            myEdit.apply();
//                            intent.putExtra("user", user.getUid());
                            startActivity(intent);
//                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(EmailPasswordActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
        // [END sign_in_with_email]
    }


//    private void reload() { }

//    private void updateUI(FirebaseUser user) {
//
//    }
}
