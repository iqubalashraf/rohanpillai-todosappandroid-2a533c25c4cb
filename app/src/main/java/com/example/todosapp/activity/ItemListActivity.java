package com.example.todosapp.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.adapter.ItemListAdapter;
import com.example.todosapp.dataModel.ItemDetails;
import com.example.todosapp.sql.ItemsDatabase;
import com.example.todosapp.util.Key;

import java.util.ArrayList;

/**
 * Created by ashrafiqubal on 11/02/18.
 */

public class ItemListActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = ItemListActivity.class.getSimpleName();
    AppCompatActivity activity;
    RecyclerView item_list;
    LinearLayoutManager linearLayoutManager;
    ImageView add_button;
    TextView title;
    String categoryName = "";
    int categoryId = 0;
    ItemListAdapter itemListAdapter;
    ItemsDatabase itemsDatabase;
    ArrayList<ItemDetails> itemDetailsArrayList = new ArrayList<>();
    ImageButton back_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_list);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        categoryName = bundle.getString(Key.KEY_CategoryName);
        categoryId = bundle.getInt(Key.KEY_CategoryId);
        initializeViews();
        initializeOnClickListener();
//        updateData();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                Intent addEditActivity = new Intent(activity, AddEditActivity.class);
                addEditActivity.putExtra(Key.KEY_CategoryName, categoryName);
                addEditActivity.putExtra(Key.KEY_CategoryId, categoryId);
                addEditActivity.putExtra(Key.KEY_IsNewNote, true);
                addEditActivity.putExtra(Key.KEY_NoteId, 0);
                startActivity(addEditActivity);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case R.id.back_button:
                onBackPressed();
                break;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        updateData();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private void initializeViews() {
        activity = this;
        itemsDatabase = new ItemsDatabase(activity);
        item_list = findViewById(R.id.item_list);
        linearLayoutManager = new LinearLayoutManager(activity);
        item_list.setLayoutManager(linearLayoutManager);
        itemListAdapter = new ItemListAdapter(activity, categoryName, categoryId, itemsDatabase);
        item_list.setAdapter(itemListAdapter);
        add_button = findViewById(R.id.add_button);
        title = findViewById(R.id.title);
        back_button = findViewById(R.id.back_button);
        title.setText(categoryName);
    }
    private void initializeOnClickListener() {
        add_button.setOnClickListener(this);
        back_button.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }
    private void updateData() {
        itemDetailsArrayList = itemsDatabase.getAllNotes(categoryId);
        itemListAdapter.setItemDetailsArrayList(itemDetailsArrayList);
        itemListAdapter.notifyDataSetChanged();
    }
}
