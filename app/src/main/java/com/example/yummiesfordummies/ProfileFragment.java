package com.example.yummiesfordummies;

import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
    private static final int RESULT_OK = -1;
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
    private PhotoAdapter adapter;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath;
//    private ImageView imageView_tester;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_profile, container, false);
        textView_username = view.findViewById(R.id.textView_username);
        imageView_camera = view.findViewById(R.id.imageView_camera);
        favorites_button = view.findViewById(R.id.button_favorites);
        recyclerView = view.findViewById(R.id.recyclerView_photos);
//        imageView_tester = view.findViewById(R.id.imageView_tester);
//        GridLayoutManager gridLayoutManager = new GridLayoutManager(view.getContext(), 3);
//        recyclerView.setLayoutManager(gridLayoutManager);

        database = FirebaseDatabase.getInstance().getReference("users");
        sharedPreferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

//        adapter = new PhotoAdapter(photoUrls);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        database.child(sharedPreferences.getString("userID", "")).child("username").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                textView_username.setText(snapshot.getValue().toString().toUpperCase());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        database.child(sharedPreferences.getString("userID", "")).child("photos").get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                }
                else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    for(DataSnapshot snapshot : task.getResult().getChildren()) {
                        String data = snapshot.getValue().toString();
                        photoUrls.add(data);
                    }
                    adapter = new PhotoAdapter(photoUrls);
                    recyclerView.setAdapter(adapter);
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                    Log.d("getchild", "adapter attached");
                }
//                adapter = new PhotoAdapter(photoUrls);
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
            }

//            @Override
//            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
//                    String data = snapshot.getValue().toString();
//                    photoUrls.add(data);
//                    // jpg_0502, jpg_0503
//                }
//                Log.d("photos", photoUrls.toString());
//                adapter = new PhotoAdapter(photoUrls);
//                recyclerView.setAdapter(adapter);
//                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
//            }

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


        // probably need to call all the photos and figure out how to store the links into an array list
//        adapter = new PhotoAdapter(photoUrls);
//        recyclerView.setAdapter(adapter);
//        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

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
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI); // where the camera is going to save the photo
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE); // launches camera app
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
//            if(data.getData() != null) {
//                ParcelFileDescriptor parcelFileDescriptor = getContext().getContentResolver().openFileDescriptor(data.getData(), "r");
//                FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();

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

            Bitmap bitmap;
            Uri uri = Uri.fromFile(new File((currentPhotoPath)));
            bitmap = BitmapFactory.decodeFile(uri.getEncodedPath());
//                    bitmap = cropAndScale(bitmap, 300); // if you mind scaling
//            imageView_tester.setImageBitmap(bitmap);
            database.child(sharedPreferences.getString("userID", "")).child("photos").push().setValue(currentPhotoPath);
            database.child("photos").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                        String data = snapshot.getKey();
                        photoUrls.add(data);
                        // jpg_0502, jpg_0503
                    }
                    Log.d("photos", photoUrls.toString());
                    adapter.updateList(photoUrls);
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer_home, new ProfileFragment())
                            .commit();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
//        }
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
