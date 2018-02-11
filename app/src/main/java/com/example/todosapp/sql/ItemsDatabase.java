package com.example.todosapp.sql;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.todosapp.dataModel.ItemDetails;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by ashrafiqubal on 11/02/18.
 */

public class ItemsDatabase extends SQLiteOpenHelper {
    private static final String TAG = ItemsDatabase.class.getSimpleName();
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

    public ItemsDatabase(Context context) {
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

    // Adding new Note
    public boolean addNote(String note_title, String note_description, String image_path, int status, int category_id, int count) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, note_title);
            values.put(KEY_DESCRIPTION, note_description);
            values.put(KEY_IMAGE_PATH, image_path);
            values.put(KEY_STATUS, status);
            values.put(KEY_CATEGORY_ID, category_id);
            db.insert(TABLE_ITEMS_LIST, null, values); // Inserting Row

            values.clear();
            values.put(KEY_COUNT, count + 1);
            db.update(TABLE_CATEGORY_LIST, values, KEY_ID + "=" + category_id, null);

            db.close(); // Closing database connection
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Updating old Note
    public boolean updateNote(int id, String note_title, String note_description, String image_path, int status) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_TITLE, note_title);
            values.put(KEY_DESCRIPTION, note_description);
            values.put(KEY_IMAGE_PATH, image_path);
            values.put(KEY_STATUS, status);

            db.update(TABLE_ITEMS_LIST, values, KEY_ID + "=" + id, null);
            db.close(); // Closing database connection
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public boolean changeNoteSatus(int id, int status){
        try {
            SQLiteDatabase db = this.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(KEY_STATUS, status);

            db.update(TABLE_ITEMS_LIST, values, KEY_ID + "=" + id, null);
            db.close(); // Closing database connection
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getting All Notes
    public ArrayList<ItemDetails> getAllNotes(int category_id) {
        // Select All Query
        ArrayList<ItemDetails> notes = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS_LIST + " WHERE " + KEY_CATEGORY_ID + " = " + category_id;
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            Cursor cursor = db.rawQuery(selectQuery, null);

            if (cursor.moveToFirst()) {
                do {
                    ItemDetails note = new ItemDetails();
                    note.setId(cursor.getInt(0));
                    note.setTitle(cursor.getString(1));
                    note.setDescription(cursor.getString(2));
                    note.setImage_path(cursor.getString(3));
                    note.setStatus(cursor.getInt(4));
                    note.setCategory_id(cursor.getInt(5));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
            cursor.close();
            Collections.reverse(notes);
            db.close();
            return notes;
        } catch (SQLException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }

    }

    // Deleting single Note
    public boolean deleteNote(int id, int category_id, int count) {
        try {
            SQLiteDatabase db = this.getWritableDatabase();
            db.delete(TABLE_ITEMS_LIST, KEY_ID + " = ?",
                    new String[]{String.valueOf(id)});

            ContentValues values = new ContentValues();
            values.clear();
            values.put(KEY_COUNT, count - 1);
            db.update(TABLE_CATEGORY_LIST, values, KEY_ID + "=" + category_id, null);

            db.close();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    // Getting Notes Count
    public int getNotesCount(int category_id) {
        String countQuery = "SELECT  * FROM " + TABLE_ITEMS_LIST + " WHERE " + KEY_CATEGORY_ID + " = " + category_id;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int len = cursor.getCount();
        cursor.close();
        db.close();
        return len;
    }

    // Getting Note by ID
    public ItemDetails getNote(int id) {
        ItemDetails note = new ItemDetails();
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "SELECT  * FROM " + TABLE_ITEMS_LIST + " WHERE " + KEY_ID + " = " + id;
        try {
            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                note.setId(cursor.getInt(0));
                note.setTitle(cursor.getString(1));
                note.setDescription(cursor.getString(2));
                note.setImage_path(cursor.getString(3));
                note.setStatus(cursor.getInt(4));
                note.setCategory_id(cursor.getInt(5));
            }
            cursor.close();
            db.close();
            return note;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return note;
    }

}
