package com.example.yummiesfordummies;

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
    private ImageView imageView_recipe;
    private Button button_youtube;
    private List<Ingredient> ingredients = new ArrayList<>();
    private IngredientAdapter adapter;
    private RecyclerView recyclerView;
    private static AsyncHttpClient client = new AsyncHttpClient();
    private SharedPreferences sharedPreferences;
    private int count = 1;

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
        button_youtube = view.findViewById(R.id.button_youtube);

        IRecipeFragmentActivity recipeFragmentActivity = (IRecipeFragmentActivity) getActivity();

        String url = recipeFragmentActivity.getLink();
        Log.d("url", url);
        client.get(url, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {

                    JSONObject json = new JSONObject(new String(responseBody));
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


//        viewModel = new ViewModelProvider(requireActivity()).get(SharedViewModel.class);

//        sharedPreferences = getContext().getSharedPreferences("Settings", Context.MODE_PRIVATE);


//        // set the header because of the api endpoint
//        client.addHeader("Accept", "application/json");
//        // send a get request to the api url
//        client.get(url, new AsyncHttpResponseHandler() {
//
//            //            @SuppressLint("SetTextI18n")
//            @Override
//            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
//                try {
//                    JSONObject json = new JSONObject(new String(responseBody));
//                    name.setText(json.get("name").toString());
////                    String file = "file:///android_asset/images/RickandMorty_Logo.png";
//                    Picasso.get().load(json.get("image").toString()).into(imageView);
//
//                    status.setText(getString(R.string.status_textView, json.get("status").toString()));
//                    species.setText(getString(R.string.species_textView, json.get("species").toString()));
//                    gender.setText(getString(R.string.gender_textView, json.get("gender").toString()));
//                    origin.setText(getString(R.string.origin_textView, json.getJSONObject("origin").get("name").toString()));
//                    location.setText(getString(R.string.location_textView, json.getJSONObject("location").get("name").toString()));
//
////                    ArrayList<String> episodes = new ArrayList<>();
//                    String link = "";
//                    String episode_String = getResources().getString(R.string.episodes_textView) + " ";
//                    // 42
//                    for(int i = 0; i < json.getJSONArray(("episode")).length(); i++) {
//                        link = json.getJSONArray("episode").get(i).toString();
//                        if(i == json.getJSONArray(("episode")).length() - 1) {
//                            episode_String += link.substring(40);
//                        }
//                        else {
//                            episode_String += link.substring(40) + ", ";
//                        }
//
//                    }
//                    episodes.setText(episode_String);
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//
//            @Override
//            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
//                Log.d("url", url);
//                Toast.makeText(getActivity(), "Error in GET REQUEST", Toast.LENGTH_SHORT).show();
//            }
//
//        });
        return view;
    }
}
