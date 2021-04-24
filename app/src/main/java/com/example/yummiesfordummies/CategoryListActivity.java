package com.example.yummiesfordummies;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class CategoryListActivity extends AppCompatActivity implements IResultFragmentActivity {

    private String categoryName, url, label;
    private List<Result> categories = new ArrayList<>();
    private boolean showProgress = true;

    private static AsyncHttpClient client = new AsyncHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categorylist);

        // might have to pass it as an intent
        categoryName = getIntent().getStringExtra("category");
        label = getIntent().getStringExtra("label");

        if(categoryName.equals("Area/Location")) {
            url = "https://www.themealdb.com/api/json/v1/1/list.php?a=list";
            label = "Area/Location";
                        // set the header because of the api endpoint
            client.addHeader("Accept", "application/json");
            // send a get request to the api url
            client.get(url, new AsyncHttpResponseHandler() {

                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
                            categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strArea").toString()));
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
            url = "https://www.themealdb.com/api/json/v1/1/list.php?c=list";
            label = "Type";
            if(categoryName.equals("Food Type")) {
                // set the header because of the api endpoint
                client.addHeader("Accept", "application/json");
                // send a get request to the api url
                client.get(url, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            JSONObject json = new JSONObject(new String(responseBody));
                            for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
                                if(i != 1 && i != 3 && i != 6 && i != 10 && i != 11) {
                                    categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strCategory").toString()));
                                }
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
            // set the header because of the api endpoint
            client.addHeader("Accept", "application/json");
            // send a get request to the api url
            client.get(url, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        JSONObject json = new JSONObject(new String(responseBody));
                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
                            if(i == 1 || i == 3 || i == 6 || i == 10 || i == 11) {
                                categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strCategory").toString()));
                            }
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
        }

        ResultsFragment results = new ResultsFragment();
        loadFragment(results, R.id.fragContainer_results);

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
//        if(categoryName.equals("Food Type")) {
//            // set the header because of the api endpoint
//            client.addHeader("Accept", "application/json");
//            // send a get request to the api url
//            client.get(url, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JSONObject json = new JSONObject(new String(responseBody));
//                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
//                            if(i != 1 || i != 3 || i != 6 || i != 10 || i != 11) {
//                                categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strCategory").toString()));
//                            }
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
//        else if(categoryName.equals("Meal Type")) {
//            // set the header because of the api endpoint
//            client.addHeader("Accept", "application/json");
//            // send a get request to the api url
//            client.get(url, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JSONObject json = new JSONObject(new String(responseBody));
//                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
//                            if(i == 1 || i == 3 || i == 6 || i == 10 || i == 11) {
//                                categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strCategory").toString()));
//                            }
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
//        else {
//            // set the header because of the api endpoint
//            client.addHeader("Accept", "application/json");
//            // send a get request to the api url
//            client.get(url, new AsyncHttpResponseHandler() {
//                @Override
//                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                    try {
//                        JSONObject json = new JSONObject(new String(responseBody));
//                        for(int i = 0; i < json.getJSONArray("meals").length(); i++) {
//                            categories.add(new Result(json.getJSONArray("meals").getJSONObject(i).get("strArea").toString()));
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
        return categories;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public String getURL() {
        return url;
    }
}
