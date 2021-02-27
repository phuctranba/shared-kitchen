package com.github.phuctranba.core.item;

public enum EnumRecipeType {
    LOAI_CONG_THUC("Loại công thức"),
    NAU_HAM("Nấu, hầm"),
    NUONG("Nướng"),
    HAP_LUOC("Hấp, luộc"),
    SALAD("Salad"),
    BANH("Bánh"),
    CHIEN_XAO("Chiên, xào");

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
