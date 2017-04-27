package com.example.sasuke.dailysuvichar.models;

public class Data {
    private String header;
    private int drawable;
    private int category;
    private int color;
    private Boolean isSelected;

    public Data(String header, int drawable, int category, int color, Boolean isSelected) {
        this.header = header;
        this.drawable = drawable;
        this.category = category;
        this.color = color;
        this.isSelected = isSelected;
    }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public int getDrawable() {
        return drawable;
    }

    public void setDrawable(int drawable) {
        this.drawable = drawable;
    }

    public int getCategory() {
        return category;
    }

    public void setCategory(int category) {
        this.category = category;
    }
}
