package com.afaryseller.ui.subscription.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;


public class SubscriptionModel {

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
        @SerializedName("plan_name")
        @Expose
        private String planName;
        @SerializedName("price")
        @Expose
        private String price;
        @SerializedName("valid_days")
        @Expose
        private String validDays;
        @SerializedName("features1")
        @Expose
        private String features1;
        @SerializedName("features2")
        @Expose
        private String features2;
        @SerializedName("features3")
        @Expose
        private String features3;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("symbol")
        @Expose
        private String symbol;

        @SerializedName("currency_name")
        @Expose
        private String currencyName;

        @SerializedName("currency_code")
        @Expose
        private String currencyCode;

        @SerializedName("plan_type")
        @Expose
        private String planType;

        @SerializedName("image_limit")
        @Expose
        private String imageLimit;


        public String getImageLimit() {
            return imageLimit;
        }

        public void setImageLimit(String imageLimit) {
            this.imageLimit = imageLimit;
        }

        public String getPlanType() {
            return planType;
        }

        public void setPlanType(String planType) {
            this.planType = planType;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPlanName() {
            return planName;
        }

        public void setPlanName(String planName) {
            this.planName = planName;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getValidDays() {
            return validDays;
        }

        public void setValidDays(String validDays) {
            this.validDays = validDays;
        }

        public String getFeatures1() {
            return features1;
        }

        public void setFeatures1(String features1) {
            this.features1 = features1;
        }

        public String getFeatures2() {
            return features2;
        }

        public void setFeatures2(String features2) {
            this.features2 = features2;
        }

        public String getFeatures3() {
            return features3;
        }

        public void setFeatures3(String features3) {
            this.features3 = features3;
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

        public String getSymbol() {
            return symbol;
        }

        public void setSymbol(String symbol) {
            this.symbol = symbol;
        }

        public String getCurrencyName() {
            return currencyName;
        }

        public void setCurrencyName(String currencyName) {
            this.currencyName = currencyName;
        }

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
        }
    }


}