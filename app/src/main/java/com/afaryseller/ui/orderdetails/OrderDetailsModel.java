/*
package com.afaryseller.ui.orderdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;



public class OrderDetailsModel {

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

    public class Result {

        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("images")
        @Expose
        private String images;
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
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("afary_code")
        @Expose
        private String afaryCode;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("shop_address")
        @Expose
        private String shopAddress;
        @SerializedName("shop_lat")
        @Expose
        private String shopLat;
        @SerializedName("shop_lon")
        @Expose
        private String shopLon;
        @SerializedName("total")
        @Expose
        private String total;
        @SerializedName("total_text")
        @Expose
        private String totalText;
        @SerializedName("shop_image")
        @Expose
        private List<ShopImage> shopImage;
        @SerializedName("product_list")
        @Expose
        private List<Product> productList;
        @SerializedName("delivery_address")
        @Expose
        private DeliveryAddress deliveryAddress;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getImages() {
            return images;
        }

        public void setImages(String images) {
            this.images = images;
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

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getShopAddress() {
            return shopAddress;
        }

        public void setShopAddress(String shopAddress) {
            this.shopAddress = shopAddress;
        }

        public String getShopLat() {
            return shopLat;
        }

        public void setShopLat(String shopLat) {
            this.shopLat = shopLat;
        }

        public String getShopLon() {
            return shopLon;
        }

        public void setShopLon(String shopLon) {
            this.shopLon = shopLon;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getTotalText() {
            return totalText;
        }

        public void setTotalText(String totalText) {
            this.totalText = totalText;
        }

        public List<ShopImage> getShopImage() {
            return shopImage;
        }

        public void setShopImage(List<ShopImage> shopImage) {
            this.shopImage = shopImage;
        }

        public List<Product> getProductList() {
            return productList;
        }

        public void setProductList(List<Product> productList) {
            this.productList = productList;
        }

        public DeliveryAddress getDeliveryAddress() {
            return deliveryAddress;
        }

        public void setDeliveryAddress(DeliveryAddress deliveryAddress) {
            this.deliveryAddress = deliveryAddress;
        }

        public class Product {

            @SerializedName("pro_id")
            @Expose
            private String proId;
            @SerializedName("cat_id")
            @Expose
            private String catId;
            @SerializedName("category_id")
            @Expose
            private String categoryId;
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
            @SerializedName("status_block")
            @Expose
            private String statusBlock;
            @SerializedName("block_resion")
            @Expose
            private String blockResion;
            @SerializedName("national_international")
            @Expose
            private String nationalInternational;
            @SerializedName("delivery_charges")
            @Expose
            private String deliveryCharges;
            @SerializedName("date")
            @Expose
            private String date;
            @SerializedName("cat_new")
            @Expose
            private String catNew;
            @SerializedName("new_qut")
            @Expose
            private String newQut;


            @SerializedName("quantity")
            @Expose
            private String quantity;


            @SerializedName("notif")
            @Expose
            private String notif;

            public String getQuantity() {
                return quantity;
            }

            public void setQuantity(String quantity) {
                this.quantity = quantity;
            }

            public String getProId() {
                return proId;
            }

            public void setProId(String proId) {
                this.proId = proId;
            }

            public String getCatId() {
                return catId;
            }

            public void setCatId(String catId) {
                this.catId = catId;
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

            public String getStatusBlock() {
                return statusBlock;
            }

            public void setStatusBlock(String statusBlock) {
                this.statusBlock = statusBlock;
            }

            public String getBlockResion() {
                return blockResion;
            }

            public void setBlockResion(String blockResion) {
                this.blockResion = blockResion;
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

            public String getNewQut() {
                return newQut;
            }




            public void setNewQut(String newQut) {
                this.newQut = newQut;
            }

            public String getNotif() {
                return notif;
            }

            public void setNotif(String notif) {
                this.notif = notif;
            }

        }

        public class ShopImage {

            @SerializedName("image")
            @Expose
            private String image;

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }

        }

        public class DeliveryAddress {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("user_id")
            @Expose
            private String userId;
            @SerializedName("address_name")
            @Expose
            private String addressName;
            @SerializedName("first_name")
            @Expose
            private String firstName;
            @SerializedName("last_name")
            @Expose
            private String lastName;
            @SerializedName("email")
            @Expose
            private String email;
            @SerializedName("phone")
            @Expose
            private String phone;
            @SerializedName("address")
            @Expose
            private String address;
            @SerializedName("country")
            @Expose
            private String country;
            @SerializedName("postcode")
            @Expose
            private String postcode;
            @SerializedName("city")
            @Expose
            private String city;
            @SerializedName("amount")
            @Expose
            private String amount;
            @SerializedName("lat")
            @Expose
            private String lat;
            @SerializedName("lon")
            @Expose
            private String lon;
            @SerializedName("pay_type")
            @Expose
            private String payType;
            @SerializedName("type")
            @Expose
            private String type;
            @SerializedName("selected_address")
            @Expose
            private String selectedAddress;
            @SerializedName("date_time")
            @Expose
            private String dateTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUserId() {
                return userId;
            }

            public void setUserId(String userId) {
                this.userId = userId;
            }

            public String getAddressName() {
                return addressName;
            }

            public void setAddressName(String addressName) {
                this.addressName = addressName;
            }

            public String getFirstName() {
                return firstName;
            }

            public void setFirstName(String firstName) {
                this.firstName = firstName;
            }

            public String getLastName() {
                return lastName;
            }

            public void setLastName(String lastName) {
                this.lastName = lastName;
            }

            public String getEmail() {
                return email;
            }

            public void setEmail(String email) {
                this.email = email;
            }

            public String getPhone() {
                return phone;
            }

            public void setPhone(String phone) {
                this.phone = phone;
            }

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getPostcode() {
                return postcode;
            }

            public void setPostcode(String postcode) {
                this.postcode = postcode;
            }

            public String getCity() {
                return city;
            }

            public void setCity(String city) {
                this.city = city;
            }

            public String getAmount() {
                return amount;
            }

            public void setAmount(String amount) {
                this.amount = amount;
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

            public String getPayType() {
                return payType;
            }

            public void setPayType(String payType) {
                this.payType = payType;
            }

            public String getType() {
                return type;
            }

            public void setType(String type) {
                this.type = type;
            }

            public String getSelectedAddress() {
                return selectedAddress;
            }

            public void setSelectedAddress(String selectedAddress) {
                this.selectedAddress = selectedAddress;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }


        }

    }


}





*/


package com.afaryseller.ui.orderdetails;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class OrderDetailsModel implements Serializable {

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
      /*  @SerializedName("price")
        @Expose
        private String price;*/

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

        @SerializedName("deliver_lat")
        @Expose
        private String deliverLat;


        @SerializedName("delivery_lon")
        @Expose
        private String deliverLon;

        @SerializedName("self_collect")
        @Expose
        private String selfCollect;

        @SerializedName("delivery_type")
        @Expose
        private String deliveryType;

        public String getDeliveryType() {
            return deliveryType;
        }

        public void setDeliveryType(String deliveryType) {
            this.deliveryType = deliveryType;
        }

        public String getSelfCollect() {
            return selfCollect;
        }

        public void setSelfCollect(String selfCollect) {
            this.selfCollect = selfCollect;
        }

        public String getDeliverLat() {
            return deliverLat;
        }

        public void setDeliverLat(String deliverLat) {
            this.deliverLat = deliverLat;
        }

        public String getDeliverLon() {
            return deliverLon;
        }

        public void setDeliverLon(String deliverLon) {
            this.deliverLon = deliverLon;
        }

      /*  public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }*/

        @SerializedName("product_list")
        @Expose

        private List<Product> productList;


        @SerializedName("delivery_person")
        @Expose
        private DeliveryPerson deliveryPerson;

        public DeliveryPerson getDeliveryPerson() {
            return deliveryPerson;
        }

        public void setDeliveryPerson(DeliveryPerson deliveryPerson) {
            this.deliveryPerson = deliveryPerson;
        }

        public class DeliveryPerson implements Serializable{

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("delivery_person_id")
            @Expose
            private String deliveryPersonId;
            @SerializedName("order_id")
            @Expose
            private String orderId;
            @SerializedName("afary_code")
            @Expose
            private String afaryCode;
            @SerializedName("delivery_person_name")
            @Expose
            private String deliveryPersonName;
            @SerializedName("delivery_person_image")
            @Expose
            private String deliveryPersonImage;
            @SerializedName("delivery_person_email")
            @Expose
            private String deliveryPersonEmail;
            @SerializedName("delivery_person_number")
            @Expose
            private String deliveryPersonNumber;
            @SerializedName("current_latitude")
            @Expose
            private String currentLatitude;
            @SerializedName("current_longitude")
            @Expose
            private String currentLongitude;
            @SerializedName("status")
            @Expose
            private String status;


            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDeliveryPersonId() {
                return deliveryPersonId;
            }

            public void setDeliveryPersonId(String deliveryPersonId) {
                this.deliveryPersonId = deliveryPersonId;
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

            public String getDeliveryPersonName() {
                return deliveryPersonName;
            }

            public void setDeliveryPersonName(String deliveryPersonName) {
                this.deliveryPersonName = deliveryPersonName;
            }

            public String getDeliveryPersonImage() {
                return deliveryPersonImage;
            }

            public void setDeliveryPersonImage(String deliveryPersonImage) {
                this.deliveryPersonImage = deliveryPersonImage;
            }

            public String getDeliveryPersonEmail() {
                return deliveryPersonEmail;
            }

            public void setDeliveryPersonEmail(String deliveryPersonEmail) {
                this.deliveryPersonEmail = deliveryPersonEmail;
            }

            public String getDeliveryPersonNumber() {
                return deliveryPersonNumber;
            }

            public void setDeliveryPersonNumber(String deliveryPersonNumber) {
                this.deliveryPersonNumber = deliveryPersonNumber;
            }

            public String getCurrentLatitude() {
                return currentLatitude;
            }

            public void setCurrentLatitude(String currentLatitude) {
                this.currentLatitude = currentLatitude;
            }

            public String getCurrentLongitude() {
                return currentLongitude;
            }

            public void setCurrentLongitude(String currentLongitude) {
                this.currentLongitude = currentLongitude;
            }

            public String getStatus() {
                return status;
            }

            public void setStatus(String status) {
                this.status = status;
            }
        }

        public class Product implements Serializable{

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
            @SerializedName("product_name")
            @Expose
            private String productName;
            @SerializedName("shop_image")
            @Expose
            private String shopImage;
            @SerializedName("shop_name")
            @Expose
            private String shopName;

            @SerializedName("shop_address")
            @Expose
            private String shopAddress;


            @SerializedName("shop_lat")
            @Expose
            private String shopLat;


            @SerializedName("shop_lon")
            @Expose
            private String shopLon;


            @SerializedName("options")
            @Expose
            private String option;


            @SerializedName("product_sku")
            @Expose
            private String productSku;

            @SerializedName("currency")
            @Expose
            private String currency;


            @SerializedName("return_to_seller_status")
            @Expose
            private String returnToSellerStatus;

            public String getReturnToSellerStatus() {
                return returnToSellerStatus;
            }

            public void setReturnToSellerStatus(String returnToSellerStatus) {
                this.returnToSellerStatus = returnToSellerStatus;
            }

            public String getCurrency() {
                return currency;
            }

            public void setCurrency(String currency) {
                this.currency = currency;
            }

            public String getOption() {
                return option;
            }

            public String getProductSku() {
                return productSku;
            }

            public void setProductSku(String productSku) {
                this.productSku = productSku;
            }

            public void setOption(String option) {
                this.option = option;
            }

            public String getShopAddress() {
                return shopAddress;
            }

            public void setShopAddress(String shopAddress) {
                this.shopAddress = shopAddress;
            }

            public String getShopLat() {
                return shopLat;
            }

            public void setShopLat(String shopLat) {
                this.shopLat = shopLat;
            }

            public String getShopLon() {
                return shopLon;
            }

            public void setShopLon(String shopLon) {
                this.shopLon = shopLon;
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

            public String getProductName() {
                return productName;
            }

            public void setProductName(String productName) {
                this.productName = productName;
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

    }


}




