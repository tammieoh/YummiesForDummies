package com.example.yummiesfordummies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cz.msebera.android.httpclient.Header;

public class RecipeFragment extends Fragment {
    private View view;
    private TextView title, category, area, instructions;
    private ImageView imageView_recipe, imageView_favorite;
    private Button button_youtube;
    private List<Ingredient> ingredients = new ArrayList<>();
    private List<String> favorites = new ArrayList<String>();
    private boolean isFavorited = false;
    private IngredientAdapter adapter;
    private RecyclerView recyclerView;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private int count = 1;
    private DatabaseReference database;
    private JSONObject favoritesJson;
    private String currentRecipe;

//    private SharedViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_recipe, container, false);
        recyclerView = view.findViewById(R.id.recyclerView_ingredients);
        title = view.findViewById(R.id.textView_title);
        category = view.findViewById(R.id.textView_category);
        area = view.findViewById(R.id.textView_area);
        instructions = view.findViewById(R.id.textView_instructions);
        imageView_recipe = view.findViewById(R.id.imageView_recipe);
        imageView_favorite = view.findViewById(R.id.imageView_favorite);
        button_youtube = view.findViewById(R.id.button_youtube);

        database = FirebaseDatabase.getInstance().getReference("users");

        IRecipeFragmentActivity recipeFragmentActivity = (IRecipeFragmentActivity) getActivity();
        sharedPreferences = this.getActivity().getSharedPreferences("Settings", Context.MODE_PRIVATE);

        String favorite_file = "file:///android_asset/images/favorite1.png";
        String unfavorite_file = "file:///android_asset/images/unfavorite1.png";

        String url = recipeFragmentActivity.getLink();

        Log.d("url", url);

        client.setEnableRedirects(true, true, true);

        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    JSONObject json = new JSONObject(new String(responseBody));

                    String favorite_file = "file:///android_asset/images/favorite1.png";
                    String unfavorite_file = "file:///android_asset/images/unfavorite1.png";
                    currentRecipe = json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString();

                    if(database.child(sharedPreferences.getString("userID", "")).child("favorites").child(currentRecipe).equals(true)) {
                        Picasso.get().load(favorite_file).into(imageView_favorite);
                    }
                    else {
                        Picasso.get().load(unfavorite_file).into(imageView_favorite);
                    }

                    imageView_favorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                if (favorites.contains(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString())) {
                                    favorites.remove(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString());
                                    Picasso.get().load(unfavorite_file).into(imageView_favorite);
                                    database.child(sharedPreferences.getString("userID", "")).child("favorites")
                                            .child(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString()).removeValue();

                                }
                                else {
                                    favorites.add(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString());
                                    Picasso.get().load(favorite_file).into(imageView_favorite);
                                    database.child(sharedPreferences.getString("userID", "")).child("favorites")
                                            .child(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString()).setValue(true);;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                    title.setText(json.getJSONArray("meals").getJSONObject(0).get("strMeal").toString());
                    category.setText("Category: " + json.getJSONArray("meals").getJSONObject(0).get("strCategory").toString());
                    area.setText("Area: " + json.getJSONArray("meals").getJSONObject(0).get("strArea").toString());
                    instructions.setText(json.getJSONArray("meals").getJSONObject(0).get("strInstructions").toString());
                    Picasso.get().load(json.getJSONArray("meals").getJSONObject(0).get("strMealThumb").toString()).into(imageView_recipe);


                    // recyclerview for ingredients
                    while((!json.getJSONArray("meals").getJSONObject(0).get("strIngredient" + count).toString().equals("")
                    && !json.getJSONArray("meals").getJSONObject(0).get("strMeasure" + count).toString().equals("")) && count < 21) {
                        Ingredient ingredient = new Ingredient(json.getJSONArray("meals").getJSONObject(0).get("strMeasure" + count).toString()
                                + " " + json.getJSONArray("meals").getJSONObject(0).get("strIngredient" + count).toString());
                        ingredients.add(ingredient);
                        count++;
                    }

                    adapter = new IngredientAdapter(ingredients);
                    recyclerView.setAdapter(adapter);
                    System.out.println(adapter.getItemCount());
                    Log.d("ingredient count", Integer.toString(adapter.getItemCount()));
                    recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));



                    // clicking button to open youtube link
                    button_youtube.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            try {
                                Intent resultIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(json.getJSONArray("meals").getJSONObject(0).get("strYoutube").toString()));
                                getContext().startActivity(resultIntent);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Log.d("url", url);
                Toast.makeText(getActivity(), "Error in GET REQUEST", Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }
}
