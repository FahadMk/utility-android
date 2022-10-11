package com.dgsl.utility_package.data.model;

public class ColorListModel {
    private int position;
    private String colorCode;
    private String colorName;
    private String colorVariantCode;

    public ColorListModel(String code,int pos,String name,String colorVariant) {
        this.colorCode = code;
        this.position = pos;
        this.colorName = name;
        this.colorVariantCode = colorVariant;
    }

    public String getColorName() {
        return colorName;
    }

    public void setColorName(String colorName) {
        this.colorName = colorName;
    }

    public String getColorVariantCode() {
        return colorVariantCode;
    }

    public void setColorVariantCode(String colorVariantCode) {
        this.colorVariantCode = colorVariantCode;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }
}
