package com.afaryseller.ui.shopdetails.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


public class ShopDetailModel implements Serializable{

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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

        @SerializedName("country_name")
        @Expose
        private String country_name;
        @SerializedName("state_name")
        @Expose
        private String state_name;
        @SerializedName("city_name")
        @Expose
        private String city_name;

        public String getCountry_name() {
            return country_name;
        }

        public void setCountry_name(String country_name) {
            this.country_name = country_name;
        }

        public String getState_name() {
            return state_name;
        }

        public void setState_name(String state_name) {
            this.state_name = state_name;
        }

        public String getCity_name() {
            return city_name;
        }

        public void setCity_name(String city_name) {
            this.city_name = city_name;
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
        @SerializedName("product")
        @Expose
        private List<Product> product;
        @SerializedName("product_images")
        @Expose
        private String productImages;

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

        public List<Product> getProduct() {
            return product;
        }

        public void setProduct(List<Product> product) {
            this.product = product;
        }

        public String getProductImages() {
            return productImages;
        }

        public void setProductImages(String productImages) {
            this.productImages = productImages;
        }


        public class Product implements Serializable {

            @SerializedName("pro_id")
            @Expose
            private String proId;
            @SerializedName("category_id")
            @Expose
            private String categoryId;
            @SerializedName("category_name")
            @Expose
            private String categoryName;

            public String getCategoryName() {
                return categoryName;
            }

            public void setCategoryName(String categoryName) {
                this.categoryName = categoryName;
            }

            @SerializedName("sub_cat")
            @Expose
            private String subCat;
            @SerializedName("restaurant_id")
            @Expose
            private String restaurantId;
            @SerializedName("seller_id")
            @Expose
            private String sellerId;
            @SerializedName("product_name")
            @Expose
            private String productName;
            @SerializedName("product_images")
            @Expose
            private String productImages;
            @SerializedName("image_1")
            @Expose
            private String image1;
            @SerializedName("image_2")
            @Expose
            private String image2;
            @SerializedName("image_3")
            @Expose
            private String image3;
            @SerializedName("product_details")
            @Expose
            private String productDetails;
            @SerializedName("addtional_detial")
            @Expose
            private String addtionalDetial;
            @SerializedName("Product_brand")
            @Expose
            private String productBrand;
            @SerializedName("product_qut")
            @Expose
            private String productQut;
            @SerializedName("product_price")
            @Expose
            private String productPrice;
            @SerializedName("pro_off")
            @Expose
            private String proOff;
            @SerializedName("interested")
            @Expose
            private String interested;
            @SerializedName("reviews")
            @Expose
            private String reviews;
            @SerializedName("sku")
            @Expose
            private String sku;
            @SerializedName("size")
            @Expose
            private String size;
            @SerializedName("stock")
            @Expose
            private String stock;
            @SerializedName("tax_n1")
            @Expose
            private String taxN1;
            @SerializedName("tax_n2")
            @Expose
            private String taxN2;
            @SerializedName("product_color")
            @Expose
            private String productColor;
            @SerializedName("status")
            @Expose
            private String status;
            @SerializedName("national_international")
            @Expose
            private String nationalInternational;
            @SerializedName("delivery_charges")
            @Expose
            private String deliveryCharges;
            @SerializedName("in_stock")
            @Expose
            private String inStock;


            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("cat_new")
            @Expose
            private String catNew;

            @SerializedName("approval")
            @Expose
            private String approval;

            @SerializedName("verify")
            @Expose
            public String verify;

            @SerializedName("seller_name")
            @Expose
            public String sellerName;

            public String getVerify() {
                return verify;
            }

            public void setVerify(String verify) {
                this.verify = verify;
            }

            public String getSellerName() {
                return sellerName;
            }

            public void setSellerName(String sellerName) {
                this.sellerName = sellerName;
            }

            public String getInStock() {
                return inStock;
            }

            public void setInStock(String inStock) {
                this.inStock = inStock;
            }

            public String getApproval() {
                return approval;
            }

            public void setApproval(String approval) {
                this.approval = approval;
            }

            public String getProId() {
                return proId;
            }

            public void setProId(String proId) {
                this.proId = proId;
            }

            public String getCategoryId() {
                return categoryId;
            }

            public void setCategoryId(String categoryId) {
                this.categoryId = categoryId;
            }

            public String getSubCat() {
                return subCat;
            }

            public void setSubCat(String subCat) {
                this.subCat = subCat;
            }

            public String getRestaurantId() {
                return restaurantId;
            }

            public void setRestaurantId(String restaurantId) {
                this.restaurantId = restaurantId;
            }

            public String getSellerId() {
                return sellerId;
            }

            public void setSellerId(String sellerId) {
                this.sellerId = sellerId;
            }

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
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

            public String getProductDetails() {
                return productDetails;
            }

            public void setProductDetails(String productDetails) {
                this.productDetails = productDetails;
            }

            public String getAddtionalDetial() {
                return addtionalDetial;
            }

            public void setAddtionalDetial(String addtionalDetial) {
                this.addtionalDetial = addtionalDetial;
            }

            public String getProductBrand() {
                return productBrand;
            }

            public void setProductBrand(String productBrand) {
                this.productBrand = productBrand;
            }

            public String getProductQut() {
                return productQut;
            }

            public void setProductQut(String productQut) {
                this.productQut = productQut;
            }

            public String getProductPrice() {
                return productPrice;
            }

            public void setProductPrice(String productPrice) {
                this.productPrice = productPrice;
            }

            public String getProOff() {
                return proOff;
            }

            public void setProOff(String proOff) {
                this.proOff = proOff;
            }

            public String getInterested() {
                return interested;
            }

            public void setInterested(String interested) {
                this.interested = interested;
            }

            public String getReviews() {
                return reviews;
            }

            public void setReviews(String reviews) {
                this.reviews = reviews;
            }

            public String getSku() {
                return sku;
            }

            public void setSku(String sku) {
                this.sku = sku;
            }

            public String getSize() {
                return size;
            }

            public void setSize(String size) {
                this.size = size;
            }

            public String getStock() {
                return stock;
            }

            public void setStock(String stock) {
                this.stock = stock;
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

            public String getProductColor() {
                return productColor;
            }

            public void setProductColor(String productColor) {
                this.productColor = productColor;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }

            public String getNationalInternational() {
                return nationalInternational;
            }

            public void setNationalInternational(String nationalInternational) {
                this.nationalInternational = nationalInternational;
            }

            public String getDeliveryCharges() {
                return deliveryCharges;
            }

            public void setDeliveryCharges(String deliveryCharges) {
                this.deliveryCharges = deliveryCharges;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public String getCatNew() {
                return catNew;
            }

            public void setCatNew(String catNew) {
                this.catNew = catNew;
            }

        }

    }


}