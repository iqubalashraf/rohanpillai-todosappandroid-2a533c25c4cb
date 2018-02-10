package com.example.todosapp.util;

import android.widget.Toast;

import com.example.todosapp.ApplicationClass;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class GeneralUtil {
    public static void showMessage(String message) {
        Toast.makeText(ApplicationClass.getParentContext(), message, Toast.LENGTH_SHORT).show();
    }
}
