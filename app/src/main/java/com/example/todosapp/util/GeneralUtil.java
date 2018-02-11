package com.example.todosapp.util;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.example.todosapp.ApplicationClass;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class GeneralUtil {
    public static void showMessage(String message) {
        Toast.makeText(ApplicationClass.getParentContext(), message, Toast.LENGTH_SHORT).show();
    }
    public static boolean checkStorageReadPermission(Activity activity){
        return ContextCompat.checkSelfPermission(activity,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED;
    }
    public static void requestReadStoragePermission(Activity activity, int REQUEST_CODE){
        ActivityCompat.requestPermissions(activity,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                REQUEST_CODE);
    }
    public static void openGallary(Activity activity, int REQUEST_CODE) {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_PICK);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Image"), REQUEST_CODE);
    }
}
