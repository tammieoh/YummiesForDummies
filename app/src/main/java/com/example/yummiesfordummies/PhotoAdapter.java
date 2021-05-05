package com.example.yummiesfordummies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class PhotoAdapter extends RecyclerView.Adapter<PhotoAdapter.ViewHolder>{
    private List<String> photos;
    private LayoutInflater mInflater;

    public PhotoAdapter(List<String> photos) {
            this.photos = photos;
            }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            Context context = parent.getContext();
            LayoutInflater inflater = LayoutInflater.from(context);
            View ingredientView = inflater.inflate(R.layout.item_photos, parent, false);
    //        View locationView = mInflater.inflate(R.layout.item_locations, parent, false);
            ViewHolder viewHolder = new ViewHolder(ingredientView);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String photoUrl = photos.get(position);
            Picasso.get().load(photoUrl).into(holder.imageView);
            }

    @Override
    public int getItemCount() {
            return photos.size();
            }

    public void updateList(List<String> list){
        photos = list;
        notifyDataSetChanged();
    }

    // make an onclick listener with the photo, and then create a new activity with the photo enlarged?
    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.textView_ingredientInfo);
        }
    }
}
