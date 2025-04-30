package com.afaryseller.ui.shoplist.listerner;

import android.view.View;

import com.afaryseller.ui.shoplist.ShopModel;

public interface ShopListener {
    void editShop(String shopId, View v, ShopModel.Result data, String tag);
}
