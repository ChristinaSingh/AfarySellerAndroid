package com.afaryseller.ui.signup;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CountryModel {

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
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("active_deactive")
        @Expose
        private String activeDeactive;
        @SerializedName("active_deactive_delivery")
        @Expose
        private String activeDeactiveDelivery;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("currency_code")
        @Expose
        private String currencyCode;

        public String getCurrencyCode() {
            return currencyCode;
        }

        public void setCurrencyCode(String currencyCode) {
            this.currencyCode = currencyCode;
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

        public String getActiveDeactive() {
            return activeDeactive;
        }

        public void setActiveDeactive(String activeDeactive) {
            this.activeDeactive = activeDeactive;
        }

        public String getActiveDeactiveDelivery() {
            return activeDeactiveDelivery;
        }

        public void setActiveDeactiveDelivery(String activeDeactiveDelivery) {
            this.activeDeactiveDelivery = activeDeactiveDelivery;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}

