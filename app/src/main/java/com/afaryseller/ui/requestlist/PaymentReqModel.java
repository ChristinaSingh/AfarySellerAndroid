package com.afaryseller.ui.requestlist;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PaymentReqModel {

    @SerializedName("result")
    @Expose
    private Boolean result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("data")
    @Expose
    private List<Datum> data;

    public Boolean getResult() {
        return result;
    }

    public void setResult(Boolean result) {
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

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public class Datum {

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("sender_id")
        @Expose
        private String senderId;
        @SerializedName("receiver_id")
        @Expose
        private String receiverId;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("to_country_code")
        @Expose
        private String toCountryCode;
        @SerializedName("to_mobile")
        @Expose
        private String toMobile;
        @SerializedName("request_reason")
        @Expose
        private String requestReason;
        @SerializedName("datetime")
        @Expose
        private String datetime;
        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("sender_username")
        @Expose
        private String senderUsername;
        @SerializedName("sender_fullname")
        @Expose
        private String senderFullname;
        @SerializedName("sender_mobile")
        @Expose
        private String senderMobile;
        @SerializedName("sender_email")
        @Expose
        private String senderEmail;
        @SerializedName("expire_status")
        @Expose
        private String expireStatus;


        @SerializedName("message_noti")
        @Expose
        private String message_noti;


        public String getMessage_noti() {
            return message_noti;
        }

        public void setMessage_noti(String message_noti) {
            this.message_noti = message_noti;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getToCountryCode() {
            return toCountryCode;
        }

        public void setToCountryCode(String toCountryCode) {
            this.toCountryCode = toCountryCode;
        }

        public String getToMobile() {
            return toMobile;
        }

        public void setToMobile(String toMobile) {
            this.toMobile = toMobile;
        }

        public String getRequestReason() {
            return requestReason;
        }

        public void setRequestReason(String requestReason) {
            this.requestReason = requestReason;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getSenderUsername() {
            return senderUsername;
        }

        public void setSenderUsername(String senderUsername) {
            this.senderUsername = senderUsername;
        }

        public String getSenderFullname() {
            return senderFullname;
        }

        public void setSenderFullname(String senderFullname) {
            this.senderFullname = senderFullname;
        }

        public String getSenderMobile() {
            return senderMobile;
        }

        public void setSenderMobile(String senderMobile) {
            this.senderMobile = senderMobile;
        }

        public String getSenderEmail() {
            return senderEmail;
        }

        public void setSenderEmail(String senderEmail) {
            this.senderEmail = senderEmail;
        }

        public String getExpireStatus() {
            return expireStatus;
        }

        public void setExpireStatus(String expireStatus) {
            this.expireStatus = expireStatus;
        }

    }

}

