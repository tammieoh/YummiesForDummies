package com.example.yummiesfordummies;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class ResultAdapter extends RecyclerView.Adapter<ResultAdapter.ViewHolder> {

    private List<Result> results;
    private Result selectedResult;
    private String resultTitle;

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
        holder.textView_title.setText(result.getTitle());
        Picasso.get().load(result.getResultImage()).into(holder.imageView_imageResult);
    }

    @Override
    public int getItemCount() {
        return results.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView textView_title;
        ImageView imageView_imageResult;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_title = itemView.findViewById(R.id.textView_resultTitle);
            imageView_imageResult = itemView.findViewById(R.id.imageView_result);


        }

        @Override
        public void onClick(View v) {
            int selected = getAdapterPosition();
            Result selectedResult = results.get(selected);
            // communicate with either a viewmodel or pass it as an intent

        }
    }
}
