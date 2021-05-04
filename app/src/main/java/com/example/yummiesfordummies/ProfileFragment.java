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
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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
    private List<String> photoUrls = new ArrayList<>();
    private SharedPreferences sharedPreferences;
    private DatabaseReference database;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView_username = view.findViewById(R.id.textView_username);
        imageView_camera = view.findViewById(R.id.imageView_camera);
        recyclerView = view.findViewById(R.id.recyclerView_photos);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
        recyclerView.setLayoutManager(gridLayoutManager);

        database = FirebaseDatabase.getInstance().getReference("users");
        sharedPreferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        textView_username.setText(database.child(sharedPreferences.getString("userID", "")).child("username").getKey());
        String camera_icon = "file:///android_asset/images/camera.png";
        Picasso.get().load(camera_icon).into(imageView_camera);

        imageView_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("camera", "CHEESE");
                dispatchTakePictureIntent();
            }
        });

        //    @Override
        //    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
        //            Bundle extras = data.getExtras();
        //            Bitmap imageBitmap = (Bitmap) extras.get("data");
        ////            imageView.setImageBitmap(imageBitmap);
        //        }
        //    }

        database.child("photos").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot snapshot : dataSnapshot.getChildren()){
                    String data = snapshot.getKey().toString();
                    photoUrls.add(data);
                    // jpg_0502, jpg_0503
                }
                Log.d("photos", photoUrls.toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // probably need to call all the photos and figure out how to store the links into an array list
        PhotoAdapter adapter = new PhotoAdapter(photoUrls);
        recyclerView.setAdapter(adapter);

        return view;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getContext().getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {

                Uri photoURI = FileProvider.getUriForFile(getContext(),
                        "com.example.android.fileprovider",
                        photoFile);

//                ContentValues values = new ContentValues();
//                values.put(MediaStore.Images.Media.TITLE, "Image File name");
//                Uri photoURI = getContext().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
//                // store the file in the database

//                        .addValueEventListener(new ValueEventListener() {
//                    @Override
//                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
////                        for (DataSnapshot snapshot : dataSnapshot.getChildren()){
////                            String data = snapshot.getKey().toString();
////                            photoUrls.add(data);
////                            // jpg_0502, jpg_0503
////                        }
//                        if (dataSnapshot.child(sharedPreferences.getString("userID", "")).child("photos").child(photoURI.toString()) == null) {
//                            database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(photoURI.toString());
//                        }
//                    }

//                    @Override
//                    public void onCancelled(@NonNull DatabaseError error) {
//
//                    }
//                });
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if(data.getData() != null) {
                try {
                    ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(data.getData(), "r");
                    FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
                    database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(fileDescriptor.toString());
                    parcelFileDescriptor.close();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            else {
                database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(data.getExtras().get("data").toString());
            }
//            Bundle extras = data.getExtras();
//            Log.d("extras", extras.get("data").toString());
//            database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(extras.get("data").toString());
//            Bitmap imageBitmap = (Bitmap) extras.get("data");
//            imageView.setImageBitmap(imageBitmap);
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
