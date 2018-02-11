package com.example.todosapp;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.example.todosapp.adapter.CategoryListAdapter;
import com.example.todosapp.dataModel.CategoryNames;
import com.example.todosapp.sql.CategoryDatabase;
import com.example.todosapp.util.AddCategoryDialog;
import com.example.todosapp.util.GeneralUtil;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, AddCategoryDialog.UpdateData {

    final String TAG = MainActivity.class.getSimpleName();
    AppCompatActivity activity;
    RecyclerView categories_list;
    LinearLayoutManager linearLayoutManager;
    ImageView add_button;
    CategoryListAdapter categoryListAdapter;
    CategoryDatabase categoryDatabase;
    int k = 0;
    private ArrayList<CategoryNames> categoryNamesList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializeOnClickListener();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_button:
                dialogToAddCategory();
                break;
        }
    }
    @Override
    protected void onStart() {
        super.onStart();
        updateData();
    }
    private void initializeViews() {
        activity = this;
        categories_list = findViewById(R.id.categories_list);
        linearLayoutManager = new LinearLayoutManager(activity);
        categories_list.setLayoutManager(linearLayoutManager);
        categoryListAdapter = new CategoryListAdapter(activity);
        categories_list.setAdapter(categoryListAdapter);
        add_button = findViewById(R.id.add_button);
        categoryDatabase = new CategoryDatabase(activity);
    }

    private void initializeOnClickListener() {
        add_button.setOnClickListener(this);
    }

    private void updateData() {
        categoryNamesList = categoryDatabase.getAllCategoryName();
        if (categoryNamesList.size() > 0) {
            categoryListAdapter.setCategoryNames(categoryNamesList);
            categoryListAdapter.notifyDataSetChanged();
        }
    }

    public void dialogToAddCategory() {
        AddCategoryDialog addCategoryDialog = new AddCategoryDialog(activity, categoryDatabase, categoryNamesList, this);
        addCategoryDialog.show();
    }

    @Override
    public void onBackPressed() {
        k++;
        if (k == 1) {
            GeneralUtil.showMessage(getString(R.string.text_press_again_to_exit));
            try {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        k = 0;
                    }
                }, getResources().getInteger(R.integer.double_back_press_time_out));
            } catch (Exception e) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        } else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        }

    }
    @Override
    public void updateList() {
        updateData();
    }
}
