package com.github.phuctranba.core.util;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import com.github.phuctranba.core.item.ItemRecipe;

public class Common {
    public static Boolean isTrue(String val) {
        return val.equals("true");
    }

    @SuppressLint("StaticFieldLeak")
    public static class actionRecipe extends AsyncTask<String, Void, String> {

        private String user_id;
        private boolean isFav;
        private String recipe_id;

        public actionRecipe(String user_id, boolean isFav, String recipe_id) {
            this.user_id = user_id;
            this.isFav = isFav;
            this.recipe_id = recipe_id;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {
            String url = String.format(params[0], user_id, recipe_id);
            String result = JsonUtils.getJSONString(url);
            return result;
        }


        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            try {
                JSONObject mainJson = new JSONObject(result);
                int code = mainJson.getInt(Constant.CODE);
                if (code == 1 || code == 2) {
//                    Toast.makeText(mContext, mContext.getString(R.string.dialog_success), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("ACTION_RECIPE: ", result);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    public static void insertRecipe(String tableName, ItemRecipe singleItem, boolean isFav, DatabaseHelper databaseHelper) {
        ContentValues fav = new ContentValues();
//        fav.put(DatabaseHelper.KEY_ID, singleItem.getRecipeId());
//        fav.put(DatabaseHelper.KEY_TITLE, singleItem.getRecipeName());
//        fav.put(DatabaseHelper.KEY_IMAGE, singleItem.getRecipeImageBig());
//        fav.put(DatabaseHelper.KEY_TIME, singleItem.getRecipeTime());
//        fav.put(DatabaseHelper.KEY_CAT, singleItem.getRecipeCategoryName());

        if (isFav) {
            if (!databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
                databaseHelper.insertTable(tableName, fav, null);
            }
        } else {
            if (!databaseHelper.getSaveById(singleItem.getRecipeId())) {
                databaseHelper.insertTable(tableName, fav, null);
            }
        }

    }

    public static void removeRecipe(ItemRecipe singleItem, boolean isFav, DatabaseHelper databaseHelper) {
        if (isFav) {
            if (databaseHelper.getFavouriteById(singleItem.getRecipeId())) {
                databaseHelper.removeFavouriteById(singleItem.getRecipeId());
            }
        } else {
            if (databaseHelper.getSaveById(singleItem.getRecipeId())) {
                databaseHelper.removeSaveById(singleItem.getRecipeId());
            }
        }
    }
}
