package com.example.yummiesfordummies;

import android.os.Bundle;
import android.util.Log;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class ResultListActivity extends AppCompatActivity implements IResultFragmentActivity {

    private String categoryName, label, url;
    private List<Result> categories = new ArrayList<>();
    private static AsyncHttpClient client = new AsyncHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_resultlist);

        categoryName = getIntent().getStringExtra("category");
        label = getIntent().getStringExtra("label");
        if(label.equals("Type")) {
            url = "https://www.themealdb.com/api/json/v1/1/filter.php?c=" + categoryName;
            client.addHeader("Accept", "application/json");
            // send a get request to the api url
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
                            categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strMeal").toString()));
                        }
                        ResultsFragment results = new ResultsFragment();
                        loadFragment(results, R.id.fragContainer_results);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("url", url);
                }
            });

        }
        else {
            url = "https://www.themealdb.com/api/json/v1/1/filter.php?a=" + categoryName;
            client.addHeader("Accept", "application/json");
            // send a get request to the api url
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
                            categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strMeal").toString()));
                        }
                        ResultsFragment results = new ResultsFragment();
                        loadFragment(results, R.id.fragContainer_results);
                        Toast.makeText(getApplicationContext(), "SUCCESS", Toast.LENGTH_LONG).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

//                @Override
//                public void onProgress(long bytesWritten, long totalSize) {
//                   Log.d("loading", "still loading");
//                    Toast.makeText(getApplicationContext(), "still loadinggggg", Toast.LENGTH_LONG).show();
//                }
//
//                @Override
//                public void onFinish() {
//                    super.onFinish();
//                    Log.d("finished", "done");
//                    Toast.makeText(getApplicationContext(), "finished", Toast.LENGTH_LONG).show();
//                    ResultsFragment results = new ResultsFragment();
//                    loadFragment(results, R.id.fragContainer_results);
//                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                    Log.d("url", url);
                }
            });

        }

//        ResultsFragment results = new ResultsFragment();
//        loadFragment(results, R.id.fragContainer_results);
    }

    public void loadFragment(Fragment fragment, int id){
        FragmentManager fragmentManager = getSupportFragmentManager();
        // create a fragment transaction to begin the transaction and replace the fragment
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //replacing the placeholder - fragmentContainerView with the fragment that is passed as parameter
        fragmentTransaction.replace(id, fragment);
        fragmentTransaction.commit();
    }

    @Override
    public List<Result> getCategories() {
//        ArrayList<Result> categories = new ArrayList<>();
//        client.addHeader("Accept", "application/json");
//        // send a get request to the api url
//        client.get(url, new AsyncHttpResponseHandler() {
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    JSONObject json = new JSONObject(new String(responseBody));
//                    for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
//                        categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strMeal").toString()));
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("url", url);
//            }
//        });
        return categories;
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getURL() {
        return url;
    }
}
