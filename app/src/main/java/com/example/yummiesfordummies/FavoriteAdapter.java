package com.example.yummiesfordummies;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.ViewHolder> {


    private List<Result> results;
    private Result selectedResult;
    private String resultTitle;
    private DatabaseReference database;
    private SharedPreferences sharedPreferences;

    public FavoriteAdapter(List<Result> results) {
        this.results = results;
    }

    @NonNull
    @Override
    public FavoriteAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultView = inflater.inflate(R.layout.item_result, parent, false);
        FavoriteAdapter.ViewHolder viewHolder = new FavoriteAdapter.ViewHolder(resultView);

        database = FirebaseDatabase.getInstance().getReference("users");

//        IRecipeFragmentActivity recipeFragmentActivity = (IRecipeFragmentActivity) getActivity();
        sharedPreferences = context.getSharedPreferences("Settings", Context.MODE_PRIVATE);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull FavoriteAdapter.ViewHolder holder, int position) {
        Result result = results.get(position);
        Log.d("position", result.getTitle());
        holder.textView_title.setText(result.getTitle());
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public void updateList(List<Result> list){
        results = list;
        notifyDataSetChanged();
    }

    public class ViewHolder  extends RecyclerView.ViewHolder{
        TextView textView_title;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_resultTitle);
        }
    }
}
