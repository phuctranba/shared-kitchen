package com.github.phuctranba.core.item;

import java.util.List;

public class ItemMenu {
    private String MenuId;
    private EnumMenuType Type;
    private List<ItemRecipe> ItemRecipes;

    public String getMenuId() {
        return MenuId;
    }

    public void setMenuId(String menuId) {
        MenuId = menuId;
    }

    public EnumMenuType getType() {
        return Type;
    }

    public void setType(EnumMenuType type) {
        Type = type;
    }

    public List<ItemRecipe> getItemRecipes() {
        return ItemRecipes;
    }

    public void setItemRecipes(List<ItemRecipe> itemRecipes) {
        ItemRecipes = itemRecipes;
    }
}
