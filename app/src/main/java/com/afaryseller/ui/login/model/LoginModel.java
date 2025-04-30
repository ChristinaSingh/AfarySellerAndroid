package com.afaryseller.ui.login.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LoginModel {

    @SerializedName("result")
    @Expose
    private Result result;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("status")
    @Expose
    private String status;

    @SerializedName("country_code")
    @Expose
    private String countryCode;

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;

    }


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

        @SerializedName("id")
        @Expose
        private String id;
        @SerializedName("user_name")
        @Expose
        private String userName;
        @SerializedName("name")
        @Expose
        private String name;
        @SerializedName("last_name")
        @Expose
        private String lastName;
        @SerializedName("mobile")
        @Expose
        private String mobile;
        @SerializedName("country_code")
        @Expose
        private String countryCode;

        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("password")
        @Expose
        private String password;
        @SerializedName("type")
        @Expose
        private String type;
        @SerializedName("type_new")
        @Expose
        private String typeNew;
        @SerializedName("business_type")
        @Expose
        private String businessType;
        @SerializedName("industry")
        @Expose
        private String industry;
        @SerializedName("gstin_number")
        @Expose
        private String gstinNumber;
        @SerializedName("business_name")
        @Expose
        private String businessName;
        @SerializedName("business_address_1")
        @Expose
        private String businessAddress1;
        @SerializedName("business_address_2")
        @Expose
        private String businessAddress2;
        @SerializedName("pincode")
        @Expose
        private String pincode;
        @SerializedName("city")
        @Expose
        private String city;
        @SerializedName("state")
        @Expose
        private String state;
        @SerializedName("countries")
        @Expose
        private String countries;
        @SerializedName("status")
        @Expose
        private String status;
        @SerializedName("wallet")
        @Expose
        private String wallet;
        @SerializedName("travel_ticket")
        @Expose
        private String travelTicket;
        @SerializedName("real_estate")
        @Expose
        private String realEstate;
        @SerializedName("home_services")
        @Expose
        private String homeServices;
        @SerializedName("image")
        @Expose
        private String image;
        @SerializedName("document_image_1")
        @Expose
        private String documentImage1;
        @SerializedName("document_image_2")
        @Expose
        private String documentImage2;
        @SerializedName("otp")
        @Expose
        private String otp;
        @SerializedName("zip_code")
        @Expose
        private String zipCode;
        @SerializedName("social_id")
        @Expose
        private String socialId;

        @SerializedName("seller_register_id")
        @Expose
        private String registerId;

        @SerializedName("password_request_status")
        @Expose
        private String passwordRequestStatus;


        @SerializedName("sub_seller_id")
        @Expose
        private String sub_seller_id;

        @SerializedName("shop_id")
        @Expose
        private String shopId;


        public String getShopId() {
            return shopId;
        }

        public void setShopId(String shopId) {
            this.shopId = shopId;
        }

        public String getSub_seller_id() {
            return sub_seller_id;
        }

        public void setSub_seller_id(String sub_seller_id) {
            this.sub_seller_id = sub_seller_id;
        }

        public String getPasswordRequestStatus() {
            return passwordRequestStatus;
        }

        public void setPasswordRequestStatus(String passwordRequestStatus) {
            this.passwordRequestStatus = passwordRequestStatus;
        }


/*
        @SerializedName("register_id")
        @Expose
        private String registerId;
*/

        @SerializedName("date_time")
        @Expose
        private String dateTime;
        @SerializedName("lat")
        @Expose
        private String lat;
        @SerializedName("lon")
        @Expose
        private String lon;
        @SerializedName("ios_register_id")
        @Expose
        private Object iosRegisterId;
        @SerializedName("address")
        @Expose
        private String address;
        @SerializedName("language")
        @Expose
        private String language;
        @SerializedName("notif")
        @Expose
        private String notif;
        @SerializedName("currency")
        @Expose
        private String currency;
        @SerializedName("step")
        @Expose
        private String step;

        @SerializedName("access_token")
        @Expose
        private String accessToken;


        @SerializedName("city_name")
        @Expose
        private String cityName;

        @SerializedName("state_name")
        @Expose
        private String stateName;

        @SerializedName("country_name")
        @Expose
        private String countryName;

        @SerializedName("country")
        @Expose
        private String country;


        @SerializedName("sub_seller_name")
        @Expose
        private String subSellerName;

        public String getSubSellerName() {
            return subSellerName;
        }

        public void setSubSellerName(String subSellerName) {
            this.subSellerName = subSellerName;
        }

        public String getCountryCode() {
            return countryCode;
        }

        public void setCountryCode(String countryCode) {
            this.countryCode = countryCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getCityName() {
            return cityName;
        }

        public void setCityName(String cityName) {
            this.cityName = cityName;
        }

        public String getStateName() {
            return stateName;
        }

        public void setStateName(String stateName) {
            this.stateName = stateName;
        }

        public String getCountryName() {
            return countryName;
        }

        public void setCountryName(String countryName) {
            this.countryName = countryName;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getTypeNew() {
            return typeNew;
        }

        public void setTypeNew(String typeNew) {
            this.typeNew = typeNew;
        }

        public String getBusinessType() {
            return businessType;
        }

        public void setBusinessType(String businessType) {
            this.businessType = businessType;
        }

        public String getIndustry() {
            return industry;
        }

        public void setIndustry(String industry) {
            this.industry = industry;
        }

        public String getGstinNumber() {
            return gstinNumber;
        }

        public void setGstinNumber(String gstinNumber) {
            this.gstinNumber = gstinNumber;
        }

        public String getBusinessName() {
            return businessName;
        }

        public void setBusinessName(String businessName) {
            this.businessName = businessName;
        }

        public String getBusinessAddress1() {
            return businessAddress1;
        }

        public void setBusinessAddress1(String businessAddress1) {
            this.businessAddress1 = businessAddress1;
        }

        public String getBusinessAddress2() {
            return businessAddress2;
        }

        public void setBusinessAddress2(String businessAddress2) {
            this.businessAddress2 = businessAddress2;
        }

        public String getPincode() {
            return pincode;
        }

        public void setPincode(String pincode) {
            this.pincode = pincode;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCountries() {
            return countries;
        }

        public void setCountries(String countries) {
            this.countries = countries;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWallet() {
            return wallet;
        }

        public void setWallet(String wallet) {
            this.wallet = wallet;
        }

        public String getTravelTicket() {
            return travelTicket;
        }

        public void setTravelTicket(String travelTicket) {
            this.travelTicket = travelTicket;
        }

        public String getRealEstate() {
            return realEstate;
        }

        public void setRealEstate(String realEstate) {
            this.realEstate = realEstate;
        }

        public String getHomeServices() {
            return homeServices;
        }

        public void setHomeServices(String homeServices) {
            this.homeServices = homeServices;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getDocumentImage1() {
            return documentImage1;
        }

        public void setDocumentImage1(String documentImage1) {
            this.documentImage1 = documentImage1;
        }

        public String getDocumentImage2() {
            return documentImage2;
        }

        public void setDocumentImage2(String documentImage2) {
            this.documentImage2 = documentImage2;
        }

        public String getOtp() {
            return otp;
        }

        public void setOtp(String otp) {
            this.otp = otp;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public String getSocialId() {
            return socialId;
        }

        public void setSocialId(String socialId) {
            this.socialId = socialId;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
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

        public Object getIosRegisterId() {
            return iosRegisterId;
        }

        public void setIosRegisterId(Object iosRegisterId) {
            this.iosRegisterId = iosRegisterId;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getNotif() {
            return notif;
        }

        public void setNotif(String notif) {
            this.notif = notif;
        }

        public String getCurrency() {
            return currency;
        }

        public void setCurrency(String currency) {
            this.currency = currency;
        }

        public String getStep() {
            return step;
        }

        public void setStep(String step) {
            this.step = step;
        }


}



}