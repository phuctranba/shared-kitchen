package com.github.phuctranba.core.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.phuctranba.core.item.EnumLevelOfDifficult;
import com.github.phuctranba.core.item.EnumRecipeType;
import com.github.phuctranba.core.item.EnumStorage;
import com.github.phuctranba.core.item.ItemIngredient;
import com.github.phuctranba.core.item.ItemRecipe;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "recipeapp.db";

    public static final String TABLE_RECIPE = "recipe";
    public static final String TABLE_STEP = "step";
    public static final String TABLE_INGREDIENT = "ingredient";


    public static final String COLUMN_RECIPE_ID = "id";
    public static final String COLUMN_RECIPE_NAME = "name";
    public static final String COLUMN_RECIPE_LEVEL_DIF = "level_dif";
    public static final String COLUMN_RECIPE_AUTHOR = "author";
    public static final String COLUMN_RECIPE_IMAGE = "image";
    public static final String COLUMN_RECIPE_VIDEO = "video";
    public static final String COLUMN_RECIPE_REQUIRE = "require";
    public static final String COLUMN_RECIPE_TYPE = "type";
    public static final String COLUMN_RECIPE_STORAGE = "storage";
    public static final String COLUMN_RECIPE_TIME_CREATE = "time_create";
    public static final String COLUMN_RECIPE_AUTHOR_ID = "author_id";

    public static final String COLUMN_STEP_ID = "id";
    public static final String COLUMN_STEP_ORDER = "order_step";
    public static final String COLUMN_STEP_CONTENT = "content";
    public static final String COLUMN_STEP_RECIPE = "recipe";

    public static final String COLUMN_INGREDIENT_ID = "id";
    public static final String COLUMN_INGREDIENT_NAME = "name";
    public static final String COLUMN_INGREDIENT_AMOUNT = "amount";
    public static final String COLUMN_INGREDIENT_RECIPE = "recipe";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //Tạo cơ sở dữ liệu
    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_RECIPE_TABLE = "CREATE TABLE " + TABLE_RECIPE + "("
                + COLUMN_RECIPE_ID + " TEXT PRIMARY KEY,"
                + COLUMN_RECIPE_NAME + " TEXT,"
                + COLUMN_RECIPE_LEVEL_DIF + " TEXT,"
                + COLUMN_RECIPE_AUTHOR + " TEXT,"
                + COLUMN_RECIPE_IMAGE + " TEXT,"
                + COLUMN_RECIPE_VIDEO + " TEXT,"
                + COLUMN_RECIPE_REQUIRE + " TEXT,"
                + COLUMN_RECIPE_TYPE + " TEXT,"
                + COLUMN_RECIPE_STORAGE + " TEXT,"
                + COLUMN_RECIPE_TIME_CREATE + " INTEGER,"
                + COLUMN_RECIPE_AUTHOR_ID + " TEXT"
                + ")";
        db.execSQL(CREATE_RECIPE_TABLE);

        String CREATE_STEP_TABLE = "CREATE TABLE " + TABLE_STEP + "("
                + COLUMN_STEP_ID + " TEXT PRIMARY KEY,"
                + COLUMN_STEP_ORDER + " INTEGER,"
                + COLUMN_STEP_CONTENT + " TEXT,"
                + COLUMN_STEP_RECIPE + " TEXT"
                + ")";
        db.execSQL(CREATE_STEP_TABLE);

        String CREATE_INGREDIENT_TABLE = "CREATE TABLE " + TABLE_INGREDIENT + "("
                + COLUMN_INGREDIENT_ID + " TEXT PRIMARY KEY,"
                + COLUMN_INGREDIENT_NAME + " TEXT,"
                + COLUMN_INGREDIENT_AMOUNT + " TEXT,"
                + COLUMN_INGREDIENT_RECIPE + " TEXT"
                + ")";
        db.execSQL(CREATE_INGREDIENT_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_RECIPE);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_STEP);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENT);

        // Create tables again
        onCreate(db);
    }


    // TABLE_RECIPE
    //Thêm một danh sách công thức
    public void addListRecipe(List<ItemRecipe> recipeList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        for (ItemRecipe recipe : recipeList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RECIPE_ID, recipe.getRecipeId());
            values.put(COLUMN_RECIPE_NAME, recipe.getRecipeName());
            values.put(COLUMN_RECIPE_LEVEL_DIF, recipe.getRecipeLevelOfDifficult().name());
            values.put(COLUMN_RECIPE_AUTHOR, recipe.getRecipeAuthor());
            values.put(COLUMN_RECIPE_REQUIRE, recipe.getRecipeRequire());
            values.put(COLUMN_RECIPE_TYPE, recipe.getRecipeType().name());
            values.put(COLUMN_RECIPE_STORAGE, recipe.getRecipeStorage().name());
            if (recipe.getRecipeImage() != null)
                values.put(COLUMN_RECIPE_IMAGE, recipe.getRecipeImage());
            if (recipe.getRecipeVideo() != null)
                values.put(COLUMN_RECIPE_VIDEO, recipe.getRecipeVideo());
            values.put(COLUMN_RECIPE_TIME_CREATE, recipe.getRecipeTimeCreate().getTime());
            values.put(COLUMN_RECIPE_AUTHOR_ID, recipe.getRecipeAuthorId());

            sqLiteDatabase.insert(TABLE_RECIPE, null, values);
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();

        for (ItemRecipe recipe : recipeList) {
            addListStep(recipe.getRecipeSteps(), recipe.getRecipeId());
            addListIngredient(recipe.getRecipeIngredient(), recipe.getRecipeId());
        }
    }

    //Cập nhật một danh sách công thức
    public void updateListRecipe(List<ItemRecipe> recipeList) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        for (ItemRecipe recipe : recipeList) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_RECIPE_NAME, recipe.getRecipeName());
            values.put(COLUMN_RECIPE_LEVEL_DIF, recipe.getRecipeLevelOfDifficult().name());
            values.put(COLUMN_RECIPE_REQUIRE, recipe.getRecipeRequire());
            values.put(COLUMN_RECIPE_TYPE, recipe.getRecipeType().name());
            values.put(COLUMN_RECIPE_STORAGE, recipe.getRecipeStorage().name());
            if (recipe.getRecipeImage() != null)
                values.put(COLUMN_RECIPE_IMAGE, recipe.getRecipeImage());
            if (recipe.getRecipeVideo() != null)
                values.put(COLUMN_RECIPE_VIDEO, recipe.getRecipeVideo());
            values.put(COLUMN_RECIPE_TIME_CREATE, recipe.getRecipeTimeCreate().getTime());

            sqLiteDatabase.update(TABLE_RECIPE, values, COLUMN_RECIPE_ID + " = ?",
                    new String[]{String.valueOf(recipe.getRecipeId())});
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();

        for (ItemRecipe recipe : recipeList) {
            deleteStepsByRecipe(recipe.getRecipeId());
            deleteIngredientsByRecipe(recipe.getRecipeId());
            addListStep(recipe.getRecipeSteps(), recipe.getRecipeId());
            addListIngredient(recipe.getRecipeIngredient(), recipe.getRecipeId());
        }
    }

//Thêm một công thức
    public boolean addRecipe(ItemRecipe recipe) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_RECIPE_ID, recipe.getRecipeId());
        values.put(COLUMN_RECIPE_NAME, recipe.getRecipeName());
        values.put(COLUMN_RECIPE_LEVEL_DIF, recipe.getRecipeLevelOfDifficult().name());
        values.put(COLUMN_RECIPE_AUTHOR, recipe.getRecipeAuthor());
        values.put(COLUMN_RECIPE_REQUIRE, recipe.getRecipeRequire());
        values.put(COLUMN_RECIPE_TYPE, recipe.getRecipeType().name());
        values.put(COLUMN_RECIPE_STORAGE, recipe.getRecipeStorage().name());
        if (recipe.getRecipeImage() != null)
            values.put(COLUMN_RECIPE_IMAGE, recipe.getRecipeImage());
        if (recipe.getRecipeVideo() != null)
            values.put(COLUMN_RECIPE_VIDEO, recipe.getRecipeVideo());
        values.put(COLUMN_RECIPE_TIME_CREATE, recipe.getRecipeTimeCreate().getTime());
        values.put(COLUMN_RECIPE_AUTHOR_ID, recipe.getRecipeAuthorId());

        long value = sqLiteDatabase.insert(TABLE_RECIPE, null, values);
        sqLiteDatabase.close();

        addListStep(recipe.getRecipeSteps(), recipe.getRecipeId());
        addListIngredient(recipe.getRecipeIngredient(), recipe.getRecipeId());

        return value != -1;
    }

    //Lấy tất cả công thức
    public List<ItemRecipe> getAllRecipe() {

        List<ItemRecipe> recipeArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemRecipe itemRecipe = new ItemRecipe();
                itemRecipe.setRecipeId(cursor.getString(0));
                itemRecipe.setRecipeName(cursor.getString(1));
                itemRecipe.setRecipeLevelOfDifficult(EnumLevelOfDifficult.valueOf(cursor.getString(2)));
                itemRecipe.setRecipeAuthor(cursor.getString(3));
                itemRecipe.setRecipeImage(cursor.getString(4));
                itemRecipe.setRecipeVideo(cursor.getString(5));
                itemRecipe.setRecipeRequire(cursor.getString(6));
                itemRecipe.setRecipeType(EnumRecipeType.valueOf(cursor.getString(7)));
                itemRecipe.setRecipeStorage(EnumStorage.valueOf(cursor.getString(8)));
                itemRecipe.setRecipeTimeCreate(new Date(cursor.getLong(9)));
                itemRecipe.setRecipeAuthorId(cursor.getString(10));

                itemRecipe.setRecipeSteps(getStepsByRecipe(itemRecipe.getRecipeId()));
                itemRecipe.setRecipeIngredient(getIngredientsByRecipe(itemRecipe.getRecipeId()));

                // Adding note to list
                recipeArrayList.add(itemRecipe);
            } while (cursor.moveToNext());
        }

        return recipeArrayList;
    }

    //Lấy công thức theo trạng thái lưu
    public List<ItemRecipe> getRecipeByStorage(EnumStorage enumStorage) {

        List<ItemRecipe> recipeArrayList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_RECIPE+" WHERE "+COLUMN_RECIPE_STORAGE+" = '"+enumStorage.name()+"'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemRecipe itemRecipe = new ItemRecipe();
                itemRecipe.setRecipeId(cursor.getString(0));
                itemRecipe.setRecipeName(cursor.getString(1));
                itemRecipe.setRecipeLevelOfDifficult(EnumLevelOfDifficult.valueOf(cursor.getString(2)));
                itemRecipe.setRecipeAuthor(cursor.getString(3));
                itemRecipe.setRecipeImage(cursor.getString(4));
                itemRecipe.setRecipeVideo(cursor.getString(5));
                itemRecipe.setRecipeRequire(cursor.getString(6));
                itemRecipe.setRecipeType(EnumRecipeType.valueOf(cursor.getString(7)));
                itemRecipe.setRecipeStorage(EnumStorage.valueOf(cursor.getString(8)));
                itemRecipe.setRecipeTimeCreate(new Date(cursor.getLong(9)));
                itemRecipe.setRecipeAuthorId(cursor.getString(10));

                itemRecipe.setRecipeSteps(getStepsByRecipe(itemRecipe.getRecipeId()));
                itemRecipe.setRecipeIngredient(getIngredientsByRecipe(itemRecipe.getRecipeId()));

                // Adding note to list
                recipeArrayList.add(itemRecipe);
            } while (cursor.moveToNext());
        }

        return recipeArrayList;
    }


    // TABLE_STEP
    //Thêm danh sách các bước thực hiện theo công thức truyền vào
    public void addListStep(List<String> steps, String recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        for (int i = 0; i < steps.size(); i++) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_STEP_ID, UUID.randomUUID().toString());
            values.put(COLUMN_STEP_ORDER, i);
            values.put(COLUMN_STEP_CONTENT, steps.get(i));
            values.put(COLUMN_STEP_RECIPE, recipeId);

            sqLiteDatabase.insert(TABLE_STEP, null, values);
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    //Xóa các bước thực hiện theo công thức truyền vào
    public void deleteStepsByRecipe(String idRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_STEP, COLUMN_STEP_RECIPE + " = ?",
                new String[] { String.valueOf(idRecipe) });
        db.close();
    }

    //Lấy danh sách các bước theo công thức truyền vào
    public List<String> getStepsByRecipe(String idRecipe) {

        List<String> steps = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_STEP + " WHERE " + COLUMN_STEP_RECIPE + " = '" + idRecipe + "' ORDER BY " + COLUMN_STEP_ORDER;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                steps.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        return steps;
    }


    // TABLE_INGREDIENT
    //Thêm một danh sách nguyên liệu theo công thức truyền vào
    public void addListIngredient(List<ItemIngredient> ingredients, String recipeId) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        sqLiteDatabase.beginTransaction();

        for (ItemIngredient ingredient : ingredients) {
            ContentValues values = new ContentValues();
            values.put(COLUMN_INGREDIENT_ID, ingredient.getIngredientId());
            values.put(COLUMN_INGREDIENT_NAME, ingredient.getIngredientName());
            if (ingredient.getIngredientAmount() != null)
                values.put(COLUMN_INGREDIENT_AMOUNT, ingredient.getIngredientAmount());
            values.put(COLUMN_INGREDIENT_RECIPE, recipeId);

            sqLiteDatabase.insert(TABLE_INGREDIENT, null, values);
        }

        sqLiteDatabase.setTransactionSuccessful();
        sqLiteDatabase.endTransaction();
        sqLiteDatabase.close();
    }

    //Xóa danh sách nguyên liệu của công thức truyền vào
    public void deleteIngredientsByRecipe(String idRecipe) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_INGREDIENT, COLUMN_INGREDIENT_RECIPE + " = ?",
                new String[] { String.valueOf(idRecipe) });
        db.close();
    }

    //Lấy danh sách nguyên liệu của công thức truyền vào
    public List<ItemIngredient> getIngredientsByRecipe(String idRecipe) {

        List<ItemIngredient> ingredients = new ArrayList<>();
        String selectQuery = "SELECT  * FROM " + TABLE_INGREDIENT + " WHERE " + COLUMN_INGREDIENT_RECIPE + " = '" + idRecipe + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                ItemIngredient itemIngredient = new ItemIngredient();

                itemIngredient.setIngredientId(cursor.getString(0));
                itemIngredient.setIngredientName(cursor.getString(1));
                itemIngredient.setIngredientAmount(cursor.getString(2));

                ingredients.add(itemIngredient);
            } while (cursor.moveToNext());
        }

        return ingredients;
    }

    //Xóa tất cả dữ liệu
    public void removeAll()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DatabaseHelper.TABLE_INGREDIENT, null, null);
        db.delete(DatabaseHelper.TABLE_RECIPE, null, null);
        db.delete(DatabaseHelper.TABLE_STEP, null, null);
    }

}
