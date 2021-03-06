package com.example.yummiesfordummies;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
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
            View photoView = inflater.inflate(R.layout.item_photos, parent, false);
    //        View locationView = mInflater.inflate(R.layout.item_locations, parent, false);
            ViewHolder viewHolder = new ViewHolder(photoView);
            return viewHolder;
            }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            String photoUrl = photos.get(position);
            Uri uri = Uri.fromFile(new File((photoUrl)));
            Bitmap bitmap = BitmapFactory.decodeFile(uri.getPath());
//            InputStream is = null;
//            try {
//                is = new URL(photoUrl).openStream();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            Bitmap bitmap = BitmapFactory.decodeStream(is);

//            bitmap = BitmapFactory.decodeFile(new File(photoUrl).getAbsolutePath());
//                    bitmap = cropAndScale(bitmap, 300); // if you mind scaling
            holder.imageView.setImageBitmap(bitmap);
//            Picasso.get().load(photoUrl).into(holder.imageView);
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
            imageView = itemView.findViewById(R.id.imageView_photo);
        }
    }
}
