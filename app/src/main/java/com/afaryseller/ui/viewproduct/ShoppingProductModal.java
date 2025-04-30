package com.afaryseller.ui.viewproduct;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ShoppingProductModal {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("reviews")
    @Expose
    public List<Object> reviews = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public List<Object> getReviews() {
        return reviews;
    }

    public void setReviews(List<Object> reviews) {
        this.reviews = reviews;
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

        @SerializedName("image")
        @Expose
        public String image;

        @SerializedName("image_1")
        @Expose
        public String image1;

        public String getImage1() {
            return image1;
        }

        @SerializedName("product_images")
        @Expose
        private String productImages;

        @SerializedName("image_2")
        @Expose
        private String image2;
        @SerializedName("image_3")
        @Expose
        private String image3;

        public String getProductImages() {
            return productImages;
        }

        public void setProductImages(String productImages) {
            this.productImages = productImages;
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

        public void setImage1(String image1) {
            this.image1 = image1;
        }

        @SerializedName("pro_id")
        @Expose
        public String proId;

        @SerializedName("seller_id")
        @Expose
        public String sellerId;

        @SerializedName("restaurant_id")
        @Expose
        public String restaurantId;
        @SerializedName("product_name")
        @Expose
        public String productName;
        @SerializedName("product_price")
        @Expose
        public String productPrice;
        @SerializedName("product_details")
        @Expose
        public String productDetails;

        @SerializedName("delivery_charges")
        @Expose
        public String deliveryCharges;

        @SerializedName("ship_by")
        @Expose
        public String shipBy;

        @SerializedName("in_stock")
        @Expose
        public String inStock;

        @SerializedName("seller_name")
        @Expose
        public String sellerName;


        @SerializedName("shop_name")
        @Expose
        public String shopName;

        @SerializedName("verify")
        @Expose
        public String verify;

        @SerializedName("shop_image")
        @Expose
        public String shopImage;

        @SerializedName("addedtowishlist")
        @Expose
        public String addedtowishlist;


        @SerializedName("avg_rating")
        @Expose
        public String avgRating;

        @SerializedName("Product_brand")
        @Expose
        public String productBrand;

        @SerializedName("validate_name")
        @Expose
        private List<ValidateName> validateName;


        @SerializedName("review")
        @Expose
        private List<review> review;

        public List<Result.review> getReview() {
            return review;
        }

        public void setReview(List<Result.review> review) {
            this.review = review;
        }

        public List<ValidateName> getValidateName() {
            return validateName;
        }

        public void setValidateName(List<ValidateName> validateName) {
            this.validateName = validateName;
        }

        public String getAddedtowishlist() {
            return addedtowishlist;
        }

        public void setAddedtowishlist(String addedtowishlist) {
            this.addedtowishlist = addedtowishlist;
        }

        public String getSellerId() {
            return sellerId;
        }

        public void setSellerId(String sellerId) {
            this.sellerId = sellerId;
        }

        public String getShopName() {
            return shopName;
        }

        public void setShopName(String shopName) {
            this.shopName = shopName;
        }

        public String getVerify() {
            return verify;
        }

        public void setVerify(String verify) {
            this.verify = verify;
        }

        public String getShopImage() {
            return shopImage;
        }

        public void setShopImage(String shopImage) {
            this.shopImage = shopImage;
        }

        public String getShipBy() {
            return shipBy;
        }

        public void setShipBy(String shipBy) {
            this.shipBy = shipBy;
        }

        public String getInStock() {
            return inStock;
        }

        public void setInStock(String inStock) {
            this.inStock = inStock;
        }

        public String getSellerName() {
            return sellerName;
        }

        public void setSellerName(String sellerName) {
            this.sellerName = sellerName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getProId() {
            return proId;
        }

        public void setProId(String proId) {
            this.proId = proId;
        }

        public String getRestaurantId() {
            return restaurantId;
        }

        public void setRestaurantId(String restaurantId) {
            this.restaurantId = restaurantId;
        }

        public String getProductName() {
            return productName;
        }

        public void setProductName(String productName) {
            this.productName = productName;
        }

        public String getProductPrice() {
            return productPrice;
        }

        public void setProductPrice(String productPrice) {
            this.productPrice = productPrice;
        }

        public String getProductDetails() {
            return productDetails;
        }

        public void setProductDetails(String productDetails) {
            this.productDetails = productDetails;
        }

        public String getDeliveryCharges() {
            return deliveryCharges;
        }

        public void setDeliveryCharges(String deliveryCharges) {
            this.deliveryCharges = deliveryCharges;
        }

        public String getProductBrand() {
            return productBrand;
        }

        public void setProductBrand(String productBrand) {
            this.productBrand = productBrand;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        public class review {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("product_id")
            @Expose
            private String productId;
            @SerializedName("product_rating")
            @Expose
            private String productRating;
            @SerializedName("product_review")
            @Expose
            private String productReview;
            @SerializedName("user_name")
            @Expose
            private String userName;
            @SerializedName("image")
            @Expose
            private String image;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
            }

            public String getProductRating() {
                return productRating;
            }

            public void setProductRating(String productRating) {
                this.productRating = productRating;
            }

            public String getProductReview() {
                return productReview;
            }

            public void setProductReview(String productReview) {
                this.productReview = productReview;
            }

            public String getUserName() {
                return userName;
            }

            public void setUserName(String userName) {
                this.userName = userName;
            }

            public String getImage() {
                return image;
            }

            public void setImage(String image) {
                this.image = image;
            }
        }








        public class ValidateName {

            @SerializedName("id")
            @Expose
            private String id;
            @SerializedName("product_id")
            @Expose
            private String productId;
            @SerializedName("shop_id")
            @Expose
            private String shopId;
            @SerializedName("seller_id")
            @Expose
            private String sellerId;
            @SerializedName("name")
            @Expose
            private String name;
            @SerializedName("date_time")
            @Expose
            private String dateTime;

            @SerializedName("checked")
            @Expose
            private boolean checked=false;
            @SerializedName("attribute_name")
            @Expose
            private List<AttributeName> attributeName;


            public boolean isChecked() {
                return checked;
            }

            public void setChecked(boolean checked) {
                this.checked = checked;
            }

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getProductId() {
                return productId;
            }

            public void setProductId(String productId) {
                this.productId = productId;
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

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getDateTime() {
                return dateTime;
            }

            public void setDateTime(String dateTime) {
                this.dateTime = dateTime;
            }

            public List<AttributeName> getAttributeName() {
                return attributeName;
            }

            public void setAttributeName(List<AttributeName> attributeName) {
                this.attributeName = attributeName;
            }


            public class AttributeName {

                @SerializedName("id")
                @Expose
                private String id;
                @SerializedName("product_id")
                @Expose
                private String productId;
                @SerializedName("seller_id")
                @Expose
                private String sellerId;
                @SerializedName("validate_id")
                @Expose
                private String validateId;
                @SerializedName("name")
                @Expose
                private String name;
                @SerializedName("date_time")
                @Expose
                private String dateTime;

                @SerializedName("chk")
                @Expose
                private boolean chk = false;

                public boolean isChk() {
                    return chk;
                }

                public void setChk(boolean chk) {
                    this.chk = chk;
                }

                public String getId() {
                    return id;
                }

                public void setId(String id) {
                    this.id = id;
                }

                public String getProductId() {
                    return productId;
                }

                public void setProductId(String productId) {
                    this.productId = productId;
                }

                public String getSellerId() {
                    return sellerId;
                }

                public void setSellerId(String sellerId) {
                    this.sellerId = sellerId;
                }

                public String getValidateId() {
                    return validateId;
                }

                public void setValidateId(String validateId) {
                    this.validateId = validateId;
                }

                public String getName() {
                    return name;
                }

                public void setName(String name) {
                    this.name = name;
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
}
