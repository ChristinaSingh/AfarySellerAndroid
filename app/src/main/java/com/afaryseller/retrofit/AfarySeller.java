package com.afaryseller.retrofit;

import com.afaryseller.ui.wallet.GetProfileModal;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface AfarySeller {


    @FormUrlEncoded
    @POST(ApiConstant.SIGNUP)
    Call<ResponseBody> sellerSignup(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(ApiConstant.SUB_SELLER_SIGNUP)
    Call<ResponseBody> subSellerSignup(
     @HeaderMap Map<String, String> auth,
                                       @Part("name") RequestBody name,
                                       @Part("username") RequestBody username,
                                       @Part("email") RequestBody email,
                                       @Part("password") RequestBody password,
                                       @Part("country_code") RequestBody countryCode,
                                       @Part("mobile") RequestBody mobile,
                                       @Part("parent_seller_id") RequestBody parent_seller_id,
                                       @Part("country") RequestBody country,
                                       @Part("state") RequestBody state,
                                       @Part("city") RequestBody city,
                                       @Part("address") RequestBody address,
                                       @Part("shop_id") RequestBody shopId,
                                       @Part MultipartBody.Part file);

    @FormUrlEncoded
    @POST(ApiConstant.SEND_OTP)
    Call<ResponseBody> sendOtpApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ApiConstant.SEND_WHATSAPP_OTP)
    Call<ResponseBody> sendWhatsappOtpApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ApiConstant.VERIFY_OTP)
    Call<ResponseBody> verifyOtpApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ApiConstant.SELLER_GET_ALL_SKILLS)
    Call<ResponseBody> getSkillApi(@FieldMap Map<String, String> paramHashMap);

    @FormUrlEncoded
    @POST(ApiConstant.SELLER_SKILLS)
    Call<ResponseBody> sellerAddSkillsApi(@FieldMap Map<String, String> paramHashMap);

    @Multipart
    @POST(ApiConstant.SELLER_UPDATE_DOCUMENT)
    Call<ResponseBody> sellerAddSkillsApi(
            @Part("user_id") RequestBody username,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2
    );


    @FormUrlEncoded
    @POST(ApiConstant.SELLER_LOGIN)
    Call<ResponseBody> sellerLogin(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.SUB_SELLER_LOGIN)
    Call<ResponseBody> subSellerLoginApi(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.SLIDER_HOME)
    Call<ResponseBody> getSlider(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_SELLER_CAT)
    Call<ResponseBody> getSellerCat(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @GET(ApiConstant.GET_ALL_COUNTRY)
    Call<ResponseBody> getCountry();

    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_CITY)
    Call<ResponseBody> getAllCity(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_STATE)
    Call<ResponseBody> getAllState(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_STATE_CITY)
    Call<ResponseBody> getAllStateCity(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_PROFILE)
    Call<ResponseBody> getSellerProfileApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_PROFILE_UPDATE)
    Call<ResponseBody> getSellerProfileUpdateApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.CHANGE_PASSWORD)
    Call<ResponseBody> changeSellerPassApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.ADD_SHOP)
    Call<ResponseBody> sellerAddShopApi(
            @HeaderMap Map<String, String> auth,
            @Part("user_id") RequestBody user_id,
            @Part("shop_name") RequestBody shop_name,
            @Part("sub_categary_id") RequestBody sub_categary_id,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("street_landmark") RequestBody streetLandmark,
            @Part("neighbourhood") RequestBody neighbourhood,
            @Part("country") RequestBody country,
            @Part("state") RequestBody state,
            @Part("city") RequestBody city,
            @Part("currency") RequestBody currency,
            @Part("country_code") RequestBody country_code,
            @Part("phone") RequestBody phone,
            @Part("phonenumber") RequestBody phonenumber,
            @Part("mobileaccount") RequestBody mobileaccount,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("seller_register_id") RequestBody register_id,
            @Part("user_seller_id") RequestBody user_seller_id,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
    );


    @Multipart
    @POST(ApiConstant.UPDATE_SHOP_IMAGE)
    Call<ResponseBody> updateShopImageApi(
            @HeaderMap Map<String, String> auth,
            @Part("shop_id") RequestBody username,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
    );




    @FormUrlEncoded
    @POST(ApiConstant.GET_DAILY_CLOSE_DAY)
    Call<ResponseBody> getDailyCloseDayApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_DAILY_CLOSE_DAY)
    Call<ResponseBody> addDailyCloseDayApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.ADD_OPEN_TIME)
    Call<ResponseBody> addOpenTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_OPEN_TIME)
    Call<ResponseBody> updateOpenTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_EDIT_OPEN_TIME)
    Call<ResponseBody> getEditOpenTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_CLOSE_TIME)
    Call<ResponseBody> addCloseTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_CLOSE_TIME)
    Call<ResponseBody> updateCloseTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_EDIT_CLOSE_TIME)
    Call<ResponseBody> getEditCloseTimeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_HOLIDAY)
    Call<ResponseBody> getAllHolidaysApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_HOLIDAY)
    Call<ResponseBody> addAllHolidaysApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.DELETE_HOLIDAY)
    Call<ResponseBody> deleteHolidaysApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_SHOP)
    Call<ResponseBody> getAllShopApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @Multipart
    @POST(ApiConstant.EDIT_SHOP)
    Call<ResponseBody> sellerEditShopApi(
            @HeaderMap Map<String, String> auth,
            @Part("shop_id") RequestBody shop_id,
            @Part("user_id") RequestBody user_id,
            @Part("shop_name") RequestBody shop_name,
            @Part("sub_categary_id") RequestBody sub_categary_id,
            @Part("description") RequestBody description,
            @Part("address") RequestBody address,
            @Part("street_landmark") RequestBody streetLandmark,
            @Part("neighbourhood") RequestBody neighbourhood,
            @Part("country") RequestBody country,
            @Part("state") RequestBody state,
            @Part("city") RequestBody city,
            @Part("country_code") RequestBody country_code,
            @Part("phone") RequestBody phone,
            @Part("phonenumber") RequestBody phonenumber,
            @Part("mobileaccount") RequestBody mobileaccount,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("seller_register_id") RequestBody register_id,
            @Part("user_seller_id") RequestBody user_seller_id,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3
    );


    @GET(ApiConstant.GET_MAIN_CATEGORY)
    Call<ResponseBody> getMainCategoryApi(@HeaderMap Map<String, String> auth);

    @FormUrlEncoded
    @POST(ApiConstant.GET_MAIN_SUB_CATEGORY)
    Call<ResponseBody> getMainSubCategoryApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_BRAND)
    Call<ResponseBody> getBrandApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_BRAND)
    Call<ResponseBody> addBrandApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_ATTRIBUTE)
    Call<ResponseBody> addAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_ATTRIBUTE)
    Call<ResponseBody> getAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_ATTRIBUTE_NEW)
    Call<ResponseBody> getAttributeNewApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.DELETE_ATTRIBUTE)
    Call<ResponseBody> deleteAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADD_SUB_ATTRIBUTE)
    Call<ResponseBody> addSubAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_SUB_ATTRIBUTE)
    Call<ResponseBody> getSubAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.DELETE_SUB_ATTRIBUTE)
    Call<ResponseBody> deleteSubAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.ADD_SHOP_PRODUCT)
    Call<ResponseBody> sellerAddShopProductApi(
            @HeaderMap Map<String, String> auth,
            @Part("user_id") RequestBody user_id,
            @Part("shop_id") RequestBody shop_id,
            @Part("product_name") RequestBody product_name,
            @Part("product_price") RequestBody product_price,
            @Part("description") RequestBody description,
            @Part("sku") RequestBody sku,
            @Part("delivery_charges") RequestBody delivery_charges,
            @Part("in_stock") RequestBody in_stock,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category") RequestBody sub_category,
            @Part("product_brand") RequestBody product_brand,
            @Part("attribute") RequestBody attribute,
            @Part("seller_register_id") RequestBody register_id,
            @Part("user_seller_id") RequestBody user_seller_id,
            @Part("product_quantity") RequestBody qty,

            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4

    );


    @Multipart
    @POST(ApiConstant.UPDATE_PRODUCT_IMAGE)
    Call<ResponseBody> updateProductImageApi(
            @HeaderMap Map<String, String> auth,
            @Part("product_id") RequestBody username,
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4

    );





    ///////////


    @FormUrlEncoded
    @POST(ApiConstant.ADD_SHOP_DETAIL_PRODUCT)
    Call<ResponseBody> getShopDetailApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.PRODUCT_ACTIVE_DEACTIVE)
    Call<ResponseBody> updateProductApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.SHOP_ACTIVE_DEACTIVE)
    Call<ResponseBody> updateShopApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.UPDATE_PROFILE)
    Call<ResponseBody> updateProfileApi(
            @HeaderMap Map<String, String> auth,
            @Part("user_id") RequestBody user_id,
            @Part("user_name") RequestBody user_name,
            @Part("address") RequestBody address,
            @Part("lat") RequestBody lat,
            @Part("lon") RequestBody lon,
            @Part("country") RequestBody country,
            @Part("city") RequestBody city,
            @Part("seller_register_id") RequestBody register_id,
            @Part("user_seller_id") RequestBody user_seller_id,
            @Part MultipartBody.Part file

    );


    @GET(ApiConstant.GET_SUBSCRIPTION_PLAN)
    Call<ResponseBody> getSubscriptionApi(@HeaderMap Map<String, String> auth, @Query("country_id") String countryId);
/*
    @FormUrlEncoded
    @POST(ApiConstant.PAY_SUBSCRIPTION_PLAN)
    Call<ResponseBody> purchaseSubscriptionPayApi( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN)
    Call<ResponseBody> purchaseZeroSubscriptionPayApi(@HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);
*/


    @FormUrlEncoded
    @POST(ApiConstant.PAY_SUBSCRIPTION_PLAN)
    Call<ResponseBody> purchaseSubscriptionPayApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.PAY_FREE_SUBSCRIPTION_PLAN)
    Call<ResponseBody> purchaseZeroSubscriptionPayApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_CURRENT_MEMBERSHIP_PLAN)
    Call<ResponseBody> currentMemberShipPlanApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.ADDED_PRODUCT_SUBCAT)
    Call<ResponseBody> addedProductSubCatApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.ADDED_PRODUCT_ATTRIBUTE)
    Call<ResponseBody> addedProductAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.EDIT_SHOP_PRODUCT)
    Call<ResponseBody> sellerEditShopProductApi(
            @HeaderMap Map<String, String> auth,
            @Part("product_id") RequestBody product_id,
            @Part("product_name") RequestBody product_name,
            @Part("product_price") RequestBody product_price,
            @Part("description") RequestBody description,
            @Part("sku") RequestBody sku,
            @Part("delivery_charges") RequestBody delivery_charges,
            @Part("in_stock") RequestBody in_stock,
            @Part("category_id") RequestBody category_id,
            @Part("sub_category") RequestBody sub_category,
            @Part("product_brand") RequestBody product_brand,
            @Part("attribute") RequestBody attribute,
            @Part("seller_register_id") RequestBody register_id,
            @Part("user_seller_id") RequestBody user_seller_id,
            @Part("product_quantity") RequestBody qty,

           /* @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2*/
            @Part MultipartBody.Part file,
            @Part MultipartBody.Part file2,
            @Part MultipartBody.Part file3,
            @Part MultipartBody.Part file4

    );


    @FormUrlEncoded
    @POST(ApiConstant.GET_NOTIFICATIONS)
    Call<ResponseBody> getAllNotificationApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.NOTIFICATIONS_COUNTER)
    Call<ResponseBody> getNotificationCounterApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_ORDER)
    Call<ResponseBody> getAllOrderApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GET_ORDER_DETAIL)
    Call<ResponseBody> getOrderDetailsApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.COMPLETE_SELF_COLLECT_ORDER)
    Call<ResponseBody> completeSelfCollectOrderApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.ACCEPT_DECLINE_ORDER)
    Call<ResponseBody> acceptDeclineOrderApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.SEND_PUSH_NOTIFICATION)
    Call<ResponseBody> sendNotificationApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.GENERATE_TOKEN)
    Call<ResponseBody> getTokenApi(@FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.ADD_DELIVERY)
    Call<ResponseBody> addDeliveryApi(
            @HeaderMap Map<String, String> auth,
            @Part("package_name") RequestBody package_name,
            @Part("owner") RequestBody owner,
            @Part("command_date") RequestBody command_date,
            @Part("adresse_source") RequestBody adresse_source,
            @Part("adresse_destination") RequestBody adresse_destination,
            @Part("delivery_type") RequestBody delivery_type,
            @Part("latitude_source") RequestBody latitude_source,
            @Part("longitude_source") RequestBody longitude_source,
            @Part("latitude_destination") RequestBody latitude_destination,
            @Part("longitude_destination") RequestBody longitude_destination1,
            @Part("command_number") RequestBody command_number,
            @Part("delivery_amount") RequestBody delivery_amount,
            @Part("command_amount") RequestBody command_amount,
            @Part("customer_code") RequestBody customer_code,
            @Part MultipartBody.Part file


    );


    @FormUrlEncoded
    @POST(ApiConstant.DELETE_PRODUCT)
    Call<ResponseBody> deleteProductApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @GET("privacy_policy")
    Call<ResponseBody> getPrivacyPolicy();

    @GET("terms_condition")
    Call<ResponseBody> getTermsConditions();

    @GET("terms_condition_for_sale")
    Call<ResponseBody> getUseConditions();

    @GET("get_register_description")
    Call<ResponseBody> getTitleDes();

    @GET(ApiConstant.GET_CURRENCY)
    Call<ResponseBody> getCurrency();


    @FormUrlEncoded
    @POST(ApiConstant.SEARCH_SHOP)
    Call<ResponseBody> searchShopApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.CHECK_PLAN_STATUS)
    Call<ResponseBody> checkPlanStatusApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.LOGOUT)
    Call<ResponseBody> logoutApi(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_driver_location")
    Call<ResponseBody> getDriverLocationApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.ONLINE_ORDER_HISTORY)
    Call<ResponseBody> getOnlineOrderHistoryApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("update_product_availability")
    Call<ResponseBody> updateProductAvailabilityApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST("get_users")
    Call<ResponseBody> getSellerChatListApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("password_reset")
    Call<ResponseBody> sendAdminRequest(@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("forgot_password")
    Call<ResponseBody> forgot_passwordNew(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.UPDATE_LANGUAGE)
    Call<ResponseBody> updateLanguageApi(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_PRODUCT_DETAILS)
    Call<ResponseBody> getProductDetailApi(@HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_wallet_history")
    Call<ResponseBody> get_transfer_money( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("payment_processing")
    Call<ResponseBody> checkPaymentStatusApi( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("add_money")
    Call<ResponseBody> add_money( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("transfer_money_wllet_to_wallet")
    Call<ResponseBody> transfer_money( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("submit_withdrawal_request")
    Call<ResponseBody> withdraw_money( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("check_user_exist_or_not_to_send_invoice")
    Call<ResponseBody> checkUserExitApi(@HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("generate_invoice_recharge_wallet")
    Call<ResponseBody> sendWalletRechargeInvoiceAnotherApi(@HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("update_withdrawal_request")
    Call<ResponseBody> withdraw_money_update( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("yes_transfer_money_wllet_to_wallet")
    Call<ResponseBody> transfer_moneyFirstApi( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("runPvit_transfer_money")
    Call<ResponseBody> transferNumberMoney(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("send_message_to_admin")
    Call<ResponseBody> sendAdminMsg( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("money_transfer_request")
    Call<ResponseBody> transfer_money_request( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("send_money_transfer_request")
    Call<ResponseBody> transferMoneyRequestSend( @HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_profile")
    Call<GetProfileModal> get_profile(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("invoice_details_recharge_wallet")
    Call<ResponseBody> getWalletInvoiceDataApi(@HeaderMap Map<String, String> auth,@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("runPvit")
    Call<ResponseBody> payment(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST("add_money_card")
    Call<ResponseBody> addMoneyToWalletFromCard(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("runPvit_transfer_money")
    Call<ResponseBody> transferMoneyToWalletFromCard(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("invoice_details")
    Call<ResponseBody> getInvoiceDataApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST("runPvitWebView")
    Call<ResponseBody> cardPaymentApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_ADMIN_MSG)
    Call<ResponseBody> getAdminMsgApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.SEND_ADMIN_MSG)
    Call<ResponseBody> notifyAdminApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_ALL_SUB_SELLER)
    Call<ResponseBody> getAllSubSellerApi(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.SUB_SELLER_CHANGE_PASSWORD)
    Call<ResponseBody> changeSubSellerPassApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_SUB_SELLER_PROFILE)
    Call<ResponseBody> getSubSellerProfileApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.DELETE_SUB_SELLER_PROFILE)
    Call<ResponseBody> deleteSubSellerProfileApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @Multipart
    @POST(ApiConstant.UPDATE_SUB_SELLER_PROFILE)
    Call<ResponseBody> updateSubSellerProfileApi(
            @HeaderMap Map<String, String> auth,
            @Part("name") RequestBody name,
            @Part("username") RequestBody username,
            @Part("email") RequestBody email,
            @Part("sub_seller_id") RequestBody sub_seller_id,
            @Part("country_code") RequestBody countryCode,
            @Part("mobile") RequestBody mobile,
            @Part("parent_seller_id") RequestBody parent_seller_id,
            @Part("country") RequestBody country,
            @Part("state") RequestBody state,
            @Part("city") RequestBody city,
            @Part("address") RequestBody address,
            @Part("shop_id") RequestBody shopId,
            @Part MultipartBody.Part file);



    @FormUrlEncoded
    @POST(ApiConstant.GET_SELLER_COUNTRY_WISE)
    Call<ResponseBody> getSellerApi(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.GET_ADDED_PRODUCT_ATTRIBUTE_SUB_ATTRIBUTE)
    Call<ResponseBody> getAddedAttributeSubAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST(ApiConstant.EDIT_PAGE_DELETE_SUB_ATTRIBUTE)
    Call<ResponseBody> deleteSubAttributeInEditPageApi(@FieldMap Map<String, String> paramHashMap);


    @FormUrlEncoded
    @POST(ApiConstant.CHECK_UNCHECK_VALIDATE)
    Call<ResponseBody> checkUncheckAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.CHECK_UNCHECK_ATTRIBUTE)
    Call<ResponseBody> checkUncheckSubAttributeApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.INFORM_DELIVERY_CANCEL_ORDER)
    Call<ResponseBody> informDeliveryOrderCancelApi( @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.RETURN_SELLER_PRODUCT)
    Call<ResponseBody> productReturnApi( @FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST(ApiConstant.INTERNATIONAL_TRANSIT_DELIVERY)
    Call<ResponseBody> internationalDeliveryTransitApi( @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.SEND_ADMIN_MSG)
    Call<ResponseBody> notifyAdminNewApi(@FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.CHECK_EMAIL_EXITS)
    Call<ResponseBody> checkEmailExitApi( @FieldMap Map<String, String> params);

    @FormUrlEncoded
    @POST(ApiConstant.CHECK_MOBILE_EXITS)
    Call<ResponseBody> checkNumberExitApi( @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.CHANGE_USER_PASSWORD_STATUS)
    Call<ResponseBody> changeUserPasswordStatusApi( @FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST(ApiConstant.GET_PERIODIC_REPORT)
    Call<ResponseBody> getPeriodicReportApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.GET_SELLER_PERIODIC_REPORT)
    Call<ResponseBody> getSellerPeriodicReportApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_list_shop_filter")
    Call<ResponseBody> getShopsApi( @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_list_sub_seller_filter")
    Call<ResponseBody> getSubSellerShopsApi( @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("get_country_wise_shop_list_by_seller")
    Call<ResponseBody> getSubSellerShopsCountryWiseApi( @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST(ApiConstant.SUB_SELLER_ORDER_COUNTER)
    Call<ResponseBody> getSubSellerOrderCounterApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


    @FormUrlEncoded
    @POST("forgot_password_sub_seller")
    Call<ResponseBody> forgotPasswordSubSellerApi( @FieldMap Map<String, String> params);



    @FormUrlEncoded
    @POST(ApiConstant.GET_SELLER_PERIODIC_REPORT_NEW)
    Call<ResponseBody> getSellerPeriodicReportNewResponseApi(@HeaderMap Map<String, String> auth, @FieldMap Map<String, String> params);


}
