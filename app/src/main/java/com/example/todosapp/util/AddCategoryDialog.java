package com.example.todosapp.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.dataModel.CategoryNames;
import com.example.todosapp.sql.CategoryDatabase;

import java.util.ArrayList;

/**
 * Created by ashrafiqubal on 11/02/18.
 */

public class AddCategoryDialog extends Dialog implements View.OnClickListener{
    final String TAG = AddCategoryDialog.class.getSimpleName();
    private Activity activity;
    private Dialog dialog;
    private EditText category_name;
    private TextView warning_text;
    private Button positive_button, negative_button;
    private CategoryDatabase categoryDatabase;
    private ArrayList<CategoryNames> categoryNamesList = new ArrayList<>();
    private UpdateData updateData = null;

    public AddCategoryDialog(Activity activity, CategoryDatabase categoryDatabase,
                             ArrayList<CategoryNames> categoryNamesList, UpdateData updateData) {
        super(activity);
        this.activity = activity;
        this.categoryDatabase = categoryDatabase;
        this.categoryNamesList = categoryNamesList;
        this.updateData = updateData;
        dialog = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_layout_add_category);
        initializeView();
        setListener();
    }

    private void initializeView() {
        category_name = findViewById(R.id.category_name);
        warning_text = findViewById(R.id.warning_text);
        positive_button = findViewById(R.id.positive_button);
        negative_button = findViewById(R.id.negative_button);

    }
    private void setListener() {
        positive_button.setOnClickListener(this);
        negative_button.setOnClickListener(this);
        category_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if(validateCategory(editable)){
                    warning_text.setVisibility(View.GONE);
                }
            }
        });
    }
    @Override
    public void dismiss() {
        super.dismiss();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.positive_button:
                if(validateCategory()){
                    if(categoryDatabase.addCategory(category_name.getText().toString().trim())){
                        GeneralUtil.showMessage("Category added successfully");
                        if(updateData != null){
                            updateData.updateList();
                        }
                    }
                    dismiss();
                }

                break;
            case R.id.negative_button:
                dismiss();
                break;
        }
    }
    @SuppressLint("SetTextI18n")
    private boolean validateCategory(){
        if(category_name.getText().toString().trim().equals("")){
            warning_text.setVisibility(View.VISIBLE);
            warning_text.setText("Enter category name.");
            return false;
        }else {
            for (int i=0; i<categoryNamesList.size(); i++){
                if(category_name.getText().toString().trim().equals(categoryNamesList.get(i).getName())){
                    warning_text.setVisibility(View.VISIBLE);
                    warning_text.setText(activity.getString(R.string.category_already_exists));
                    return false;
                }
            }
        }
        return true;
    }

    @SuppressLint("SetTextI18n")
    private boolean validateCategory(Editable editable){
        if(editable.toString().trim().equals("")){
            warning_text.setVisibility(View.VISIBLE);
            warning_text.setText("Enter category name.");
            return false;
        }else {
            for (int i=0; i<categoryNamesList.size(); i++){
                if(editable.toString().trim().equals(categoryNamesList.get(i).getName())){
                    warning_text.setVisibility(View.VISIBLE);
                    warning_text.setText(activity.getString(R.string.category_already_exists));
                    return false;
                }
            }
        }
        return true;
    }

    public interface UpdateData{
        void updateList();
    }
}
