package com.afaryseller.ui.sellerreport;



import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class PeriodicReportModel {

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


    public static class Result {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("order_details")
        @Expose
        private List<OrderDetail> orderDetails;

        private String reportTotal="0";

        public String getReportTotal() {
            return reportTotal;
        }

        public void setReportTotal(String reportTotal) {
            this.reportTotal = reportTotal;
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

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public List<OrderDetail> getOrderDetails() {
            return orderDetails;
        }

        public void setOrderDetails(List<OrderDetail> orderDetails) {
            this.orderDetails = orderDetails;
        }

        public class OrderDetail {

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
            @SerializedName("options")
            @Expose
            private String options;
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
            @SerializedName("total_amount_old")
            @Expose
            private Object totalAmountOld;
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
            @SerializedName("view_status")
            @Expose
            private String viewStatus;
            @SerializedName("event_time")
            @Expose
            private String eventTime;
            @SerializedName("user_number")
            @Expose
            private Object userNumber;
            @SerializedName("admin_accepted_time")
            @Expose
            private Object adminAcceptedTime;
            @SerializedName("insert_id_delivery")
            @Expose
            private String insertIdDelivery;
            @SerializedName("delivery_type")
            @Expose
            private String deliveryType;
            @SerializedName("user_name")
            @Expose
            private String userName;
            @SerializedName("user_image")
            @Expose
            private String userImage;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("address_name")
            @Expose
            private String addressName;
            @SerializedName("product_list")
            @Expose
            private List<Product> productList;

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

            public String getOptions() {
                return options;
            }

            public void setOptions(String options) {
                this.options = options;
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

            public Object getTotalAmountOld() {
                return totalAmountOld;
            }

            public void setTotalAmountOld(Object totalAmountOld) {
                this.totalAmountOld = totalAmountOld;
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

            public String getViewStatus() {
                return viewStatus;
            }

            public void setViewStatus(String viewStatus) {
                this.viewStatus = viewStatus;
            }

            public String getEventTime() {
                return eventTime;
            }

            public void setEventTime(String eventTime) {
                this.eventTime = eventTime;
            }

            public Object getUserNumber() {
                return userNumber;
            }

            public void setUserNumber(Object userNumber) {
                this.userNumber = userNumber;
            }

            public Object getAdminAcceptedTime() {
                return adminAcceptedTime;
            }

            public void setAdminAcceptedTime(Object adminAcceptedTime) {
                this.adminAcceptedTime = adminAcceptedTime;
            }

            public String getInsertIdDelivery() {
                return insertIdDelivery;
            }

            public void setInsertIdDelivery(String insertIdDelivery) {
                this.insertIdDelivery = insertIdDelivery;
            }

            public String getDeliveryType() {
                return deliveryType;
            }

            public void setDeliveryType(String deliveryType) {
                this.deliveryType = deliveryType;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getUserImage() {
                return userImage;
            }

            public void setUserImage(String userImage) {
                this.userImage = userImage;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getAddressName() {
                return addressName;
            }

            public void setAddressName(String addressName) {
                this.addressName = addressName;
            }

            public List<Product> getProductList() {
                return productList;
            }

            public void setProductList(List<Product> productList) {
                this.productList = productList;
            }

            public class Product {

                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("common_order_id")
                @Expose
                private String commonOrderId;
                @SerializedName("order_id")
                @Expose
                private String orderId;
                @SerializedName("afary_code")
                @Expose
                private String afaryCode;
                @SerializedName("cutomer_afary_code")
                @Expose
                private Object cutomerAfaryCode;
                @SerializedName("address_id")
                @Expose
                private String addressId;
                @SerializedName("delivery_free")
                @Expose
                private String deliveryFree;
                @SerializedName("user_id")
                @Expose
                private String userId;
                @SerializedName("restaurant_id")
                @Expose
                private Object restaurantId;
                @SerializedName("item_id")
                @Expose
                private String itemId;
                @SerializedName("merchant_id")
                @Expose
                private Object merchantId;
                @SerializedName("offer_id")
                @Expose
                private Object offerId;
                @SerializedName("price")
                @Expose
                private String price;
                @SerializedName("total_amount")
                @Expose
                private String totalAmount;
                @SerializedName("total_amount_old")
                @Expose
                private Object totalAmountOld;
                @SerializedName("quantity")
                @Expose
                private String quantity;
                @SerializedName("address")
                @Expose
                private Object address;
                @SerializedName("options")
                @Expose
                private String options;
                @SerializedName("payment_type")
                @Expose
                private String paymentType;
                @SerializedName("date")
                @Expose
                private Object date;
                @SerializedName("time")
                @Expose
                private Object time;
                @SerializedName("type")
                @Expose
                private Object type;
                @SerializedName("status")
                @Expose
                private String status;
                @SerializedName("payment_status")
                @Expose
                private String paymentStatus;
                @SerializedName("payment_method")
                @Expose
                private String paymentMethod;
                @SerializedName("seller_amount")
                @Expose
                private String sellerAmount;
                @SerializedName("date_time")
                @Expose
                private String dateTime;
                @SerializedName("notif")
                @Expose
                private String notif;
                @SerializedName("shop_id")
                @Expose
                private String shopId;
                @SerializedName("seller_id")
                @Expose
                private String sellerId;
                @SerializedName("cat_id")
                @Expose
                private String catId;
                @SerializedName("item_amount")
                @Expose
                private String itemAmount;
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
                @SerializedName("view_status")
                @Expose
                private String viewStatus;
                @SerializedName("seller_amout")
                @Expose
                private Object sellerAmout;
                @SerializedName("tax_of_product_each")
                @Expose
                private String taxOfProductEach;
                @SerializedName("tax_of_admin_each")
                @Expose
                private String taxOfAdminEach;
                @SerializedName("tax_product_each")
                @Expose
                private String taxProductEach;
                @SerializedName("tax_admin_each")
                @Expose
                private String taxAdminEach;
                @SerializedName("return_to_seller_status")
                @Expose
                private String returnToSellerStatus;
                @SerializedName("product_images")
                @Expose
                private String productImages;
                @SerializedName("image_1")
                @Expose
                private String image1;
                @SerializedName("product_name")
                @Expose
                private String productName;
                @SerializedName("currency")
                @Expose
                private String currency;
                @SerializedName("product_price")
                @Expose
                private String productPrice;
                @SerializedName("local_currency")
                @Expose
                private Object localCurrency;
                @SerializedName("show_currency_code")
                @Expose
                private Object showCurrencyCode;
                @SerializedName("local_price")
                @Expose
                private String localPrice;
                @SerializedName("shop_image")
                @Expose
                private String shopImage;
                @SerializedName("shop_name")
                @Expose
                private String shopName;

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getCommonOrderId() {
                    return commonOrderId;
                }

                public void setCommonOrderId(String commonOrderId) {
                    this.commonOrderId = commonOrderId;
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

                public Object getCutomerAfaryCode() {
                    return cutomerAfaryCode;
                }

                public void setCutomerAfaryCode(Object cutomerAfaryCode) {
                    this.cutomerAfaryCode = cutomerAfaryCode;
                }

                public String getAddressId() {
                    return addressId;
                }

                public void setAddressId(String addressId) {
                    this.addressId = addressId;
                }

                public String getDeliveryFree() {
                    return deliveryFree;
                }

                public void setDeliveryFree(String deliveryFree) {
                    this.deliveryFree = deliveryFree;
                }

                public String getUserId() {
                    return userId;
                }

                public void setUserId(String userId) {
                    this.userId = userId;
                }

                public Object getRestaurantId() {
                    return restaurantId;
                }

                public void setRestaurantId(Object restaurantId) {
                    this.restaurantId = restaurantId;
                }

                public String getItemId() {
                    return itemId;
                }

                public void setItemId(String itemId) {
                    this.itemId = itemId;
                }

                public Object getMerchantId() {
                    return merchantId;
                }

                public void setMerchantId(Object merchantId) {
                    this.merchantId = merchantId;
                }

                public Object getOfferId() {
                    return offerId;
                }

                public void setOfferId(Object offerId) {
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

                public Object getTotalAmountOld() {
                    return totalAmountOld;
                }

                public void setTotalAmountOld(Object totalAmountOld) {
                    this.totalAmountOld = totalAmountOld;
                }

                public String getQuantity() {
                    return quantity;
                }

                public void setQuantity(String quantity) {
                    this.quantity = quantity;
                }

                public Object getAddress() {
                    return address;
                }

                public void setAddress(Object address) {
                    this.address = address;
                }

                public String getOptions() {
                    return options;
                }

                public void setOptions(String options) {
                    this.options = options;
                }

                public String getPaymentType() {
                    return paymentType;
                }

                public void setPaymentType(String paymentType) {
                    this.paymentType = paymentType;
                }

                public Object getDate() {
                    return date;
                }

                public void setDate(Object date) {
                    this.date = date;
                }

                public Object getTime() {
                    return time;
                }

                public void setTime(Object time) {
                    this.time = time;
                }

                public Object getType() {
                    return type;
                }

                public void setType(Object type) {
                    this.type = type;
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

                public String getPaymentMethod() {
                    return paymentMethod;
                }

                public void setPaymentMethod(String paymentMethod) {
                    this.paymentMethod = paymentMethod;
                }

                public String getSellerAmount() {
                    return sellerAmount;
                }

                public void setSellerAmount(String sellerAmount) {
                    this.sellerAmount = sellerAmount;
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

                public String getSellerId() {
                    return sellerId;
                }

                public void setSellerId(String sellerId) {
                    this.sellerId = sellerId;
                }

                public String getCatId() {
                    return catId;
                }

                public void setCatId(String catId) {
                    this.catId = catId;
                }

                public String getItemAmount() {
                    return itemAmount;
                }

                public void setItemAmount(String itemAmount) {
                    this.itemAmount = itemAmount;
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

                public String getViewStatus() {
                    return viewStatus;
                }

                public void setViewStatus(String viewStatus) {
                    this.viewStatus = viewStatus;
                }

                public Object getSellerAmout() {
                    return sellerAmout;
                }

                public void setSellerAmout(Object sellerAmout) {
                    this.sellerAmout = sellerAmout;
                }

                public String getTaxOfProductEach() {
                    return taxOfProductEach;
                }

                public void setTaxOfProductEach(String taxOfProductEach) {
                    this.taxOfProductEach = taxOfProductEach;
                }

                public String getTaxOfAdminEach() {
                    return taxOfAdminEach;
                }

                public void setTaxOfAdminEach(String taxOfAdminEach) {
                    this.taxOfAdminEach = taxOfAdminEach;
                }

                public String getTaxProductEach() {
                    return taxProductEach;
                }

                public void setTaxProductEach(String taxProductEach) {
                    this.taxProductEach = taxProductEach;
                }

                public String getTaxAdminEach() {
                    return taxAdminEach;
                }

                public void setTaxAdminEach(String taxAdminEach) {
                    this.taxAdminEach = taxAdminEach;
                }

                public String getReturnToSellerStatus() {
                    return returnToSellerStatus;
                }

                public void setReturnToSellerStatus(String returnToSellerStatus) {
                    this.returnToSellerStatus = returnToSellerStatus;
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

                public String getProductName() {
                    return productName;
                }

                public void setProductName(String productName) {
                    this.productName = productName;
                }

                public String getCurrency() {
                    return currency;
                }

                public void setCurrency(String currency) {
                    this.currency = currency;
                }

                public String getProductPrice() {
                    return productPrice;
                }

                public void setProductPrice(String productPrice) {
                    this.productPrice = productPrice;
                }

                public Object getLocalCurrency() {
                    return localCurrency;
                }

                public void setLocalCurrency(Object localCurrency) {
                    this.localCurrency = localCurrency;
                }

                public Object getShowCurrencyCode() {
                    return showCurrencyCode;
                }

                public void setShowCurrencyCode(Object showCurrencyCode) {
                    this.showCurrencyCode = showCurrencyCode;
                }

                public String getLocalPrice() {
                    return localPrice;
                }

                public void setLocalPrice(String localPrice) {
                    this.localPrice = localPrice;
                }

                public String getShopImage() {
                    return shopImage;
                }

                public void setShopImage(String shopImage) {
                    this.shopImage = shopImage;
                }

                public String getShopName() {
                    return shopName;
                }

                public void setShopName(String shopName) {
                    this.shopName = shopName;
                }

            }


        }


    }


}


