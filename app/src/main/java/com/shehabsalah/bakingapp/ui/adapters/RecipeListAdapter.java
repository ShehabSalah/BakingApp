package com.shehabsalah.bakingapp.ui.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;

import java.util.ArrayList;

/**
 * Created by ShehabSalah on 6/7/17.
 *
 */

public class RecipeListAdapter extends BaseAdapter {
    private ArrayList<RecipeHolder> mDataset;
    private Context context;

    public RecipeListAdapter(ArrayList<RecipeHolder> mDataset, Context context) {
        this.mDataset = mDataset;
        this.context = context;
    }

    @Override
    public int getCount() {
        return mDataset.size() == 0 ? 0 : mDataset.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup) {
        View itemView = view;
        Holder holder;
        LayoutInflater inflater;

        if (itemView == null) {
            holder = new Holder();
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            itemView = inflater.inflate(R.layout.recipe_item, viewGroup, false);
            holder.recipe_name = (TextView) itemView.findViewById(R.id.step_name);
            itemView.setTag(holder);
        } else {
            holder = (Holder) itemView.getTag();
        }

        holder.recipe_name.setText(mDataset.get(position).getName());
        return itemView;
    }

    private class Holder {
        TextView recipe_name;
    }
}
