package com.bulgogi.flag.model;

public class CategoryItem {
    public static final int ITEM = 0;
    public static final int SECTION = 1;

    public String name;
    public int type;

    private CategoryItem(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public static CategoryItem valueOf(String name, int type) {
        return new CategoryItem(name, type);
    }
}