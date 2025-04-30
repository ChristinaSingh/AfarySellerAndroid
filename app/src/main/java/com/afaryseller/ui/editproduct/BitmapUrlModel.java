package com.afaryseller.ui.editproduct;

import android.graphics.Bitmap;

public class BitmapUrlModel {
    String url,chkImg;
    Bitmap img;

    public BitmapUrlModel(String url, String chkImg, Bitmap img) {
        this.url = url;
        this.chkImg = chkImg;
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getChkImg() {
        return chkImg;
    }

    public void setChkImg(String chkImg) {
        this.chkImg = chkImg;
    }

    public Bitmap getImg() {
        return img;
    }

    public void setImg(Bitmap img) {
        this.img = img;
    }
}
