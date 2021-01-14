package com.github.phuctranba.core.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import com.github.phuctranba.core.item.ItemRecipe;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "recipeapp.db";
    public static final String TABLE_FAVOURITE_NAME = "favourite";
    public static final String TABLE_SAVE_NAME = "recipe_save";


    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "title";
    public static final String KEY_IMAGE = "image";
    public static final String KEY_TIME = "time";
    public static final String KEY_CAT = "cat";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_FAVOURITE_TABLE = "CREATE TABLE " + TABLE_FAVOURITE_NAME + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_TIME + " INTEGER,"
                + KEY_CAT + " TEXT"
                + ")";
        db.execSQL(CREATE_FAVOURITE_TABLE);

        String CREATE_SAVE_TABLE = "CREATE TABLE " + TABLE_SAVE_NAME + "("
                + KEY_ID + " INTEGER,"
                + KEY_TITLE + " TEXT,"
                + KEY_IMAGE + " TEXT,"
                + KEY_TIME + " INTEGER,"
                + KEY_CAT + " TEXT"
                + ")";
        db.execSQL(CREATE_SAVE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_FAVOURITE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_SAVE_NAME);

        // Create tables again
        onCreate(db);
    }

    public boolean getFavouriteById(String story_id) {
        boolean count = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{story_id};
        Cursor cursor = db.rawQuery("SELECT id FROM favourite WHERE id=? ", args);
        if (cursor.moveToFirst()) {
            count = true;
        }
        cursor.close();
        db.close();
        return count;
    }

    public void removeFavouriteById(String _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  favourite " + " WHERE " + KEY_ID + " = " + _id);
        db.close();
    }

    public boolean getSaveAll(String story_id) {
        boolean count = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{story_id};
        Cursor cursor = db.rawQuery("SELECT id FROM recipe_save", args);
        if (cursor.moveToFirst()) {
            count = true;
        }
        cursor.close();
        db.close();
        return count;
    }

    public boolean getSaveById(String story_id) {
        boolean count = false;
        SQLiteDatabase db = this.getWritableDatabase();
        String[] args = new String[]{story_id};
        Cursor cursor = db.rawQuery("SELECT id FROM recipe_save WHERE id=? ", args);
        if (cursor.moveToFirst()) {
            count = true;
        }
        cursor.close();
        db.close();
        return count;
    }

    public void removeSaveById(String _id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("DELETE FROM  recipe_save " + " WHERE " + KEY_ID + " = " + _id);
        db.close();
    }



    public long insertTable(String TableName, ContentValues contentvalues, String s1) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.insert(TableName, s1, contentvalues);
    }

    public ArrayList<ItemRecipe> getFavourite() {
        ArrayList<ItemRecipe> chapterList = new ArrayList<>();
        String selectQuery = "SELECT *  FROM "
                + TABLE_FAVOURITE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                ItemRecipe contact = new ItemRecipe();
                contact.setRecipeId(cursor.getString(cursor.getColumnIndexOrThrow(KEY_ID)));
                contact.setRecipeName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_TITLE)));
                contact.setRecipeImageBig(cursor.getString(cursor.getColumnIndexOrThrow(KEY_IMAGE)));
                contact.setRecipeTime(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_TIME)));
                contact.setRecipeCategoryName(cursor.getString(cursor.getColumnIndexOrThrow(KEY_CAT)));

                chapterList.add(contact);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return chapterList;
    }
}
