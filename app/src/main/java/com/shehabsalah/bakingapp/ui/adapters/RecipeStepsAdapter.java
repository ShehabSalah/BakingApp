package com.shehabsalah.bakingapp.ui.adapters;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.shehabsalah.bakingapp.R;
import com.shehabsalah.bakingapp.data.recipes.RecipeHolder;
import com.shehabsalah.bakingapp.util.Config;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by Shehab Salah on 6/10/17.
 *
 */

public class RecipeStepsAdapter extends SelectableAdapter<RecipeStepsAdapter.MyViewHolder> {
    private RecipeHolder mDataset;
    private Context context;
    final private ListItemClickListener mOnClickListener;

    /**
     * RecipeStepsAdapter Constructor to initialize Recipe Holder, context and item click listener,
     * Called only the recipe steps created for its first time
     * @param mDataset Data set that contain the list
     * @param context application context
     * @param listener Listener to handle on item click listener
     * @param isFirstTime indicate that the recipe steps list created for its first time
     * */
    public RecipeStepsAdapter(RecipeHolder mDataset, Context context,ListItemClickListener listener,boolean isFirstTime) {
        this.mDataset = mDataset;
        this.context = context;
        mOnClickListener = listener;
        // If it's the first time to call the Step adapter, highlight the first item in the list
        if (isFirstTime){
            toggleSelection(0);
        }
    }
    /**
     * RecipeStepsAdapter Constructor to initialize Recipe Holder, context and item click listener.
     * Also the adapter highlight the item at a given position
     * @param mDataset Data set that contain the list
     * @param context application context
     * @param listener Listener to handle on item click listener
     * @param position Step position to be highlighted
     * */
    public RecipeStepsAdapter(RecipeHolder mDataset, Context context,ListItemClickListener listener, int position) {
        this.mDataset = mDataset;
        this.context = context;
        mOnClickListener = listener;
        // Highlight the item at given position
        if (position != -1){
            clearSelection();
            toggleSelection(position);
        }
    }
    public interface ListItemClickListener {
        void onListItemClick(int clickedItemIndex);
    }

    class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        @BindView(R.id.step_name)
        TextView recipe_name;
        @BindView(R.id.steps_list_container)
        RelativeLayout steps_list_container;

        MyViewHolder(View view) {
            super(view);
            ButterKnife.bind(this,view);
            view.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            clearSelection();
            toggleSelection(clickedPosition);
            mOnClickListener.onListItemClick(clickedPosition);

        }
    }

    @Override
    public RecipeStepsAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recipe_details_item, parent, false);
        return new RecipeStepsAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final RecipeStepsAdapter.MyViewHolder holder, final int position) {
        if (position == 0)
            holder.recipe_name.setText(context.getResources().getString(R.string.first_details_item_name));
        else holder.recipe_name.setText(mDataset.getSteps().get(position - 1).getShortDescription());

        if (Config.TwoPane){
            // Highlight the item if it's selected
            holder.steps_list_container.setActivated(isSelected(position));
            // Changing the text Color
            holder.recipe_name.setTextColor(isSelected(position)?
                    ContextCompat.getColor(context, R.color.white_text):ContextCompat.getColor(context, R.color.default_text_color));
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.getSteps().size() + 1;
    }

}