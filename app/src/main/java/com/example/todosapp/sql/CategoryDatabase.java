package com.example.todosapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todosapp.dataModel.CategoryNames;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ashrafiqubal on 10/02/18.
 */

public class CategoryDatabase extends SQLiteOpenHelper {
    private static final String TAG = CategoryDatabase.class.getSimpleName();
    private static final int DATABASE_VERSION = 1;

    private static final String DATABASE_NAME = "TO_DOS_APP";                      // Database Name
    private static final String TABLE_ITEMS_LIST = "ITEMS_TABLE";                  // Items table name
    private static final String TABLE_CATEGORY_LIST = "CATEGORY_TABLE";            // Category table name
    private static final String KEY_ID = "ID";                                     // ID of each note
    private static final String KEY_TITLE = "TITLE";                               // Title of each note
    private static final String KEY_DESCRIPTION = "DESCRIPTION";                   // Description of each note
    private static final String KEY_IMAGE_PATH = "IMAGE_PATH";                     // Image Path of each note
    private static final String KEY_STATUS = "STATUS";                             // Status of each note
    private static final String KEY_CATEGORY_ID = "CATEGORY_ID";                   // Id of parent category
    private static final String KEY_CATEGORY = "CATEGORY";                         // CATEGORY of each note
    private static final String KEY_COUNT = "COUNT";                               // COUNT of each note

    private String CREATE_ITEMS_TABLE = "CREATE TABLE " + TABLE_ITEMS_LIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_TITLE + " TEXT," + KEY_DESCRIPTION + " TEXT,"
            + KEY_IMAGE_PATH + " TEXT," + KEY_STATUS + " INTEGER," + KEY_CATEGORY_ID + " INTEGER" + ")";

    private String CREATE_CATEGORY_TABLE = "CREATE TABLE " + TABLE_CATEGORY_LIST + "("
            + KEY_ID + " INTEGER PRIMARY KEY," + KEY_CATEGORY + " TEXT," + KEY_COUNT + " INTEGER" + ")";

    public CategoryDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_CATEGORY_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEMS_LIST);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_CATEGORY_LIST);

        // Create tables again
        onCreate(db);
    }

    // Adding new Category
    public boolean addCategory(String category){
        try{
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_CATEGORY, category);
            values.put(KEY_COUNT, 0);

            // Inserting Row
            db.insert(TABLE_CATEGORY_LIST,null,values);
            db.close(); // Closing database connection
            return true;
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    // Getting All CategoryName
    public ArrayList<CategoryNames> getAllCategoryName(){
        // Select All Query
        ArrayList<CategoryNames> categoryNamesArrayList = new ArrayList<>();
        String selectQuery = "SELECT * FROM " + TABLE_CATEGORY_LIST;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                CategoryNames categoryNames = new CategoryNames();
                categoryNames.setId(cursor.getInt(0));
                categoryNames.setName(cursor.getString(1));
                categoryNames.setCount(cursor.getInt(2));
                categoryNamesArrayList.add(categoryNames);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        Collections.reverse(categoryNamesArrayList);
        return categoryNamesArrayList;
    }

}