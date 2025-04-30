package com.afaryseller.ui.orderdetails;

public interface OrderItemListener {
    void orderItem(int position,OrderDetailsModel.Result.Product product,String tag);
}
