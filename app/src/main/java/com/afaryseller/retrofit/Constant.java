package com.afaryseller.retrofit;

import java.util.ArrayList;
import java.util.List;

public class Constant {
   // public static final String BASE_URL = "https://technorizen.com/afarycode/webservice/";
    public static final String BASE_URL = "https://technorizen.com/afarycodewebsite/webservice/";

    public static final String BASE_URL_TEST = "https://technorizen.com/afarycodewebsite/testwebservice/";

    public static final String BASE_URL_OTHER = "https://delivery.tchanigroup.com/api/";


    public static String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    public static String SUBADMIN = "Seller";

    public static String USER = "USER";

    public static String SUB_SELLER = "Sub Seller";


    public static String SELLER_INFO = "seller_info";

    public static String SELLER_LANGUAGE = "seller_language";

    public static String shopId = "shop_id";

    public static String shopName = "shop_name";


    public static String SUB_SELLER_ID = "sub_seller_id";





    public static List<String> getNationality() {
        List<String> list = new ArrayList<>();
        list.add("Afghanistan");
        list.add("Albania");
        list.add("Algeria");
        list.add("Argentina");
        list.add("Argentinian");
        list.add("Australia");
        list.add("Austria");
        list.add("Bangladesh");
        list.add("Belgium");
        list.add("Bolivia");
        list.add("Botswana");
        list.add("Brazil");
        list.add("Bulgaria");
        list.add("Cambodia");
        list.add("Cameroon");
        list.add("Canada");
        list.add("Chile");
        list.add("China");
        list.add("Colombia");
        list.add("Costa Rica");
        list.add("Croatia");
        list.add("Cuba");
        list.add("Czech");
        list.add("Denmark");
        list.add("Dominican");
        list.add("Ecuador");
        list.add("Egypt");
        list.add("El Salvador");
        list.add("England");
        list.add("Estonia");
        list.add("Ethiopia");
        list.add("Fiji");
        list.add("Finland");
        list.add("France");
        list.add("Germany");
        list.add("Ghana");
        list.add("Greece");
        list.add("Guatemala");
        list.add("Haiti");
        list.add("Honduras");
        list.add("Hungary");
        list.add("Iceland");
        list.add("India");
        list.add("Indonesia");
        list.add("Iran");
        list.add("Iraq");
        list.add("Ireland");
        list.add("Israel");
        list.add("Italy");
        list.add("Jamaica");
        list.add("Japan");
        list.add("Jordan");
        list.add("Kenya");
        list.add("Kuwait");
        list.add("Laos");
        list.add("Latvia");
        list.add("Lebanon");
        list.add("Libya");
        list.add("Lithuania");
        list.add("Madagascar");
        list.add("Malaysia");
        list.add("Mali");
        list.add("Malta");
        list.add("Mexico");
        list.add("Mongolia");
        list.add("Morocco");
        list.add("Mozambique");
        list.add("Namibia");
        list.add("Nepal");
        list.add("Netherlands");
        list.add("New Zealand");
        list.add("Nicaragua");
        list.add("Nigeria");
        list.add("Norway");
        list.add("Pakistan");
        list.add("Panama");
        list.add("Paraguay");
        list.add("Peru");
        list.add("Philippines");
        list.add("Poland");
        list.add("Portugal");
        list.add("Romania");
        list.add("Russia");
        list.add("Saudi Arabia");
        list.add("Scotland");
        list.add("Senegal");
        list.add("Serbia");
        list.add("Singapore");
        list.add("Slovakia");
        list.add("South Africa");
        list.add("South Korea");
        list.add("Spain");
        list.add("Sri Lanka");
        list.add("Sudan");
        list.add("Sweden");
        list.add("Switzerland");
        list.add("Syria");
        list.add("Taiwan");
        list.add("Tajikistan");
        list.add("Thailand");
        list.add("Tonga");
        list.add("Tunisia");
        list.add("Turkey");
        list.add("Ukraine");
        list.add("United Arab Emirates");
        list.add("(The) United Kingdom");
        list.add("(The) United States	American");
        list.add("Uruguay");
        list.add("Venezuela");
        list.add("Vietnam");
        list.add("Wales");
        list.add("Zambia");
        return list;
    }


    public static String commaSeprated(ArrayList<String> arrayList) {
        StringBuilder result = new StringBuilder();
        for (String string : arrayList) {
            result.append(string);
            result.append(",");
        }
        return result.length() > 0 ? /*result.substring(0, result.length() - 1)*/result.deleteCharAt(result.length() - 1).toString() : "";
    }


}
