package com.example.yummiesfordummies;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RegisterActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private DatabaseReference database;
    private DatabaseReference userRef;
    private Button button_register;
    private EditText editText_email;
    private EditText editText_username;
    private EditText editText_password;
    private UserProfileChangeRequest profileUpdate;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        button_register = findViewById(R.id.button_register);
        editText_email = findViewById(R.id.editText_email);
        editText_username = findViewById(R.id.editText_username);
        editText_password = findViewById(R.id.editText_password);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // database initialize;
        database = FirebaseDatabase.getInstance().getReference("users");
//        userRef = database.child("users");

        button_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // if(TextUtils.isEmpty(title.text) || TextUtils.isEmpty(desc.text)
                //                || TextUtils.isEmpty(interpret.text.toString()) || selected_emotion == "") {
                //                toastError("Missing Fields")
                if(TextUtils.isEmpty(editText_email.getText()) || TextUtils.isEmpty(editText_username.getText())  || TextUtils.isEmpty(editText_password.getText())) {
                    Toast.makeText(RegisterActivity.this, "Missing fields", Toast.LENGTH_SHORT).show();
                }
                else {
                    // make an if statement to check if email exists in database
                    String email = editText_email.getText().toString();
                    String username = editText_username.getText().toString();
                    String password = editText_password.getText().toString();
                    createAccount(email, username, password);
                }
            }
        });
    }

    private void createAccount(String email, String username, String password) {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "createUserWithEmail:success");

                            FirebaseUser user = mAuth.getCurrentUser();
                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()) {
                                        Log.d("username", username);
//                                        SharedPreferences.Editor myEdit = sharedPreferences.edit();
//                                        myEdit.putString("userID", String.valueOf(database.push()));
//                                        myEdit.apply();
                                        database.child(user.getUid()).setValue(new User(user.getUid(), user.getDisplayName(), user.getEmail(), new ArrayList<String>()));
                                    }
                                }
                            });
//                            user.get
//                            DatabaseReference newRef = database.child("users").push();
//                            newRef.setValue(new User(username, password, email, new ArrayList<String>()));
//                            database.addListenerForSingleValueEvent(new ValueEventListener() {
//                                @Override
//                                public void onDataChange(@NonNull DataSnapshot snapshot) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError error) {
//
//                                }
//                            });
//                            database.child("users").addValueEventListener()
//                            database.push().setValue(new User(user.getUid(), user.getDisplayName(), user.getEmail(), new ArrayList<String>()));
//                            database.child("users").child(user.getUid()).child("username").setValue(username);
//                            database.child("users").child(user.getUid()).child("email").setValue(email);
//                            database.child("users").child(user.getUid()).child("favorites").setValue(new ArrayList<String>());
//                            database.child("users").setValue(new User(user.getUid(), user.getDisplayName(), user.getEmail(), new ArrayList<String>()));
//                            database.child("users").child(email).child("username").setValue(username);
//                            database.child("users").child(email).child("password").setValue(password);
//                            database.child("users").child(email).child("favorites").setValue(new ArrayList<String>());

                            Intent intent = new Intent(RegisterActivity.this, EmailPasswordActivity.class);
                            startActivity(intent);
                        } else {
                            // If sign in fails, display a message to the user.
//                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
                            Toast.makeText(RegisterActivity.this, "Authentication failed or Account already exists",
                                    Toast.LENGTH_SHORT).show();
//                            updateUI(null);
                        }
                    }
                });
    }
//    private void sendEmailVerification() {
//        // Send verification email
//        // [START send_email_verification]
//        final FirebaseUser user = mAuth.getCurrentUser();
//        user.sendEmailVerification()
//                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        // Email sent
//                    }
//                });
//        // [END send_email_verification]
//    }
}
