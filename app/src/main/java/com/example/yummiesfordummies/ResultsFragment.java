package com.example.yummiesfordummies;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ResultsFragment extends Fragment {
    private View view;
    private List<Result> results = new ArrayList<>();
    private ResultAdapter adapter;
    private RecyclerView recyclerView;
    private SearchView searchView;
    private TextView numResults;
    private String label, url;
    private AsyncHttpClient client = new AsyncHttpClient();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_results, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_results);
        searchView = view.findViewById(R.id.searchView_results);
        numResults = view.findViewById(R.id.textView_numResults);


        IResultFragmentActivity resultFragmentActivity = (IResultFragmentActivity) getActivity();

        results = resultFragmentActivity.getCategories();
        label = resultFragmentActivity.getLabel();
        url = resultFragmentActivity.getURL();
        // get all the results from the api call (which we need from search fragment and the database
//        for(int i = 0; i < results.size(); i++) {
//            Result result = new Result()
//            beers.add(beer);
//        }

//        if(url.equals("")) {
//
//        }
//        else {
//            client.addHeader("Accept", "application/json");
//            // send a get request to the api url
//            client.get(url, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JSONObject json = new JSONObject(new String(responseBody));
//                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
//                            results.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strArea").toString()));
//                        }
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                }
//                @Override
//                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                    Log.d("url", url);
//                }
//            });
//        }

        adapter = new ResultAdapter(results, label);
        recyclerView.setAdapter(adapter);
        System.out.println(adapter.getItemCount());
        Log.d("list_count", Integer.toString(adapter.getItemCount()));
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        numResults.setText(getString(R.string.results, Integer.toString(adapter.getItemCount())));


        // this is a filter feature for searchView, i don't think this will be necessary
        searchView.setOnQueryTextListener(new androidx.appcompat.widget.SearchView.OnQueryTextListener() {
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
//
        return view;
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
        numResults.setText(getString(R.string.results, Integer.toString(adapter.getItemCount())));
    }
}
