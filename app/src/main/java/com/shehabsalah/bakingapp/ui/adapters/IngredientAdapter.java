package com.shehabsalah.bakingapp.ui.adapters;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.ingredient.Ingredient;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by ShehabSalah on 6/9/17.
 * 
 */

public class IngredientAdapter extends RecyclerView.Adapter<IngredientAdapter.MyViewHolder>{
    private ArrayList<Ingredient> ingredients;

    public IngredientAdapter(ArrayList<Ingredient> ingredients) {
        this.ingredients = ingredients;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.quantity) TextView quantity;
        @BindView(R.id.measure) TextView measure;
        @BindView(R.id.ingredient) TextView ingredient;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.ingredient_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        holder.quantity.setText(String.valueOf(ingredients.get(position).getQuantity()));
        holder.measure.setText(ingredients.get(position).getMeasure());
        holder.ingredient.setText(ingredients.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        return ingredients.size();
    }
}