package com.afaryseller.ui.addproduct.model;

public class SelectSubCateModel {
    String id,name,category_name;


    public SelectSubCateModel() {
    }

    public SelectSubCateModel(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public SelectSubCateModel(String id, String name,String category_name) {
        this.id = id;
        this.name = name;
        this.category_name =category_name;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
