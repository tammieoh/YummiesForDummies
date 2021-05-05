package com.example.yummiesfordummies;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Result> results, favorites;
    private Result selectedResult;
    private String resultTitle, label;
    private DatabaseReference database;
    private SharedPreferences sharedPreferences;


    public ResultAdapter(List<Result> results, String label) {
        this.results = results;
        this.label = label;
        this.favorites = new ArrayList<>();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultView = inflater.inflate(R.layout.item_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(resultView);

        database = FirebaseDatabase.getInstance().getReference("users");

//        IRecipeFragmentActivity recipeFragmentActivity = (IRecipeFragmentActivity) getActivity();
        sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        Log.d("position", result.getTitle());
        holder.textView_title.setText(result.getTitle());

//        String favorite_file = "file:///android_asset/images/favorite1.png";
//        String unfavorite_file = "file:///android_asset/images/unfavorite1.png";
//            if (favorites.contains(result)) {
//                Picasso.get().load(favorite_file).into(holder.imageView_favorite);
//                database.child(sharedPreferences.getString("userID", "")).child("favorites")
//                        .child(result.getTitle()).setValue(true);
//            }
//            else {
//                Picasso.get().load(unfavorite_file).into(holder.imageView_favorite);
//                database.child(sharedPreferences.getString("userID", "")).child("favorites")
//                        .child(result.getTitle()).setValue(false);
//            }

        holder.itemView.setOnClickListener(v -> {
            if(v.getContext() instanceof MainActivity) {
                Intent intent = new Intent(v.getContext(), CategoryListActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", result.getTitle());
                intent.putExtra("label", label);
                v.getContext().startActivity(intent);
            }
            else if(v.getContext() instanceof CategoryListActivity) {
                Intent intent = new Intent(v.getContext(), ResultListActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", result.getTitle());
                intent.putExtra("label", label);
                v.getContext().startActivity(intent);
            }
            else if(v.getContext() instanceof FavoritesActivity) {
                Log.d("clicked", "do nothing");
            }
            else {
                Intent intent = new Intent(v.getContext(), SelectedRecipeActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", result.getTitle());
                intent.putExtra("label", label);
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<Result> list){
        results = list;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_title;
        ImageView imageView_favorite;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_resultTitle);
//            imageView_favorite = itemView.findViewById(R.id.imageView_favorite);


//            imageView_favorite.setOnClickListener(new View.OnClickListener(){
//                @Override
//                public void onClick(View v) {
//                    int selected = getAdapterPosition();
//                    Result selectedResult = results.get(selected);
////                            isFavorited = true;
//                    if (favorites.contains(selectedResult)) {
//                        favorites.remove(selectedResult);
//                    } else {
//                        favorites.add(selectedResult);
//                    }
//                    notifyDataSetChanged();
//                }
//            });
        }

        @Override
        public void onClick(View v) {
            int selected = getAdapterPosition();
            Result selectedResult = results.get(selected);
            if(itemView.getContext() instanceof MainActivity) {
                Intent intent = new Intent(itemView.getContext(), CategoryListActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", selectedResult.getTitle());
                intent.putExtra("label", label);
                itemView.getContext().startActivity(intent);
            }
            else if(itemView.getContext() instanceof CategoryListActivity) {
                Intent intent = new Intent(itemView.getContext(), ResultListActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", selectedResult.getTitle());
                intent.putExtra("label", label);
                itemView.getContext().startActivity(intent);
            }
            else {
                Intent intent = new Intent(itemView.getContext(), SelectedRecipeActivity.class);
                System.out.println(selectedResult);
                intent.putExtra("category", selectedResult.getTitle());
                intent.putExtra("label", label);
                itemView.getContext().startActivity(intent);
            }
            // communicate with either a viewmodel or pass it as an intent

        }
    }
}
