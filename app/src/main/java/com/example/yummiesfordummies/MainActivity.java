package com.example.yummiesfordummies;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;

import java.util.ArrayList;
import java.util.List;

import com.google.firebase.database.DatabaseReference;


public class MainActivity extends AppCompatActivity implements IRecipeFragmentActivity, IResultFragmentActivity{

    private String random_url = "http://www.themealdb.com/api/json/v1/1/random.php";

    private TabLayout tabLayout;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // database initialize;
        FirebaseApp.initializeApp(getApplicationContext());
        database = FirebaseDatabase.getInstance().getReference("users");

        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d("tab", Integer.toString(tab.getPosition()));
                if(tab.getPosition() == 0) {
                    RecipeFragment recipeFragment = new RecipeFragment();
                    loadFragment(recipeFragment,  R.id.fragContainer_home);
                }
                else if(tab.getPosition() == 1) {
                    ResultsFragment mainResults = new ResultsFragment();
                    loadFragment(mainResults, R.id.fragContainer_home);
                }
                else {
                    ProfileFragment profileFragment = new ProfileFragment();
                    loadFragment(profileFragment, R.id.fragContainer_home);
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                if(tab.getPosition() == 0) {
                    getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragContainer_home, new RecipeFragment())
                            .commit();
//                    RecipeFragment recipeFragment = new RecipeFragment();
//                    loadFragment(recipeFragment,  R.id.fragContainer_home);
                }
                else if(tab.getPosition() == 1) {
                    ResultsFragment mainResults = new ResultsFragment();
                    loadFragment(mainResults, R.id.fragContainer_home);
                }
                else {
                    ProfileFragment profileFragment = new ProfileFragment();
                    loadFragment(profileFragment, R.id.fragContainer_home);
                }
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

//    @Override
//    public void onResume(){
//        super.onResume();
//        RecipeFragment recipeFragment = new RecipeFragment();
//        loadFragment(recipeFragment,  R.id.fragContainer_home);
//    }

    @Override
    public String getLink() {
        return random_url;
    }

    @Override
    public List<Result> getCategories() {
        ArrayList<Result> categories = new ArrayList<>();
        categories.add(new Result("Food Type"));
        categories.add(new Result("Meal Type"));
        categories.add(new Result("Area/Location"));
        return categories;
    }

    @Override
    public String getLabel() {
        return "";
    }

    @Override
    public String getURL() {
        return "";
    }
}