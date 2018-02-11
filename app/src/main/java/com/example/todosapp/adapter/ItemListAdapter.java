package com.example.todosapp.adapter;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.activity.AddEditActivity;
import com.example.todosapp.dataModel.ItemDetails;
import com.example.todosapp.sql.ItemsDatabase;
import com.example.todosapp.util.Key;
import com.example.todosapp.util.OnSwipeTouchListener;
import com.example.todosapp.util.RoundedImageView;

import java.util.ArrayList;

/**
 * Created by ashrafiqubal on 11/02/18.
 */

public class ItemListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private AppCompatActivity activity;
    private String categoryName = "";
    private int categoryId = 0;
    private ItemsDatabase itemsDatabase;
    private ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();

    public ItemListAdapter(AppCompatActivity activity, String categoryName, int categoryId, ItemsDatabase itemsDatabase) {
        this.activity = activity;
        this.categoryName = categoryName;
        this.categoryId = categoryId;
        this.itemsDatabase = itemsDatabase;
    }

    public void setItemDetailsArrayList(ArrayList<ItemDetails> itemDetailsArrayList) {
        this.itemDetailsArrayList = itemDetailsArrayList;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolderItems(LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout_items, parent, false));
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {
        final ItemDetails itemDetails = itemDetailsArrayList.get(position);
        if (holder instanceof ViewHolderItems) {
            if (itemDetails.getImage_path().length() > 0)
                ((ViewHolderItems) holder).image_view.setImageURI(Uri.parse(itemDetails.getImage_path()));
            ((ViewHolderItems) holder).item_title.setText(itemDetails.getTitle());
            if (itemDetails.getStatus() == Key.KEY_COMPLETED) {
                ((ViewHolderItems) holder).status_done.setVisibility(View.VISIBLE);
                ((ViewHolderItems) holder).status_pending.setVisibility(View.GONE);
            } else {
                ((ViewHolderItems) holder).status_done.setVisibility(View.GONE);
                ((ViewHolderItems) holder).status_pending.setVisibility(View.VISIBLE);
            }
            ((ViewHolderItems) holder).itemView.setOnTouchListener(new OnSwipeTouchListener(activity) {
                public void onSwipeTop() {
                }

                public void onSwipeRight() {
                    changeNoteSatus(itemDetails.getId(), Key.KEY_PENDING, holder.getAdapterPosition());
                }

                public void onSwipeLeft() {
                    changeNoteSatus(itemDetails.getId(), Key.KEY_COMPLETED, holder.getAdapterPosition());
                }

                public void onSwipeBottom() {
                }

                public void onClick() {
                    Intent addEditActivity = new Intent(activity, AddEditActivity.class);
                    addEditActivity.putExtra(Key.KEY_CategoryName, categoryName);
                    addEditActivity.putExtra(Key.KEY_CategoryId, categoryId);
                    addEditActivity.putExtra(Key.KEY_IsNewNote, false);
                    addEditActivity.putExtra(Key.KEY_NoteId, itemDetails.getId());
                    activity.startActivity(addEditActivity);
                    activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        if (itemDetailsArrayList == null)
            return 0;
        else
            return itemDetailsArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    private void changeNoteSatus(int id, int status, int position) {
        if (itemsDatabase.changeNoteSatus(id, status)) {
            itemDetailsArrayList.get(position).setStatus(status);
            notifyDataSetChanged();
        }
    }

    private class ViewHolderItems extends RecyclerView.ViewHolder {
        View itemView;
        RoundedImageView image_view;
        TextView item_title, status_done, status_pending;

        ViewHolderItems(View itemView) {
            super(itemView);
            this.itemView = itemView;
            image_view = itemView.findViewById(R.id.image_view);
            image_view.setImageResource(R.drawable.circular_background);
            item_title = itemView.findViewById(R.id.item_title);
            status_done = itemView.findViewById(R.id.status_done);
            status_pending = itemView.findViewById(R.id.status_pending);
        }
    }
}
