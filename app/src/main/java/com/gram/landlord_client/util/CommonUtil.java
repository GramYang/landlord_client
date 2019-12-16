package com.gram.landlord_client.util;



public class CommonUtil {

    public static boolean checkNotNull(String... strings) {
        boolean isNotNull = true;
        for(String s : strings) {
            if(s == null || s.equals("")) {
                isNotNull = false;
                break;
            }
        }
        return isNotNull;
    }


}
