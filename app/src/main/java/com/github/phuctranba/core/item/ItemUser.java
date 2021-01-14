package com.github.phuctranba.core.item;

public class ItemUser {
    private String UserId;
    private String Name;
    private String Avatar;
    private String Likes;
    private String Followers;
    private String RecipeCounter;
    private String Rate;
    private boolean Followed;

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getLikes() {
        return Likes;
    }

    public void setLikes(String likes) {
        Likes = likes;
    }

    public String getFollowers() {
        return Followers;
    }

    public void setFollowers(String followers) {
        Followers = followers;
    }

    public boolean isFollowed() {
        return Followed;
    }

    public void setFollowed(boolean followed) {
        Followed = followed;
    }

    public String getRecipeCounter() {
        return RecipeCounter;
    }

    public void setRecipeCounter(String recipeCounter) {
        RecipeCounter = recipeCounter;
    }

    public float getRate() {
        if (Rate.equals("0") || Rate.equals("null") || Rate.equals("")) {
            return 0;
        }
        return Float.parseFloat(Rate);
    }

    public void setRate(String rate) {
        Rate = rate;
    }
}
