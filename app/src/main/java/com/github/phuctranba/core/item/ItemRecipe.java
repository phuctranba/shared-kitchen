package com.github.phuctranba.core.item;

import java.util.List;

public class ItemRecipe {

    private String RecipeId;
    private String RecipeCategoryId;
    private String RecipeCategoryName;
    private String RecipeName;
    private String RecipeIngredient;
    private String RecipeDirection;
    private List<String> RecipeDirectionList;
    private String RecipeImageBig;
    private String RecipeImageSmall;
    private String RecipeUrl;
    private String RecipePlayId;
    private String RecipeType;
    private String RecipeTotalRate;
    private String RecipeAvgRate;
    private Boolean RecipeUserLiked;
    private Boolean RecipeUserBookmarked;
    private String RecipeImage;
    private String Status;
    private String RecipeLevel;
    private int RecipeTime;
    private int RecipeViews;
    private int RecipeLikes;
    private int RecipeBookmarks;
    private String VideoId;

    public String getVideoId() {
        return VideoId;
    }

    public void setVideoId(String videoId) {
        VideoId = videoId;
    }

    public String getRecipeLevel() {
        return RecipeLevel;
    }

    public void setRecipeLevel(String recipeLevel) {
        RecipeLevel = recipeLevel;
    }

    public Boolean getRecipeUserBookmarked() {
        return RecipeUserBookmarked;
    }

    public void setRecipeUserBookmarked(Boolean recipeBookmarked) {
        RecipeUserBookmarked = recipeBookmarked;
    }

    public List<String> getRecipeDirectionList() {
        return RecipeDirectionList;
    }

    public void setRecipeDirectionList(List<String> recipeDirectionList) {
        RecipeDirectionList = recipeDirectionList;
    }

    public int getRecipeViews() {
        return RecipeViews;
    }

    public void setRecipeViews(int recipeViews) {
        RecipeViews = recipeViews;
    }

    public String getRecipeTotalRate() {
        return RecipeTotalRate;
    }

    public void setRecipeTotalRate(String recipeTotalRate) {
        RecipeTotalRate = recipeTotalRate;
    }

    public String getRecipeAvgRate() {
        return RecipeAvgRate;
    }

    public void setRecipeAvgRate(String recipeAvgRate) {
        RecipeAvgRate = recipeAvgRate;
    }

    public String getRecipeId() {
        return RecipeId;
    }

    public void setRecipeId(String recipeId) {
        RecipeId = recipeId;
    }

    public String getRecipeCategoryId() {
        return RecipeCategoryId;
    }

    public void setRecipeCategoryId(String RecipeCategoryId) {
        this.RecipeCategoryId = RecipeCategoryId;
    }

    public String getRecipeCategoryName() {
        return RecipeCategoryName;
    }

    public void setRecipeCategoryName(String RecipeCategoryName) {
        this.RecipeCategoryName = RecipeCategoryName;
    }

    public String getRecipeName() {
        return RecipeName;
    }

    public void setRecipeName(String RecipeName) {
        this.RecipeName = RecipeName;
    }


    public int getRecipeTime() {
        return RecipeTime;
    }

    public void setRecipeTime(int recipeTime) {
        RecipeTime = recipeTime;
    }

    public String getRecipeIngredient() {
        return RecipeIngredient;
    }

    public void setRecipeIngredient(String RecipeIngredient) {
        this.RecipeIngredient = RecipeIngredient;
    }

    public String getRecipeDirection() {
        return RecipeDirection;
    }

    public void setRecipeDirection(String RecipeDirection) {
        this.RecipeDirection = RecipeDirection;
    }

    public String getRecipeImageBig() {
        return RecipeImageBig;
    }

    public void setRecipeImageBig(String RecipeImageBig) {
        this.RecipeImageBig = RecipeImageBig;
    }

    public String getRecipeImageSmall() {
        return RecipeImageSmall;
    }

    public void setRecipeImageSmall(String RecipeImageSmall) {
        this.RecipeImageSmall = RecipeImageSmall;
    }

    public String getRecipeUrl() {
        return RecipeUrl;
    }

    public void setRecipeUrl(String RecipeUrl) {
        this.RecipeUrl = RecipeUrl;
    }

    public String getRecipePlayId() {
        return RecipePlayId;
    }

    public void setRecipePlayId(String RecipePlayId) {
        this.RecipePlayId = RecipePlayId;
    }


    public String getRecipeType() {
        return RecipeType;
    }

    public void setRecipeType(String RecipeType) {
        this.RecipeType = RecipeType;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public Boolean getRecipeUserLiked() {
        return RecipeUserLiked;
    }

    public void setRecipeUserLiked(Boolean recipeUserLiked) {
        RecipeUserLiked = recipeUserLiked;
    }

    public String getRecipeImage() {
        return RecipeImage;
    }

    public void setRecipeImage(String recipeImage) {
        RecipeImage = recipeImage;
    }

    public int getRecipeLikes() {
        return RecipeLikes;
    }

    public void setRecipeLikes(int recipeLikes) {
        RecipeLikes = recipeLikes;
    }

    public int getRecipeBookmarks() {
        return RecipeBookmarks;
    }

    public void setRecipeBookmarks(int recipeBookmarks) {
        RecipeBookmarks = recipeBookmarks;
    }
}
