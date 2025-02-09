package io.aryby.spring_boot_crud.util;

public class CapitalizeFirstChar {
    public static String capitalizeFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = Character.toUpperCase(str.charAt(0));
        return firstChar + str.substring(1);
    }


    public static String lowerCaseFirstLetter(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        char firstChar = Character.toLowerCase(str.charAt(0));
        return firstChar + str.substring(1);
    }
}
