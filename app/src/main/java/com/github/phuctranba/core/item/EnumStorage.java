package com.github.phuctranba.core.item;

public enum EnumStorage {
    PERSONAL("Cá nhân"), APPROVED("Từ chối"), WAITING("Đang chờ"), PUBLISHED("Đã duyệt"), COLLECTED("Sưu tập");

    private final String name;

    private EnumStorage(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
