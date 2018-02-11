package com.example.todosapp.adapter;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.activity.ItemListActivity;
import com.example.todosapp.dataModel.CategoryNames;
import com.example.todosapp.util.Key;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class CategoryListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<CategoryNames> categoryNamesList = new ArrayList<>();
    private AppCompatActivity activity;

    public CategoryListAdapter(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void setCategoryNames(List<CategoryNames> categoryNamesList) {
        this.categoryNamesList = categoryNamesList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderNoteCards(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_category_list, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final CategoryNames categoryNames = categoryNamesList.get(position);
        if (holder instanceof ViewHolderNoteCards) {
            ((ViewHolderNoteCards) holder).category_name.setText(categoryNames.getName());
            if (categoryNames.getCount() <= 1)
                ((ViewHolderNoteCards) holder).item_count.setText(String.valueOf(categoryNames.getCount()) + " item");
            else
                ((ViewHolderNoteCards) holder).item_count.setText(String.valueOf(categoryNames.getCount()) + " items");

            ((ViewHolderNoteCards) holder).itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent itemListIntent = new Intent(activity, ItemListActivity.class);
                    itemListIntent.putExtra(Key.KEY_CategoryName, categoryNames.getName());
                    itemListIntent.putExtra(Key.KEY_CategoryId, categoryNames.getId());
                    activity.startActivity(itemListIntent);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                }
            });

        }
    }

    @Override
    public int getItemCount() {
        if (categoryNamesList != null) {
            return categoryNamesList.size();
        } else
            return 0;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private class ViewHolderNoteCards extends RecyclerView.ViewHolder {
        View itemView;
        TextView category_name, item_count;

        private ViewHolderNoteCards(View itemView) {
            super(itemView);
            this.itemView = itemView;
            category_name = itemView.findViewById(R.id.category_name);
            item_count = itemView.findViewById(R.id.item_count);
        }
    }
}
