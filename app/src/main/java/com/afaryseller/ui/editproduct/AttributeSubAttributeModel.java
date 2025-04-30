package com.afaryseller.ui.editproduct;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



import java.util.List;


public class AttributeSubAttributeModel {

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
        @SerializedName("delete_status")
        @Expose
        private String deleteStatus;
        @SerializedName("checked_status")
        @Expose
        private String checkedStatus;
        @SerializedName("status")
        @Expose
        private Boolean status;
        @SerializedName("attributes")
        @Expose
        private List<Attribute> attributes;

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

        public String getDeleteStatus() {
            return deleteStatus;
        }

        public void setDeleteStatus(String deleteStatus) {
            this.deleteStatus = deleteStatus;
        }

        public String getCheckedStatus() {
            return checkedStatus;
        }

        public void setCheckedStatus(String checkedStatus) {
            this.checkedStatus = checkedStatus;
        }

        public Boolean getStatus() {
            return status;
        }

        public void setStatus(Boolean status) {
            this.status = status;
        }

        public List<Attribute> getAttributes() {
            return attributes;
        }

        public void setAttributes(List<Attribute> attributes) {
            this.attributes = attributes;
        }

        public class Attribute {

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
            @SerializedName("delete_status")
            @Expose
            private String deleteStatus;
            @SerializedName("checked_status")
            @Expose
            private String checkedStatus;

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

            public String getDeleteStatus() {
                return deleteStatus;
            }

            public void setDeleteStatus(String deleteStatus) {
                this.deleteStatus = deleteStatus;
            }

            public String getCheckedStatus() {
                return checkedStatus;
            }

            public void setCheckedStatus(String checkedStatus) {
                this.checkedStatus = checkedStatus;
            }

        }


    }

}

