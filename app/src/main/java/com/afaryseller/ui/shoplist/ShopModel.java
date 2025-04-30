package com.afaryseller.ui.shoplist;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ShopModel implements Serializable {

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

    public class Result implements Serializable{

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sub_admin_id")
        @Expose
        private String subAdminId;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("image_1")
        @Expose
        private String image1;
        @SerializedName("image_2")
        @Expose
        private String image2;
        @SerializedName("image_3")
        @Expose
        private String image3;
        @SerializedName("country_code")
        @Expose
        private String countryCode;
        @SerializedName("phone")
        @Expose
        private String phone;
        @SerializedName("merchant_id")
        @Expose
        private String merchantId;
        @SerializedName("category_id")
        @Expose
        private String categoryId;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("country")
        @Expose
        private String country;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("city")
        @Expose
        private String city;

        @SerializedName("currency")
        @Expose
        private String currency;

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("discount")
        @Expose
        private String discount;
        @SerializedName("available_seat")
        @Expose
        private String availableSeat;
        @SerializedName("description")
        @Expose
        private String description;
        @SerializedName("open_time")
        @Expose
        private String openTime;
        @SerializedName("close_time")
        @Expose
        private String closeTime;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("res_status")
        @Expose
        private String resStatus;
        @SerializedName("delivered_time")
        @Expose
        private String deliveredTime;
        @SerializedName("rating")
        @Expose
        private String rating;
        @SerializedName("payment_accept")
        @Expose
        private String paymentAccept;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("onlinestore")
        @Expose
        private String onlinestore;
        @SerializedName("phonenumber")
        @Expose
        private String phonenumber;
        @SerializedName("mobileaccount")
        @Expose
        private String mobileaccount;
        @SerializedName("neighbourhood")
        @Expose
        private String neighbourhood;
        @SerializedName("days")
        @Expose
        private String days;
        @SerializedName("op_time")
        @Expose
        private String opTime;
        @SerializedName("cl_time")
        @Expose
        private String clTime;
        @SerializedName("holidays_date")
        @Expose
        private String holidaysDate;
        @SerializedName("open_tim")
        @Expose
        private List<OpenTim> openTim;
        @SerializedName("close_tim")
        @Expose
        private List<CloseTim> closeTim;
        @SerializedName("dayle")
        @Expose
        private List<Dayle> dayle;

        @SerializedName("plan_count")
        @Expose
        private String planCount;

        @SerializedName("product_qut")
        @Expose
        private String productQut;

        @SerializedName("add_product_count")
        @Expose
        private String addProductCount;

        @SerializedName("plan_id")
        @Expose
        private String planId;

        @SerializedName("shop_id")
        @Expose
        private String shopId;

        @SerializedName("shop_status")
        @Expose
        private String shopStatus;

        @SerializedName("street_landmark")
        @Expose
        private String streetLandmark;

        public String getStreetLandmark() {
            return streetLandmark;
        }

        public void setStreetLandmark(String streetLandmark) {
            this.streetLandmark = streetLandmark;
        }

        public String getShopStatus() {
            return shopStatus;
        }

        public void setShopStatus(String shopStatus) {
            this.shopStatus = shopStatus;
        }

        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getPlanId() {
            return planId;
        }

        public void setPlanId(String planId) {
            this.planId = planId;
        }

        public String getProductQut() {
            return productQut;
        }

        public void setProductQut(String productQut) {
            this.productQut = productQut;
        }

        public String getAddProductCount() {
            return addProductCount;
        }

        public void setAddProductCount(String addProductCount) {
            this.addProductCount = addProductCount;
        }

        public String getPlanCount() {
            return planCount;
        }

        public void setPlanCount(String planCount) {
            this.planCount = planCount;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSubAdminId() {
            return subAdminId;
        }

        public void setSubAdminId(String subAdminId) {
            this.subAdminId = subAdminId;
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

        public String getImage1() {
            return image1;
        }

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        public String getImage2() {
            return image2;
        }

        public void setImage2(String image2) {
            this.image2 = image2;
        }

        public String getImage3() {
            return image3;
        }

        public void setImage3(String image3) {
            this.image3 = image3;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getMerchantId() {
            return merchantId;
        }

        public void setMerchantId(String merchantId) {
            this.merchantId = merchantId;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getDiscount() {
            return discount;
        }

        public void setDiscount(String discount) {
            this.discount = discount;
        }

        public String getAvailableSeat() {
            return availableSeat;
        }

        public void setAvailableSeat(String availableSeat) {
            this.availableSeat = availableSeat;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getOpenTime() {
            return openTime;
        }

        public void setOpenTime(String openTime) {
            this.openTime = openTime;
        }

        public String getCloseTime() {
            return closeTime;
        }

        public void setCloseTime(String closeTime) {
            this.closeTime = closeTime;
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

        public String getResStatus() {
            return resStatus;
        }

        public void setResStatus(String resStatus) {
            this.resStatus = resStatus;
        }

        public String getDeliveredTime() {
            return deliveredTime;
        }

        public void setDeliveredTime(String deliveredTime) {
            this.deliveredTime = deliveredTime;
        }

        public String getRating() {
            return rating;
        }

        public void setRating(String rating) {
            this.rating = rating;
        }

        public String getPaymentAccept() {
            return paymentAccept;
        }

        public void setPaymentAccept(String paymentAccept) {
            this.paymentAccept = paymentAccept;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getOnlinestore() {
            return onlinestore;
        }

        public void setOnlinestore(String onlinestore) {
            this.onlinestore = onlinestore;
        }

        public String getPhonenumber() {
            return phonenumber;
        }

        public void setPhonenumber(String phonenumber) {
            this.phonenumber = phonenumber;
        }

        public String getMobileaccount() {
            return mobileaccount;
        }

        public void setMobileaccount(String mobileaccount) {
            this.mobileaccount = mobileaccount;
        }

        public String getNeighbourhood() {
            return neighbourhood;
        }

        public void setNeighbourhood(String neighbourhood) {
            this.neighbourhood = neighbourhood;
        }

        public String getDays() {
            return days;
        }

        public void setDays(String days) {
            this.days = days;
        }

        public String getOpTime() {
            return opTime;
        }

        public void setOpTime(String opTime) {
            this.opTime = opTime;
        }

        public String getClTime() {
            return clTime;
        }

        public void setClTime(String clTime) {
            this.clTime = clTime;
        }

        public String getHolidaysDate() {
            return holidaysDate;
        }

        public void setHolidaysDate(String holidaysDate) {
            this.holidaysDate = holidaysDate;
        }

        public List<OpenTim> getOpenTim() {
            return openTim;
        }

        public void setOpenTim(List<OpenTim> openTim) {
            this.openTim = openTim;
        }

        public List<CloseTim> getCloseTim() {
            return closeTim;
        }

        public void setCloseTim(List<CloseTim> closeTim) {
            this.closeTim = closeTim;
        }

        public List<Dayle> getDayle() {
            return dayle;
        }

        public void setDayle(List<Dayle> dayle) {
            this.dayle = dayle;
        }


        public class CloseTim implements Serializable{

            @SerializedName("week_name")
            @Expose
            private String weekName;
            @SerializedName("close_time")
            @Expose
            private String closeTime;

            public String getWeekName() {
                return weekName;
            }

            public void setWeekName(String weekName) {
                this.weekName = weekName;
            }

            public String getCloseTime() {
                return closeTime;
            }

            public void setCloseTime(String closeTime) {
                this.closeTime = closeTime;
            }

        }

        public class Dayle implements Serializable{

            @SerializedName("week_name")
            @Expose
            private String weekName;

            public String getWeekName() {
                return weekName;
            }

            public void setWeekName(String weekName) {
                this.weekName = weekName;
            }

        }

        public class OpenTim implements Serializable{

            @SerializedName("week_name")
            @Expose
            private String weekName;
            @SerializedName("open_time")
            @Expose
            private String openTime;

            public String getWeekName() {
                return weekName;
            }

            public void setWeekName(String weekName) {
                this.weekName = weekName;
            }

            public String getOpenTime() {
                return openTime;
            }

            public void setOpenTime(String openTime) {
                this.openTime = openTime;
            }

        }

    }


}