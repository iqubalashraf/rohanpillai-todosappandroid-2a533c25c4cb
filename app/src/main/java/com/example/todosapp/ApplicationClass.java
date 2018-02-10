package com.example.todosapp;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class ApplicationClass extends Application {
    public static final String TAG = ApplicationClass.class.getSimpleName();
    @SuppressLint("StaticFieldLeak")
    private static Context context;

    @SuppressLint("StaticFieldLeak")
    private static ApplicationClass mInstance;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
    }

    public static Context getParentContext(){
        return context;
    }

    public static synchronized ApplicationClass getInstance() {
        if (mInstance == null) {
            mInstance = new ApplicationClass();
        }
        return mInstance;
    }
}
