package com.example.yummiesfordummies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ProfileFragment  extends Fragment {
    private static final int RESULT_OK = 0;
    //    private static final int RESULT_OK = 0;
    private View view;
    private TextView textView_username;
    private RecyclerView recyclerView;
    private ImageView imageView_camera;
    private Button favorites_button;
    private List<String> photoUrls = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private DatabaseReference database;
    private File photoFile = null;
    private PhotoAdapter adapter = new PhotoAdapter(photoUrls);
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView_username = view.findViewById(R.id.textView_username);
        imageView_camera = view.findViewById(R.id.imageView_camera);
        favorites_button = view.findViewById(R.id.button_favorites);
        recyclerView = view.findViewById(R.id.recyclerView_photos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        database = FirebaseDatabase.getInstance().getReference("users");
        sharedPreferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        database.child(sharedPreferences.getString("userID", "")).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView_username.setText(snapshot.getValue().toString().toUpperCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


//        textView_username.setText(database.child(sharedPreferences.getString("userID", "")).child("username").toString());
        String camera_icon = "file:///android_asset/images/camera.png";
        Picasso.get().load(camera_icon).into(imageView_camera);

        favorites_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), FavoritesActivity.class);
                startActivity(intent);
            }
        });

        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("camera", "CHEESE");
                dispatchTakePictureIntent();
            }
        });

        database.child("photos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getKey().toString();
                    photoUrls.add(data);
                    // jpg_0502, jpg_0503
                }
                Log.d("photos", photoUrls.toString());
                adapter.updateList(photoUrls);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // probably need to call all the photos and figure out how to store the links into an array list
        adapter = new PhotoAdapter(photoUrls);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
//            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.d("Profile Fragment", "can't create image file");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra("photoURL", photoURI); // where the camera is going to save the photo
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); // launches camera app
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(data.getData() != null) {
//                ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(data.getData(), "r");
//                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(photoFile.toString());
                database.child("photos").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String data = snapshot.getKey().toString();
                            photoUrls.add(data);
                            // jpg_0502, jpg_0503
                        }
                        Log.d("photos", photoUrls.toString());
                        adapter.updateList(photoUrls);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
//                parcelFileDescriptor.close();

//            }
//            else {
//                database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(data.getExtras().get("data").toString());
//            }
//            Bundle extras = data.getExtras();
//            Log.d("extras", extras.get("data").toString());
//            database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(extras.get("data").toString());
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
            }
        }
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
}
