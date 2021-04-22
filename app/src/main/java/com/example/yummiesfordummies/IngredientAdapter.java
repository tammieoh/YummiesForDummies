package com.example.yummiesfordummies;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.ViewHolder>{
    private List<Ingredient> ingredients;
    private LayoutInflater mInflater;

    public IngredientAdapter(List<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View ingredientView = inflater.inflate(R.layout.item_ingredients, parent, false);
//        View locationView = mInflater.inflate(R.layout.item_locations, parent, false);
        ViewHolder viewHolder = new ViewHolder(ingredientView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Ingredient ingredient = ingredients.get(position);
        holder.textView_ingredientInfo.setText(ingredient.getInfo());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView textView_ingredientInfo;

        public ViewHolder(View itemView) {
            super(itemView);
            textView_ingredientInfo = itemView.findViewById(R.id.textView_ingredientInfo);
        }
    }
}
