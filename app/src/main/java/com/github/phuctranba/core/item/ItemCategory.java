package com.github.phuctranba.core.item;

public class ItemCategory {

    private String CategoryId;
    private String CategoryName;
    private String CategoryImage;
    private String CategoryImageBig;
    private String CategoryImageSmall;
    private String CategoryImageThumb;
    private String CategoryImageIcon;


    public String getCategoryImageIcon() {
        return CategoryImageIcon;
    }

    public void setCategoryImageIcon(String categoryImageIcon) {
        CategoryImageIcon = categoryImageIcon;
    }

    public String getCategoryImage() {
        return CategoryImage;
    }

    public void setCategoryImage(String categoryImage) {
        CategoryImage = categoryImage;
    }

    public String getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(String CategoryId) {
        this.CategoryId = CategoryId;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String CategoryName) {
        this.CategoryName = CategoryName;
    }

    public String getCategoryImageBig() {
        return CategoryImageBig;
    }

    public void setCategoryImageBig(String CategoryImageBig) {
        this.CategoryImageBig = CategoryImageBig;
    }

    public String getCategoryImageSmall() {
        return CategoryImageSmall;
    }

    public void setCategoryImageSmall(String CategoryImageSmall) {
        this.CategoryImageSmall = CategoryImageSmall;
    }

    public String getCategoryImageThumb() {
        return CategoryImageThumb;
    }

    public void setCategoryImageThumb(String CategoryImageThumb) {
        this.CategoryImageThumb = CategoryImageThumb;
    }

}
