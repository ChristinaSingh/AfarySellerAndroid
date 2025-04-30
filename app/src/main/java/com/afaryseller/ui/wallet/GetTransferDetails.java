package com.afaryseller.ui.wallet;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GetTransferDetails {

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
        @SerializedName("user_id")
        @Expose
        private String userId;
        @SerializedName("interfaceid")
        @Expose
        private String interfaceid;
        @SerializedName("reference")
        @Expose
        private String reference;
        @SerializedName("afary_code")
        @Expose
        private String afaryCode;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("statut")
        @Expose
        private String statut;
        @SerializedName("operateur")
        @Expose
        private String operateur;
        @SerializedName("client")
        @Expose
        private String client;
        @SerializedName("message")
        @Expose
        private String message;
        @SerializedName("token")
        @Expose
        private String token;
        @SerializedName("agent")
        @Expose
        private String agent;
        @SerializedName("amount")
        @Expose
        private String amount;
        @SerializedName("order_id")
        @Expose
        private String orderId;
        @SerializedName("transaction_type")
        @Expose
        private String transactionType;
        @SerializedName("date_time")
        @Expose
        private String dateTime;

        @SerializedName("datetime")
        @Expose
        private String datetime;

        @SerializedName("transaction_by")
        @Expose
        private String transactionBy;

        @SerializedName("transaction_id")
        @Expose
        private String transactionId;

        @SerializedName("reference_info")
        @Expose
        private String referenceInfo;


        @SerializedName("wallet_balance")
        @Expose
        private String walletBalance;


        @SerializedName("local_currency")
        @Expose
        public String localCurrency;

        @SerializedName("local_price")
        @Expose
        public String localPrice;

        @SerializedName("wallet_local_currency")
        @Expose
        public String walletLocalCurrency;

        @SerializedName("wallet_local_price")
        @Expose
        public String walletLocalPrice;


        public String getLocalCurrency() {
            return localCurrency;
        }

        public void setLocalCurrency(String localCurrency) {
            this.localCurrency = localCurrency;
        }

        public String getLocalPrice() {
            return localPrice;
        }

        public void setLocalPrice(String localPrice) {
            this.localPrice = localPrice;
        }

        public String getWalletLocalCurrency() {
            return walletLocalCurrency;
        }

        public void setWalletLocalCurrency(String walletLocalCurrency) {
            this.walletLocalCurrency = walletLocalCurrency;
        }

        public String getWalletLocalPrice() {
            return walletLocalPrice;
        }

        public void setWalletLocalPrice(String walletLocalPrice) {
            this.walletLocalPrice = walletLocalPrice;
        }

        public String getWalletBalance() {
            return walletBalance;
        }

        public void setWalletBalance(String walletBalance) {
            this.walletBalance = walletBalance;
        }

        public String getReferenceInfo() {
            return referenceInfo;
        }

        public void setReferenceInfo(String referenceInfo) {
            this.referenceInfo = referenceInfo;
        }

        @SerializedName("chk")
        @Expose
        private boolean chk=false;


        public String getTransactionId() {
            return transactionId;
        }

        public void setTransactionId(String transactionId) {
            this.transactionId = transactionId;
        }

        public boolean isChk() {
            return chk;
        }

        public void setChk(boolean chk) {
            this.chk = chk;
        }

        public String getDatetime() {
            return datetime;
        }

        public void setDatetime(String datetime) {
            this.datetime = datetime;
        }

        public String getTransactionBy() {
            return transactionBy;
        }

        public void setTransactionBy(String transactionBy) {
            this.transactionBy = transactionBy;
        }

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

        public String getInterfaceid() {
            return interfaceid;
        }

        public void setInterfaceid(String interfaceid) {
            this.interfaceid = interfaceid;
        }

        public String getReference() {
            return reference;
        }

        public void setReference(String reference) {
            this.reference = reference;
        }

        public String getAfaryCode() {
            return afaryCode;
        }

        public void setAfaryCode(String afaryCode) {
            this.afaryCode = afaryCode;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getStatut() {
            return statut;
        }

        public void setStatut(String statut) {
            this.statut = statut;
        }

        public String getOperateur() {
            return operateur;
        }

        public void setOperateur(String operateur) {
            this.operateur = operateur;
        }

        public String getClient() {
            return client;
        }

        public void setClient(String client) {
            this.client = client;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getAgent() {
            return agent;
        }

        public void setAgent(String agent) {
            this.agent = agent;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getOrderId() {
            return orderId;
        }

        public void setOrderId(String orderId) {
            this.orderId = orderId;
        }

        public String getTransactionType() {
            return transactionType;
        }

        public void setTransactionType(String transactionType) {
            this.transactionType = transactionType;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}




