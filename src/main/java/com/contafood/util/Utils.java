package com.contafood.util;

public class Utils {

    public static boolean stringContainsOnlyCertainCharacters(String input){
        for (int i = 0; i < input.length(); i++) {
            char ch = input.charAt(i);
            if(!Constants.BARCODE_ALLOWED_CHARS.contains(ch)){
                return false;
            }
        }
        return true;
    }
}
