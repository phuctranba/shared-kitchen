package com.github.phuctranba.core.item;

public enum EnumLevelOfDifficult {
    DO_KHO("Độ khó"),
    MOI_VAO_BEP("Mới vào bếp"),
    PHO_THONG("Phổ thông"),
    CUNG_TAY("Cứng tay"),
    PHUC_TAP("Phức tạp"),
    CHUYEN_NGHIEP("Chuyên nghiệp");

    private final String name;

    private EnumLevelOfDifficult(String s) {
        name = s;
    }

    public boolean equalsName(String otherName) {
        return name.equals(otherName);
    }

    public String toString() {
        return this.name;
    }
}
