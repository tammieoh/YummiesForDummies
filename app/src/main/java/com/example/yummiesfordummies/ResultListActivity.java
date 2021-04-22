package com.example.yummiesfordummies;

import android.os.Bundle;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

public class ResultListActivity extends AppCompatActivity {

    private RecyclerView recyclerView_results;
    private SearchView searchView;
    private TextView textView_results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultlist);

        recyclerView_results = findViewById(R.id.recyclerView_results);
        searchView = findViewById(R.id.searchView_results);
        textView_results = findViewById(R.id.textView_numResults);

    }
}
