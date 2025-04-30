/*
package com.afaryseller.ui.bookedorder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public class Result {


        @SerializedName("order_id")
        @Expose
        private String orderId;

        @SerializedName("total")
        @Expose
        private String total;

        @SerializedName("total_text")
        @Expose
        private String totalText;

        @SerializedName("status")
        @Expose
        private String status;

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        @SerializedName("product_images")
        @Expose
        private List<productImage> productImageList;



        public class productImage {


            @SerializedName("product_images")
            @Expose
            private String productImages;

            public String getImage() {
                return productImages;
            }

            public void setImage(String productImages) {
                this.productImages = productImages;
            }
        }

        public String getTotalText() {
            return totalText;
        }

        public void setTotalText(String totalText) {
            this.totalText = totalText;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public List<productImage> getProductImageList() {
            return productImageList;
        }

        public void setShopImageList(List<productImage> productImageList) {
            this.productImageList = productImageList;
        }
    }



}*/


package com.afaryseller.ui.bookedorder;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderModel {

    @SerializedName("result")
    @Expose
    private List<Result> result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;



    public class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("afary_code")
        @Expose
        private String afaryCode;
        @SerializedName("cart_id")
        @Expose
        private String cartId;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("item_id")
        @Expose
        private String itemId;
        @SerializedName("quantity")
        @Expose
        private String quantity;
        @SerializedName("seller_id")
        @Expose
        private String sellerId;
        @SerializedName("item_amount")
        @Expose
        private String itemAmount;
        @SerializedName("total_amount")
        @Expose
        private String totalAmount;
        @SerializedName("shop_id")
        @Expose
        private String shopId;
        @SerializedName("cat_id")
        @Expose
        private String catId;
        @SerializedName("taxN1")
        @Expose
        private String taxN1;
        @SerializedName("taxN2")
        @Expose
        private String taxN2;
        @SerializedName("delivery_charges")
        @Expose
        private String deliveryCharges;
        @SerializedName("platFormsFees")
        @Expose
        private String platFormsFees;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("payment_status")
        @Expose
        private String paymentStatus;
        @SerializedName("payment_type")
        @Expose
        private String paymentType;
        @SerializedName("sen")
        @Expose
        private String sen;
        @SerializedName("address_id")
        @Expose
        private String addressId;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("price")
        @Expose
        private String price;

        @SerializedName("delivery_type")
        @Expose
        private String deliveryType;

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        @SerializedName("product_list")
        @Expose
        private List<Product> productList;


        public class Product {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("restaurant_id")
            @Expose
            private String restaurantId;
            @SerializedName("item_id")
            @Expose
            private String itemId;
            @SerializedName("merchant_id")
            @Expose
            private String merchantId;
            @SerializedName("offer_id")
            @Expose
            private String offerId;
            @SerializedName("price")
            @Expose
            private String price;
            @SerializedName("total_amount")
            @Expose
            private String totalAmount;
            @SerializedName("quantity")
            @Expose
            private String quantity;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("payment_type")
            @Expose
            private String paymentType;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("time")
            @Expose
            private String time;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("date_time")
            @Expose
            private String dateTime;
            @SerializedName("notif")
            @Expose
            private String notif;
            @SerializedName("shop_id")
            @Expose
            private String shopId;
            @SerializedName("cat_id")
            @Expose
            private String catId;
            @SerializedName("product_images")
            @Expose
            private String productImages;
            @SerializedName("image_1")
            @Expose
            private String image1;



            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getOrderId() {
                return orderId;
            }

            public void setOrderId(String orderId) {
                this.orderId = orderId;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getRestaurantId() {
                return restaurantId;
            }

            public void setRestaurantId(String restaurantId) {
                this.restaurantId = restaurantId;
            }

            public String getItemId() {
                return itemId;
            }

            public void setItemId(String itemId) {
                this.itemId = itemId;
            }

            public String getMerchantId() {
                return merchantId;
            }

            public void setMerchantId(String merchantId) {
                this.merchantId = merchantId;
            }

            public String getOfferId() {
                return offerId;
            }

            public void setOfferId(String offerId) {
                this.offerId = offerId;
            }

            public String getPrice() {
                return price;
            }

            public void setPrice(String price) {
                this.price = price;
            }

            public String getTotalAmount() {
                return totalAmount;
            }

            public void setTotalAmount(String totalAmount) {
                this.totalAmount = totalAmount;
            }

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getPaymentType() {
                return paymentType;
            }

            public void setPaymentType(String paymentType) {
                this.paymentType = paymentType;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public String getNotif() {
                return notif;
            }

            public void setNotif(String notif) {
                this.notif = notif;
            }

            public String getShopId() {
                return shopId;
            }

            public void setShopId(String shopId) {
                this.shopId = shopId;
            }

            public String getCatId() {
                return catId;
            }

            public void setCatId(String catId) {
                this.catId = catId;
            }

            public String getProductImages() {
                return productImages;
            }

            public void setProductImages(String productImages) {
                this.productImages = productImages;
            }

            public String getImage1() {
                return image1;
            }

            public void setImage1(String image1) {
                this.image1 = image1;
            }

        }


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getAfaryCode() {
            return afaryCode;
        }

        public void setAfaryCode(String afaryCode) {
            this.afaryCode = afaryCode;
        }

        public String getCartId() {
            return cartId;
        }

        public void setCartId(String cartId) {
            this.cartId = cartId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getItemId() {
            return itemId;
        }

        public void setItemId(String itemId) {
            this.itemId = itemId;
        }

        public String getQuantity() {
            return quantity;
        }

        public void setQuantity(String quantity) {
            this.quantity = quantity;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getItemAmount() {
            return itemAmount;
        }

        public void setItemAmount(String itemAmount) {
            this.itemAmount = itemAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getCatId() {
            return catId;
        }

        public void setCatId(String catId) {
            this.catId = catId;
        }

        public String getTaxN1() {
            return taxN1;
        }

        public void setTaxN1(String taxN1) {
            this.taxN1 = taxN1;
        }

        public String getTaxN2() {
            return taxN2;
        }

        public void setTaxN2(String taxN2) {
            this.taxN2 = taxN2;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getPlatFormsFees() {
            return platFormsFees;
        }

        public void setPlatFormsFees(String platFormsFees) {
            this.platFormsFees = platFormsFees;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getPaymentStatus() {
            return paymentStatus;
        }

        public void setPaymentStatus(String paymentStatus) {
            this.paymentStatus = paymentStatus;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getSen() {
            return sen;
        }

        public void setSen(String sen) {
            this.sen = sen;
        }

        public String getAddressId() {
            return addressId;
        }

        public void setAddressId(String addressId) {
            this.addressId = addressId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public List<Product> getProductList() {
            return productList;
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

    }



    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}










