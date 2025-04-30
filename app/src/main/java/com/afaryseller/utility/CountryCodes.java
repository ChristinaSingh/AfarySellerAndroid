package com.afaryseller.utility;




import java.util.HashMap;
import java.util.Map;

public class CountryCodes {

  /*  public static Map<String, Integer> getCountryPhoneCodes() {
        Map<String, Integer> countryPhoneCodes = new HashMap<>();

        countryPhoneCodes.put("AF", 93);   // Afghanistan
        countryPhoneCodes.put("AL", 355);  // Albania
        countryPhoneCodes.put("DZ", 213);  // Algeria
        countryPhoneCodes.put("AD", 376);  // Andorra
        countryPhoneCodes.put("AO", 244);  // Angola
        countryPhoneCodes.put("AG", 268);  // Antigua and Barbuda
        countryPhoneCodes.put("AR", 54);   // Argentina
        countryPhoneCodes.put("AM", 374);  // Armenia
        countryPhoneCodes.put("AU", 61);   // Australia
        countryPhoneCodes.put("AT", 43);   // Austria
        countryPhoneCodes.put("AZ", 994);  // Azerbaijan
        countryPhoneCodes.put("BS", 242);  // Bahamas
        countryPhoneCodes.put("BH", 973);  // Bahrain
        countryPhoneCodes.put("BD", 880);  // Bangladesh
        countryPhoneCodes.put("BB", 246);  // Barbados
        countryPhoneCodes.put("BY", 375);  // Belarus
        countryPhoneCodes.put("BE", 32);   // Belgium
        countryPhoneCodes.put("BZ", 501);  // Belize
        countryPhoneCodes.put("BJ", 229);  // Benin
        countryPhoneCodes.put("BT", 975);  // Bhutan
        countryPhoneCodes.put("BO", 591);  // Bolivia
        countryPhoneCodes.put("BA", 387);  // Bosnia and Herzegovina
        countryPhoneCodes.put("BW", 267);  // Botswana
        countryPhoneCodes.put("BR", 55);   // Brazil
        countryPhoneCodes.put("BN", 673);  // Brunei
        countryPhoneCodes.put("BG", 359);  // Bulgaria
        countryPhoneCodes.put("BF", 226);  // Burkina Faso
        countryPhoneCodes.put("BI", 257);  // Burundi
        countryPhoneCodes.put("CV", 238);  // Cape Verde
        countryPhoneCodes.put("KH", 855);  // Cambodia
        countryPhoneCodes.put("CM", 237);  // Cameroon
        countryPhoneCodes.put("CA", 1);    // Canada
        countryPhoneCodes.put("CF", 236);  // Central African Republic
        countryPhoneCodes.put("TD", 235);  // Chad
        countryPhoneCodes.put("CL", 56);   // Chile
        countryPhoneCodes.put("CN", 86);   // China
        countryPhoneCodes.put("CO", 57);   // Colombia
        countryPhoneCodes.put("KM", 269);  // Comoros
        countryPhoneCodes.put("CD", 243);  // Congo, Democratic Republic of the
        countryPhoneCodes.put("CG", 242);  // Congo, Republic of the
        countryPhoneCodes.put("CR", 506);  // Costa Rica
        countryPhoneCodes.put("CI", 225);  // Côte d'Ivoire
        countryPhoneCodes.put("HR", 385);  // Croatia
        countryPhoneCodes.put("CU", 53);   // Cuba
        countryPhoneCodes.put("CY", 357);  // Cyprus
        countryPhoneCodes.put("CZ", 420);  // Czech Republic
        countryPhoneCodes.put("DK", 45);   // Denmark
        countryPhoneCodes.put("DJ", 253);  // Djibouti
        countryPhoneCodes.put("DM", 1767); // Dominica
        countryPhoneCodes.put("DO", 1809); // Dominican Republic
        countryPhoneCodes.put("EC", 593);  // Ecuador
        countryPhoneCodes.put("EG", 20);   // Egypt
        countryPhoneCodes.put("SV", 503);  // El Salvador
        countryPhoneCodes.put("GQ", 240);  // Equatorial Guinea
        countryPhoneCodes.put("ER", 291);  // Eritrea
        countryPhoneCodes.put("EE", 372);  // Estonia
        countryPhoneCodes.put("ET", 251);  // Ethiopia
        countryPhoneCodes.put("FJ", 679);  // Fiji
        countryPhoneCodes.put("FI", 358);  // Finland
        countryPhoneCodes.put("FR", 33);   // France
        countryPhoneCodes.put("GA", 241);  // Gabon
        countryPhoneCodes.put("GM", 220);  // Gambia
        countryPhoneCodes.put("GE", 995);  // Georgia
        countryPhoneCodes.put("DE", 49);   // Germany
        countryPhoneCodes.put("GH", 233);  // Ghana
        countryPhoneCodes.put("GR", 30);   // Greece
        countryPhoneCodes.put("GD", 1473); // Grenada
        countryPhoneCodes.put("GT", 502);  // Guatemala
        countryPhoneCodes.put("GN", 224);  // Guinea
        countryPhoneCodes.put("GW", 245);  // Guinea-Bissau
        countryPhoneCodes.put("GY", 592);  // Guyana
        countryPhoneCodes.put("HT", 509);  // Haiti
        countryPhoneCodes.put("HN", 504);  // Honduras
        countryPhoneCodes.put("HU", 36);   // Hungary
        countryPhoneCodes.put("IS", 354);  // Iceland
        countryPhoneCodes.put("IN", 91);   // India
        countryPhoneCodes.put("ID", 62);   // Indonesia
        countryPhoneCodes.put("IR", 98);   // Iran
        countryPhoneCodes.put("IQ", 964);  // Iraq
        countryPhoneCodes.put("IE", 353);  // Ireland
        countryPhoneCodes.put("IL", 972);  // Israel
        countryPhoneCodes.put("IT", 39);   // Italy
        countryPhoneCodes.put("JM", 1876); // Jamaica
        countryPhoneCodes.put("JP", 81);   // Japan
        countryPhoneCodes.put("JO", 962);  // Jordan
        countryPhoneCodes.put("KZ", 7);    // Kazakhstan
        countryPhoneCodes.put("KE", 254);  // Kenya
        countryPhoneCodes.put("KI", 686);  // Kiribati
        countryPhoneCodes.put("KP", 850);  // Korea, North
        countryPhoneCodes.put("KR", 82);   // Korea, South
        countryPhoneCodes.put("KW", 965);  // Kuwait
        countryPhoneCodes.put("KG", 996);  // Kyrgyzstan
        countryPhoneCodes.put("LA", 856);  // Laos
        countryPhoneCodes.put("LV", 371);  // Latvia
        countryPhoneCodes.put("LB", 961);  // Lebanon
        countryPhoneCodes.put("LS", 266);  // Lesotho
        countryPhoneCodes.put("LR", 231);  // Liberia
        countryPhoneCodes.put("LY", 218);  // Libya
        countryPhoneCodes.put("LI", 423);  // Liechtenstein
        countryPhoneCodes.put("LT", 370);  // Lithuania
        countryPhoneCodes.put("LU", 352);  // Luxembourg
        countryPhoneCodes.put("MO", 853);  // Macau
        countryPhoneCodes.put("MK", 389);  // North Macedonia
        countryPhoneCodes.put("MG", 261);  // Madagascar
        countryPhoneCodes.put("MW", 265);  // Malawi
        countryPhoneCodes.put("MY", 60);   // Malaysia
        countryPhoneCodes.put("MV", 960);  // Maldives
        countryPhoneCodes.put("ML", 223);  // Mali
        countryPhoneCodes.put("MT", 356);  // Malta
        countryPhoneCodes.put("MH", 692);  // Marshall Islands
        countryPhoneCodes.put("MR", 222);  // Mauritania
        countryPhoneCodes.put("MU", 230);  // Mauritius
        countryPhoneCodes.put("MX", 52);   // Mexico
        countryPhoneCodes.put("FM", 691);  // Micronesia
        countryPhoneCodes.put("MD", 373);  // Moldova
        countryPhoneCodes.put("MC", 377);  // Monaco
        countryPhoneCodes.put("MN", 976);  // Mongolia
        countryPhoneCodes.put("MA", 212);  // Morocco
        countryPhoneCodes.put("MZ", 258);  // Mozambique
        countryPhoneCodes.put("MM", 95);   // Myanmar
        countryPhoneCodes.put("NA", 264);  // Namibia
        countryPhoneCodes.put("NR", 674);  // Nauru
        countryPhoneCodes.put("NP", 977);  // Nepal
        countryPhoneCodes.put("NL", 31);   // Netherlands
        countryPhoneCodes.put("NZ", 64);   // New Zealand
        countryPhoneCodes.put("NI", 505);  // Nicaragua
        countryPhoneCodes.put("NE", 227);  // Niger
        countryPhoneCodes.put("NG", 234);  // Nigeria
        countryPhoneCodes.put("NU", 683);  // Niue
        countryPhoneCodes.put("NF", 672);  // Norfolk Island
        countryPhoneCodes.put("MP", 1670); // Northern Mariana Islands
        countryPhoneCodes.put("NO", 47);   // Norway
        countryPhoneCodes.put("OM", 968);  // Oman
        countryPhoneCodes.put("PK", 92);   // Pakistan
        countryPhoneCodes.put("PW", 680);  // Palau
        countryPhoneCodes.put("PA", 507);  // Panama
        countryPhoneCodes.put("PG", 675);  // Papua New Guinea
        countryPhoneCodes.put("PY", 595);  // Paraguay
        countryPhoneCodes.put("PE", 51);   // Peru
        countryPhoneCodes.put("PH", 63);   // Philippines
        countryPhoneCodes.put("PN", 872);  // Pitcairn Islands
        countryPhoneCodes.put("PL", 48);   // Poland
        countryPhoneCodes.put("PT", 351);  // Portugal
        countryPhoneCodes.put("PR", 1);    // Puerto Rico
        countryPhoneCodes.put("QA", 974);  // Qatar
        countryPhoneCodes.put("RE", 262);  // Réunion
        countryPhoneCodes.put("RO", 40);   // Romania
        countryPhoneCodes.put("RU", 7);    // Russia
        countryPhoneCodes.put("RW", 250);  // Rwanda
        countryPhoneCodes.put("SH", 290);  // Saint Helena
        countryPhoneCodes.put("KN", 1869); // Saint Kitts and Nevis
        countryPhoneCodes.put("LC", 1758); // Saint Lucia
        countryPhoneCodes.put("PM", 508);  // Saint Pierre and Miquelon
        countryPhoneCodes.put("VC", 1784); // Saint Vincent and the Grenadines
        countryPhoneCodes.put("WS", 685);  // Samoa
        countryPhoneCodes.put("SM", 378);  // San Marino
        countryPhoneCodes.put("ST", 239);  // São Tomé and Príncipe
        countryPhoneCodes.put("SA", 966);  // Saudi Arabia
        countryPhoneCodes.put("SN", 221);  // Senegal
        countryPhoneCodes.put("RS", 381);  // Serbia
        countryPhoneCodes.put("SC", 248);  // Seychelles
        countryPhoneCodes.put("SL", 232);  // Sierra Leone
        countryPhoneCodes.put("SG", 65);   // Singapore
        countryPhoneCodes.put("SX", 1721); // Sint Maarten
        countryPhoneCodes.put("SK", 421);  // Slovakia
        countryPhoneCodes.put("SI", 386);  // Slovenia
        countryPhoneCodes.put("SB", 677);  // Solomon Islands
        countryPhoneCodes.put("SO", 252);  // Somalia
        countryPhoneCodes.put("ZA", 27);   // South Africa
        countryPhoneCodes.put("SS", 211);  // South Sudan
        countryPhoneCodes.put("ES", 34);   // Spain
        countryPhoneCodes.put("LK", 94);   // Sri Lanka
        countryPhoneCodes.put("SD", 249);  // Sudan
        countryPhoneCodes.put("SR", 597);  // Suriname
        countryPhoneCodes.put("SZ", 268);  // Swaziland
        countryPhoneCodes.put("SE", 46);   // Sweden
        countryPhoneCodes.put("CH", 41);   // Switzerland
        countryPhoneCodes.put("SY", 963);  // Syria
        countryPhoneCodes.put("TW", 886);  // Taiwan
        countryPhoneCodes.put("TJ", 992);  // Tajikistan
        countryPhoneCodes.put("TZ", 255);  // Tanzania
        countryPhoneCodes.put("TH", 66);   // Thailand
        countryPhoneCodes.put("TL", 670);  // Timor-Leste
        countryPhoneCodes.put("TG", 228);  // Togo
        countryPhoneCodes.put("TK", 690);  // Tokelau
        countryPhoneCodes.put("TO", 676);  // Tonga
        countryPhoneCodes.put("TT", 1868); // Trinidad and Tobago
        countryPhoneCodes.put("TN", 216);  // Tunisia
        countryPhoneCodes.put("TR", 90);   // Turkey
        countryPhoneCodes.put("TM", 993);  // Turkmenistan
        countryPhoneCodes.put("TV", 688);  // Tuvalu
        countryPhoneCodes.put("UG", 256);  // Uganda
        countryPhoneCodes.put("UA", 380);  // Ukraine
        countryPhoneCodes.put("AE", 971);  // United Arab Emirates
        countryPhoneCodes.put("GB", 44);   // United Kingdom
        countryPhoneCodes.put("US", 1);    // United States
        countryPhoneCodes.put("UY", 598);  // Uruguay
        countryPhoneCodes.put("UZ", 998);  // Uzbekistan
        countryPhoneCodes.put("VU", 678);  // Vanuatu
        countryPhoneCodes.put("VA", 379);  // Vatican City
        countryPhoneCodes.put("VE", 58);   // Venezuela
        countryPhoneCodes.put("VN", 84);   // Vietnam
        countryPhoneCodes.put("WF", 681);  // Wallis and Futuna
        countryPhoneCodes.put("EH", 212);  // Western Sahara
        countryPhoneCodes.put("YE", 967);  // Yemen
        countryPhoneCodes.put("ZM", 260);  // Zambia
        countryPhoneCodes.put("ZW", 263);  // Zimbabwe

        return countryPhoneCodes;
    }*/


    private static final String[] COUNTRY_CODES = {
            "AF", "AL", "DZ", "AD", "AO", "AG", "AR", "AM", "AU", "AT", "AZ", "BS", "BH", "BD", "BB", "BY", "BE", "BZ",
            "BJ", "BT", "BO", "BA", "BW", "BR", "BN", "BG", "BF", "BI", "CV", "KH", "CM", "CA", "CF", "TD", "CL", "CN",
            "CO", "KM", "CD", "CG", "CR", "CI", "HR", "CU", "CY", "CZ", "DK", "DJ", "DM", "DO", "EC", "EG", "SV", "GQ",
            "ER", "EE", "ET", "FJ", "FI", "FR", "GA", "GM", "GE", "DE", "GH", "GR", "GD", "GT", "GN", "GW", "GY", "HT",
            "HN", "HU", "IS", "IN", "ID", "IR", "IQ", "IE", "IL", "IT", "JM", "JP", "JO", "KZ", "KE", "KI", "KP", "KR",
            "KW", "KG", "LA", "LV", "LB", "LS", "LR", "LY", "LI", "LT", "LU", "MO", "MK", "MG", "MW", "MY", "MV", "ML",
            "MT", "MH", "MR", "MU", "MX", "FM", "MD", "MC", "MN", "MA", "MZ", "MM", "NA", "NR", "NP", "NL", "NZ", "NI",
            "NE", "NG", "NU", "NF", "MP", "NO", "OM", "PK", "PW", "PA", "PG", "PY", "PE", "PH", "PN", "PL", "PT", "PR",
            "QA", "RE", "RO", "RU", "RW", "SH", "KN", "LC", "PM", "VC", "WS", "SM", "ST", "SA", "SN", "RS", "SC", "SL",
            "SG", "SX", "SK", "SI", "SB", "SO", "ZA", "SS", "ES", "LK", "SD", "SR", "SZ", "SE", "CH", "SY", "TW", "TJ",
            "TZ", "TH", "TL", "TG", "TK", "TO", "TT", "TN", "TR", "TM", "TV", "UG", "UA", "AE", "GB", "US", "UY", "UZ",
            "VU", "VA", "VE", "VN", "WF", "EH", "YE", "ZM", "ZW"
    };


    private static final int[] PHONE_CODES = {
            93, 355, 213, 376, 244, 268, 54, 374, 61, 43, 994, 242, 973, 880, 246, 375, 32, 501, 229, 975, 591, 387,
            267, 55, 673, 359, 226, 257, 238, 855, 237, 1, 236, 235, 56, 86, 57, 269, 243, 242, 506, 225, 385, 53,
            357, 420, 45, 253, 1767, 1809, 593, 20, 503, 240, 291, 372, 251, 679, 358, 33, 241, 220, 995, 49, 233,
            30, 1473, 502, 224, 245, 592, 509, 504, 36, 354, 91, 62, 98, 964, 353, 972, 39, 1876, 81, 962, 7, 254,
            686, 850, 82, 965, 996, 856, 371, 961, 266, 231, 218, 423, 370, 352, 853, 389, 261, 265, 60, 960, 223,
            356, 692, 222, 230, 52, 691, 373, 377, 976, 212, 258, 95, 264, 674, 977, 31, 64, 505, 227, 234, 683, 672,
            1670, 47, 968, 92, 680, 507, 675, 595, 51, 63, 872, 48, 351, 1, 974, 262, 40, 7, 250, 290, 1869, 1758,
            508, 1784, 685, 378, 239, 966, 221, 381, 248, 232, 65, 1721, 421, 386, 677, 252, 27, 211, 34, 94, 249,
            597, 268, 46, 41, 963, 886, 992, 255, 66, 670, 228, 690, 676, 1868, 216, 90, 993, 688, 256, 380, 971,
            44, 1, 598, 998, 678, 379, 58, 84, 681, 212, 967, 260, 263
    };


    public static int getPhoneCode(String countryCode) {
        for (int i = 0; i < COUNTRY_CODES.length; i++) {
            if (COUNTRY_CODES[i].equals(countryCode)) {
                return PHONE_CODES[i];
            }
        }
        return 1; // Default to US if unknown

    }

}
