package com.example.yummiesfordummies;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class FavoritesActivity extends AppCompatActivity{

    private List<Result> results = new ArrayList<>();
    private RecyclerView recyclerView;
    private FavoriteAdapter adapter;
    private TextView textView_results;
    private SearchView searchView_results;
    private SharedPreferences sharedPreferences;
    private DatabaseReference database;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        recyclerView = findViewById(R.id.recyclerView_results);
        textView_results = findViewById(R.id.textView_numResults);
        searchView_results = findViewById(R.id.searchView_results);

        database = FirebaseDatabase.getInstance().getReference("users");
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);

        database.child(sharedPreferences.getString("userID", "")).child("favorites").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Result result = new Result(snapshot.getKey());
//                            Result result = snapshot.getValue(Result.class);
                    results.add(result);
//                            System.out.println(user.email);
                }
                adapter = new FavoriteAdapter(results);
                recyclerView.setAdapter(adapter);
                recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                textView_results.setText(getString(R.string.results, Integer.toString(adapter.getItemCount())));

                searchView_results.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String newText) {
                        filter(newText);
                        return true;
                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
//        Log.d("results", results.toString());

//        adapter = new FavoriteAdapter(results);
//        recyclerView.setAdapter(adapter);
//        System.out.println(adapter.getItemCount());
//        Log.d("list_count", Integer.toString(adapter.getItemCount()));
//        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
//
//
//
//       textView_results.setText(getString(R.string.results, Integer.toString(adapter.getItemCount())));
//       searchView_results.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                filter(newText);
//                return true;
//            }
//        });
//    }
    }

    // helper method for filtering
    public void filter(String text){
        ArrayList<Result> temp = new ArrayList();
        for(Result r: results){
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if(r.getTitle().toLowerCase().contains(text.toLowerCase())){
                temp.add(r);
            }
        }
        //update recyclerview
        adapter.updateList(temp);
        textView_results.setText(getString(R.string.results, Integer.toString(adapter.getItemCount())));
    }
}
