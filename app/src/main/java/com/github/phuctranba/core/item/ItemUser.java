package com.github.phuctranba.core.item;

import java.util.List;

public class ItemUser {
    private String UserId;
    private String Name;
    private String Avatar;
    private boolean IsAdmin;
    private String Email;
    private List<ItemMenu> ItemMenus;
    private List<ItemRecipe> ItemRecipes;

    public ItemUser(String name, boolean isAdmin, String email) {
        Name = name;
        IsAdmin = isAdmin;
        Email = email;
    }

    public ItemUser() {
    }

    public boolean isAdmin() {
        return IsAdmin;
    }

    public void setAdmin(boolean admin) {
        IsAdmin = admin;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public List<ItemMenu> getItemMenus() {
        return ItemMenus;
    }

    public void setItemMenus(List<ItemMenu> itemMenus) {
        ItemMenus = itemMenus;
    }

    public List<ItemRecipe> getItemRecipes() {
        return ItemRecipes;
    }

    public void setItemRecipes(List<ItemRecipe> itemRecipes) {
        ItemRecipes = itemRecipes;
    }

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
}
