package com.example.yummiesfordummies;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements IRecipeFragmentActivity{

    private String base_url = "https://api.edamam.com/search?app_id=748f1c8c&app_key=d5e91a2728e63b8c0fa4dc6ff3e89e2&q=chicken";

    //    private String location_url = private String url = "https://rickandmortyapi.com/api/location";
//    ArrayList<String> location_names = new ArrayList<>(), location_types = new ArrayList<>(), location_dimensions = new ArrayList<>();
//    private ArrayList<Location> locations = new ArrayList<>();
    private TabLayout tabLayout;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tab", Integer.toString(tab.getPosition()));
                if(tab.getPosition() == 0) {
                    RecipeFragment recipeFragment = new RecipeFragment();
                    loadFragment(recipeFragment,  R.id.fragContainer_recipe);
//                    HomeFragment homeFragment = new HomeFragment();
//                    loadFragment(homeFragment);
                }
//                else if(tab.getPosition() == 1) {
//                    // set the header because of the api endpoint
//                    client.addHeader("Accept", "application/json");
//                    // send a get request to the api url
//                    client.get(character_url, new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            try {
//                                JSONObject json = new JSONObject(new String(responseBody));
//                                String count = json.getJSONObject("info").getString("count");
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                Log.d("count", count);
//                                editor.putInt("character_count", Integer.parseInt(count));
//                                editor.apply();
//                                CharacterFragment characterFragment = new CharacterFragment();
//                                loadFragment(characterFragment);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Log.d("url", base_url);
//                        }
//                    });
//                }
//                else if(tab.getPosition() == 2) {
//                    LocationFragment locationFragment = new LocationFragment();
//                    loadFragment(locationFragment);
////                    });
//                }
//                else {
//                    // set the header because of the api endpoint
//                    client.addHeader("Accept", "application/json");
//                    // send a get request to the api url
//                    client.get(episode_url, new AsyncHttpResponseHandler() {
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            try {
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                JSONObject json = new JSONObject(new String(responseBody));
//                                String count = json.getJSONObject("info").getString("count");
//                                editor.putInt("episode_count", Integer.parseInt(count));
//                                editor.apply();
//                                EpisodeFragment episodeFragment = new EpisodeFragment();
//                                loadFragment(episodeFragment);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Log.d("url", base_url);
//                        }
//                    });
//                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
//                if(tab.getPosition() == 0) {
//                    // set the header because of the api endpoint
//                    client.addHeader("Accept", "application/json");
//                    // send a get request to the api url
//                    client.get(character_url, new AsyncHttpResponseHandler() {
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            try {
//                                JSONObject json = new JSONObject(new String(responseBody));
//                                String count = json.getJSONObject("info").getString("count");
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                Log.d("count", count);
//                                editor.putInt("character_count", Integer.parseInt(count));
//                                editor.apply();
//                                CharacterFragment characterFragment = new CharacterFragment();
//                                loadFragment(characterFragment);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Log.d("url", base_url);
//                        }
//                    });
//                }
//                else if(tab.getPosition() == 1) {
//                    LocationFragment locationFragment = new LocationFragment();
//                    loadFragment(locationFragment);
//                }
//                else {
//                    // set the header because of the api endpoint
//                    client.addHeader("Accept", "application/json");
//                    // send a get request to the api url
//                    client.get(episode_url, new AsyncHttpResponseHandler() {
//
//                        @Override
//                        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                            try {
//                                SharedPreferences.Editor editor = sharedPreferences.edit();
//                                JSONObject json = new JSONObject(new String(responseBody));
//                                String count = json.getJSONObject("info").getString("count");
//                                editor.putInt("episode_count", Integer.parseInt(count));
//                                editor.apply();
//                                EpisodeFragment episodeFragment = new EpisodeFragment();
//                                loadFragment(episodeFragment);
//                            } catch (JSONException e) {
//                                e.printStackTrace();
//                            }
//
//                        }
//
//                        @Override
//                        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                            Log.d("url", base_url);
//                        }
//                    });
//                }
            }
        });
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
    public String getLink() {

        return getString(R.string.random_recipe);
    }
}