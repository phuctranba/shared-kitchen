package com.github.phuctranba.core.item;

public enum EnumRecipeType {
    LOAI_CONG_THUC("Loại công thức"),
    NAU_HAM("Nấu - Hầm"),
    NUONG("Nướng"),
    HAP_LUOC("Hấp - Luộc"),
    SALAD("Salad"),
    BANH("Bánh"),
    CHIEN_XAO("Chiên - Xào");

    private final String name;

    private EnumRecipeType(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
