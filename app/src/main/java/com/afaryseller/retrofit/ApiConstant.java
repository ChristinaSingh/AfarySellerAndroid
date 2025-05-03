package com.afaryseller.retrofit;

public class ApiConstant {


    public final static String SIGNUP = "signup_seller";

    public final static String SUB_SELLER_SIGNUP = "add_sub_seller";


    public final static String SELLER_GET_ALL_SKILLS = "get_skills";

   /* public final static String SEND_OTP1 = "send_otp";

    public final static String VERIFY_OTP1 = "verify_mobile_otp";*/

    public final static String SEND_OTP = "send_otp_before_signup";

    public final static String SEND_WHATSAPP_OTP = "send_otp_before_signup_whatsup";

    public final static String VERIFY_OTP = "verify_mobile_otp_before_signup";


    //public final static String SELLER_SKILLS = "update_skills";

    public final static String SELLER_SKILLS = "add_skills";


    public final static String SELLER_UPDATE_DOCUMENT = "update_document";

    // login
    public final static String SELLER_LOGIN = "seller_login";

    public final static String SUB_SELLER_LOGIN = "login_sub_seller";


    public final static String SLIDER_HOME = "get_seller_slider";

    public final static String GET_SELLER_CAT = "get_category_seller";

    public final static String GET_ALL_COUNTRY = "get_country";

    public final static String GET_ALL_STATE = "get_states";

    public final static String GET_ALL_CITY = "get_city";

    public final static String GET_ALL_STATE_CITY = "get_state_city";

    public final static String GET_PROFILE = "get_seller_profile";
    public final static String GET_PROFILE_UPDATE = "get_seller_profile";


    public final static String CHANGE_PASSWORD = "change_password";

    public final static String ADD_SHOP = "add_shop";

    public final static String UPDATE_SHOP_IMAGE = "update_shop_image";

    public final static String GET_DAILY_CLOSE_DAY = "get_shop_week_name";

    public final static String ADD_DAILY_CLOSE_DAY = "update_shop_week_name";

    public final static String ADD_OPEN_TIME = "update_shop_open_time";

    public final static String UPDATE_OPEN_TIME = "update_single_shop_open_time";
    public final static String GET_EDIT_OPEN_TIME = "get_shop_week_open_time";

    public final static String ADD_CLOSE_TIME = "update_shop_close_time";

    public final static String UPDATE_CLOSE_TIME = "update_single_shop_close_time";


    public final static String GET_EDIT_CLOSE_TIME = "get_shop_week_close_time";


    public final static String ADD_HOLIDAY = "add_holiday";

    public final static String GET_HOLIDAY = "get_holiday";

    public final static String DELETE_HOLIDAY = "delete_holiday";

    public final static String GET_ALL_SHOP = "get_shop";

    public final static String EDIT_SHOP = "edit_shop";

    public final static String GET_MAIN_CATEGORY = "get_category_all";

    public final static String GET_MAIN_SUB_CATEGORY = "get_subcat";

    public final static String GET_BRAND = "get_brand";

    public final static String ADD_BRAND = "add_brand";

    public final static String ADD_ATTRIBUTE = "add_validate";

    public final static String GET_ATTRIBUTE = "get_validate";

    public final static String GET_ATTRIBUTE_NEW = "get_validate_add";


    public final static String DELETE_ATTRIBUTE = "delete_validate";

    public final static String GET_SUB_ATTRIBUTE = "get_attribute";

    public final static String ADD_SUB_ATTRIBUTE = "add_attribute";

    public final static String DELETE_SUB_ATTRIBUTE = "delete_attribute";

    public final static String ADD_SHOP_PRODUCT = "add_product";

    public final static String UPDATE_PRODUCT_IMAGE = "update_product_image";

    public final static String ADD_SHOP_DETAIL_PRODUCT = "get_product";

    public final static String UPDATE_PROFILE = "update_seller_profile";

    public final static String PRODUCT_ACTIVE_DEACTIVE = "active_deactive_product";

    public final static String SHOP_ACTIVE_DEACTIVE = "active_deactive_shop";

    public final static String GET_SUBSCRIPTION_PLAN = "get_plans";

    public final static String PAY_SUBSCRIPTION_PLAN = "purchase_plan";

    public final static String GET_CURRENT_MEMBERSHIP_PLAN = "get_membership_status";



   /*
    public final static String PAY_SUBSCRIPTION_PLAN = "runPvit_plan";


    public final static String PAY_FREE_SUBSCRIPTION_PLAN = "runPvit_plan_free";
*/

    public final static String PAY_FREE_SUBSCRIPTION_PLAN = "purchase_plan";


    public final static String ADDED_PRODUCT_ATTRIBUTE = "get_sub_atrbute";

    public final static String GET_ADDED_PRODUCT_ATTRIBUTE_SUB_ATTRIBUTE = "get_validate_and_attribute";


    public final static String ADDED_PRODUCT_SUBCAT = "get_sub_cat";

    public final static String EDIT_SHOP_PRODUCT = "edit_product";

    public final static String GET_NOTIFICATIONS = "get_notification";

    public final static String NOTIFICATIONS_COUNTER = "get_notification_count";

    public final static String GET_ALL_ORDER = "get_order";

    public final static String GET_ORDER_DETAIL = "get_order_detial";

    public final static String COMPLETE_SELF_COLLECT_ORDER = "self_collect_order_complete";


    public final static String ACCEPT_DECLINE_ORDER = "accept_decline_order";

    public static final String SEND_PUSH_NOTIFICATION = "insert_chat";

    public static final String GENERATE_TOKEN = "login";

  //  public static final String ADD_DELIVERY = "delivery/add";

    public static final String ADD_DELIVERY = "delivery_app_add";

    public static final String DELETE_PRODUCT = "delete_product";

    public final static String SEARCH_SHOP = "search_shop";

    public final static String CHECK_PLAN_STATUS = "get_active_plan";

    public final static String LOGOUT = "seller_logout";

    public final static String ONLINE_ORDER_HISTORY = "get_history_seller";


    public static final String GET_CURRENCY = "get_currency" ;

    public final static String UPDATE_LANGUAGE = "update_language";

    public final static String GET_PRODUCT_DETAILS = "get_product_detail_seller";

    public final static String SEND_ADMIN_MSG = "send_admin_message";

    public final static String GET_ADMIN_MSG = "get_admin_message";

    public final static String GET_ALL_SUB_SELLER = "get_sub_seller_list";

    public final static String SUB_SELLER_CHANGE_PASSWORD = "update_sub_seller_password";

    public final static String GET_SUB_SELLER_PROFILE = "get_sub_seller";

    public final static String DELETE_SUB_SELLER_PROFILE = "delete_sub_seller";

    public final static String UPDATE_SUB_SELLER_PROFILE = "update_sub_seller";

    public final static String GET_SELLER_COUNTRY_WISE = "get_seller_for_sub_seller";


    public final static String EDIT_PAGE_DELETE_SUB_ATTRIBUTE = "delete_attribute";

    public final static String CHECK_UNCHECK_VALIDATE = "checked_unchecked_validate";

    public final static String CHECK_UNCHECK_ATTRIBUTE = "checked_unchecked_attribute";

    public final static String INFORM_DELIVERY_CANCEL_ORDER = "delivery_cancel_order";

    public final static String RETURN_SELLER_PRODUCT = "product_has_been_returned_to_seller";

    public final static String INTERNATIONAL_TRANSIT_DELIVERY = "update_international_delivery";


    public final static String CHECK_EMAIL_EXITS = "check_email_already_exist";

    public final static String CHECK_MOBILE_EXITS = "check_country_code_with_mobile_exist";


    public final static String CHANGE_USER_PASSWORD_STATUS = "password_user_status";


    public final static String GET_PERIODIC_REPORT = "get_order_filter";

    public final static String GET_SELLER_PERIODIC_REPORT = "get_order_filter";

    public final static String SUB_SELLER_ORDER_COUNTER = "count_active_bookings_by_seller";


    // https://technorizen.com/afarycodewebsite/Webservice/?seller_id=137&name=test


    public final static String TERMS_AND_CONDITIONS = Constant.BASE_URL_TEST+"terms_condition";

    public final static String PRIVACY_POLICY = Constant.BASE_URL_TEST+"privacy_policy";


}
