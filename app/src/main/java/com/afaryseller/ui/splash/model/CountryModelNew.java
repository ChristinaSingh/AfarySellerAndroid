package com.afaryseller.ui.splash.model;

public class CountryModelNew {
    private String id,name,nameFr;

    public CountryModelNew(String id, String name,String nameFr) {
        this.id = id;
        this.name = name;
        this.nameFr = nameFr;
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

    public String getNameFr() {
        return nameFr;
    }

    public void setNameFr(String nameFr) {
        this.nameFr = nameFr;
    }
}
