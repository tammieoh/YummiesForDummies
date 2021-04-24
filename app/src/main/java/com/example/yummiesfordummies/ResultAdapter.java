package com.example.yummiesfordummies;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.Locale;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Result> results;
    private Result selectedResult;
    private String resultTitle, label;

    public ResultAdapter(List<Result> results, String label) {
        this.results = results;
        this.label = label;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View resultView = inflater.inflate(R.layout.item_result, parent, false);
        ViewHolder viewHolder = new ViewHolder(resultView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Result result = results.get(position);
        Log.d("position", result.getTitle());
        holder.textView_title.setText(result.getTitle());
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

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_title;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_resultTitle);
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
