package com.example.todosapp.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SwitchCompat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.todosapp.R;
import com.example.todosapp.dataModel.ItemDetails;
import com.example.todosapp.sql.ItemsDatabase;
import com.example.todosapp.util.GeneralUtil;
import com.example.todosapp.util.Key;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Date;
import java.util.Random;

/**
 * Created by ashrafiqubal on 11/02/18.
 */

public class AddEditActivity extends AppCompatActivity implements View.OnClickListener {

    final String TAG = AddEditActivity.class.getSimpleName();
    AppCompatActivity activity;
    TextView title;
    Button save_button, button_delete, add_image_button;
    ImageView selected_image;
    EditText add_title, add_description;
    SwitchCompat add_status;
    ItemsDatabase itemsDatabase;
    String categoryName = "";
    int categoryId = 0;
    ImageButton clear_button;
    boolean isNewNote = true;
    int itemId = 0;
    String image_uri = "", thumbnail_uri = "";
    ItemDetails itemDetails = new ItemDetails();

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_status:
                break;
            case R.id.save_button:
                if (validateForm()) {
                    if (isNewNote)
                        addNote();
                    else
                        updateNote();
                }else {
                    GeneralUtil.showMessage(getString(R.string.text_enter_note_title));
                }
                break;
            case R.id.button_delete:
                if(itemsDatabase.deleteNote(itemId, categoryId, itemsDatabase.getNotesCount(categoryId))){
                    GeneralUtil.showMessage(getString(R.string.text_note_deleted_successfully));
                    finish();
                }else {
                    GeneralUtil.showMessage(getString(R.string.unable_to_delete_noe));
                }
                break;
            case R.id.add_image_button:
                if (GeneralUtil.checkStorageReadPermission(activity)) {
                    GeneralUtil.openGallary(activity, Key.KEY_REQUEST_GALLARY);
                } else {
                    GeneralUtil.requestReadStoragePermission(activity, Key.KEY_READ_STOARGE_PERMISSION);
                }
                break;
            case R.id.clear_button:
                finish();
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_edit);
        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        categoryName = bundle.getString(Key.KEY_CategoryName);
        categoryId = bundle.getInt(Key.KEY_CategoryId);
        isNewNote = bundle.getBoolean(Key.KEY_IsNewNote);
        itemId = bundle.getInt(Key.KEY_NoteId);
        initializeViews();
        initializeOnClickListener();
//        updateData();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == Key.KEY_REQUEST_GALLARY && resultCode == RESULT_OK && data != null && data.getData() != null) {
            try {
                Uri uri = data.getData();
                try{
                    Cursor cursor = MediaStore.Images.Thumbnails.queryMiniThumbnails(
                            getContentResolver(), uri,
                            MediaStore.Images.Thumbnails.MINI_KIND,
                            null );
                    if( cursor != null && cursor.getCount() > 0 ) {
                        cursor.moveToFirst();//**EDIT**
                        image_uri = cursor.getString( cursor.getColumnIndex( MediaStore.Images.Thumbnails.DATA ) );
                        cursor.close();
                    }
                }catch (Exception e){
                    image_uri = uri.toString();
                }
                selected_image.setImageURI(uri);
                saveBitmap(getThumbNail(((BitmapDrawable)selected_image.getDrawable()).getBitmap()));
            } catch (Exception e) {
                    e.printStackTrace();
            }
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String permissions[], @NonNull int[] grantResults) {
        switch (requestCode) {
            case Key.KEY_READ_STOARGE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    GeneralUtil.openGallary(activity, Key.KEY_REQUEST_GALLARY);
                }else {
                    GeneralUtil.showMessage(getString(R.string.text_in_order_to_continue_we_need_this_permission));
                }
                break;
        }
    }
    @SuppressLint("SetTextI18n")
    private void initializeViews() {
        activity = this;
        itemsDatabase = new ItemsDatabase(activity);
        title = findViewById(R.id.title);
        save_button = findViewById(R.id.save_button);
        button_delete = findViewById(R.id.button_delete);
        add_image_button = findViewById(R.id.add_image_button);
        selected_image = findViewById(R.id.selected_image);
        add_title = findViewById(R.id.add_title);
        add_description = findViewById(R.id.add_description);
        add_status = findViewById(R.id.add_status);
        clear_button = findViewById(R.id.clear_button);
        if (isNewNote) {
            title.setText("Add item");
            button_delete.setVisibility(View.GONE);
        } else {
            title.setText("Edit item");
            itemDetails = itemsDatabase.getNote(itemId);
            add_title.setText(itemDetails.getTitle());
            add_description.setText(itemDetails.getDescription());
            image_uri = itemDetails.getImage_path();
            thumbnail_uri = itemDetails.getThumbnail_uri();
            if(image_uri.length() >0 ){
                selected_image.setImageURI(Uri.parse(image_uri));
            }
            if (itemDetails.getStatus() == Key.KEY_COMPLETED)
                add_status.setChecked(true);
            else
                add_status.setChecked(false);

        }
    }

    private void initializeOnClickListener() {
        save_button.setOnClickListener(this);
        button_delete.setOnClickListener(this);
        add_image_button.setOnClickListener(this);
        add_status.setOnClickListener(this);
        clear_button.setOnClickListener(this);
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    private boolean validateForm() {
        return !add_title.getText().toString().trim().equals("");
    }

    private void addNote() {
        if (itemsDatabase.addNote(add_title.getText().toString().trim(),
                add_description.getText().toString().trim(),
                image_uri,
                thumbnail_uri,
                add_status.isChecked() ? 1 : 0,
                categoryId, itemsDatabase.getNotesCount(categoryId))) {
            GeneralUtil.showMessage(getString(R.string.text_note_added_successfully));
            finish();
        } else {
            GeneralUtil.showMessage(getString(R.string.text_unable_to_add_note));
        }
    }
    private void updateNote() {
        if (itemsDatabase.updateNote(itemId, add_title.getText().toString().trim(),
                add_description.getText().toString().trim(),
                image_uri,
                thumbnail_uri,
                add_status.isChecked() ? 1 : 0) ) {
            GeneralUtil.showMessage(getString(R.string.text_note_updated_successfully));
            finish();
        } else {
            GeneralUtil.showMessage(getString(R.string.text_unable_to_update_note));
        }
    }
    private Bitmap getThumbNail(Bitmap bitmap) {
        try{
            return ThumbnailUtils.extractThumbnail(bitmap, 120, 120);
        }catch (Exception e){
            return null;
        }
    }

    private void saveBitmap(Bitmap bitmap){
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/.savenote/.thumbnail");
        if(!myDir.exists()){
            myDir.mkdirs();
        }
        long n = new Date().getTime();
        String fname = "Image-" + n + ".jpg";
        File file = new File(myDir, fname);
        thumbnail_uri = file.getAbsoluteFile().toString();
        Log.i(TAG, "" + file);
        if (file.exists())
            file.delete();
        try {
            FileOutputStream out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 60, out);
            out.flush();
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
