package com.github.phuctranba.core.item;

import java.io.Serializable;

public class ItemIngredient implements Serializable {
    private String IngredientId;
    private String IngredientName;
    private String IngredientAmount;

    public String getIngredientId() {
        return IngredientId;
    }

    public void setIngredientId(String ingredientId) {
        IngredientId = ingredientId;
    }

    public String getIngredientName() {
        return IngredientName;
    }

    public void setIngredientName(String ingredientName) {
        IngredientName = ingredientName;
    }

    public String getIngredientAmount() {
        return IngredientAmount;
    }

    public void setIngredientAmount(String ingredientAmount) {
        IngredientAmount = ingredientAmount;
    }
}
