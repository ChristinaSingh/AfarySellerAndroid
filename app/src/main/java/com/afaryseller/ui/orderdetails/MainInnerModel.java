package com.afaryseller.ui.orderdetails;

public class MainInnerModel {
    String mainName,innerName;

    public MainInnerModel(String mainName, String innerName) {
        this.mainName = mainName;
        this.innerName = innerName;
    }

    public String getMainName() {
        return mainName;
    }

    public void setMainName(String mainName) {
        this.mainName = mainName;
    }

    public String getInnerName() {
        return innerName;
    }

    public void setInnerName(String innerName) {
        this.innerName = innerName;
    }
}
