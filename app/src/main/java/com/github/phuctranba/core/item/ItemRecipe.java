package com.github.phuctranba.core.item;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ItemRecipe implements Serializable {

    private String RecipeId;
    private String RecipeAuthorId;
    private String RecipeName;
    private EnumLevelOfDifficult RecipeLevelOfDifficult;
    private String recipeAuthor;
    private List<ItemIngredient> RecipeIngredient;
    private String RecipeImage;
    private String RecipeVideo;
    private List<String> RecipeSteps;
    private String RecipeRequire;
    private EnumRecipeType RecipeType;
    private EnumStorage RecipeStorage;
    private Date RecipeTimeCreate;
    private boolean Fav;

    public String getRecipeAuthorId() {
        return RecipeAuthorId;
    }

    public void setRecipeAuthorId(String recipeAuthorId) {
        RecipeAuthorId = recipeAuthorId;
    }

    public String getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(String recipeId) {
        RecipeId = recipeId;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String recipeName) {
        RecipeName = recipeName;
    }

    public EnumLevelOfDifficult getRecipeLevelOfDifficult() {
        return RecipeLevelOfDifficult;
    }

    public void setRecipeLevelOfDifficult(EnumLevelOfDifficult recipeLevelOfDifficult) {
        RecipeLevelOfDifficult = recipeLevelOfDifficult;
    }

    public boolean isFav() {
        return Fav;
    }

    public void setFav(boolean fav) {
        Fav = fav;
    }

    public String getRecipeAuthor() {
        return recipeAuthor;
    }

    public void setRecipeAuthor(String recipeAuthor) {
        this.recipeAuthor = recipeAuthor;
    }

    public List<ItemIngredient> getRecipeIngredient() {
        return RecipeIngredient;
    }

    public void setRecipeIngredient(List<ItemIngredient> recipeIngredient) {
        RecipeIngredient = recipeIngredient;
    }

    public String getRecipeImage() {
        return RecipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        RecipeImage = recipeImage;
    }

    public String getRecipeVideo() {
        return RecipeVideo;
    }

    public void setRecipeVideo(String recipeVideo) {
        RecipeVideo = recipeVideo;
    }

    public List<String> getRecipeSteps() {
        return RecipeSteps;
    }

    public void setRecipeSteps(List<String> recipeSteps) {
        RecipeSteps = recipeSteps;
    }

    public String getRecipeRequire() {
        return RecipeRequire;
    }

    public void setRecipeRequire(String recipeRequire) {
        RecipeRequire = recipeRequire;
    }

    public EnumRecipeType getRecipeType() {
        return RecipeType;
    }

    public void setRecipeType(EnumRecipeType recipeType) {
        RecipeType = recipeType;
    }

    public EnumStorage getRecipeStorage() {
        return RecipeStorage;
    }

    public void setRecipeStorage(EnumStorage recipeStorage) {
        RecipeStorage = recipeStorage;
    }

    public Date getRecipeTimeCreate() {
        return RecipeTimeCreate;
    }

    public void setRecipeTimeCreate(Date recipeTimeCreate) {
        RecipeTimeCreate = recipeTimeCreate;
    }
}
