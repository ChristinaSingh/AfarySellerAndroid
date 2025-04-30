package com.afaryseller.ui.subseller.subsellerlsit;

import android.view.View;

import com.afaryseller.ui.shoplist.ShopModel;

public interface SubSellerListListener {
    void onListClick(int position, ShopModel.Result data, String tag);
}
