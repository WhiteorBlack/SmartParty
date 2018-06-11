package com.qiantang.smartparty.utils;


import com.qiantang.smartparty.MyApplication;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {
    public static boolean isEmpty(String value) {
        return value == null || "".equals(value.trim());
    }

    public static String getString(int resId, Object... formatArgs) {
        return MyApplication.getContext().getString(resId, formatArgs);
    }

    public static String getUrlValue(String url, String key) {
        int start = url.indexOf(key) + key.length();
        int i = url.indexOf("&", start);
        return url.substring(start, i == -1 ? url.length() : i);
    }

    /*首字母大写*/
    public static String capitalize(String value) {
        if (isEmpty(value)) return "";
        String first = value.substring(0, 1).toUpperCase();
        return value.length() == 1 ? first : first + value.substring(1);
    }

    public static boolean checkPassword(String password) {
        if (isEmpty(password)) {
            return false;
        }
        final String passwordRegular = "^[a-zA-Z0-9]{6,15}";
        Pattern p = Pattern.compile(passwordRegular);
        Matcher m = p.matcher(password);
        return m.matches();
    }

    public static boolean isPhoneNumberValid(String phoneNumber) {
        boolean isValid = false;
        CharSequence inputStr = phoneNumber;
        //正则表达式
        String phone = "^1[34578]\\d{9}$";
        Pattern pattern = Pattern.compile(phone);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static String getActivityStatus(int status) {
        String text = "";
        switch (status) {
            case 1:
                text = "待发布";
                break;
            case 2:
                text = "未开始";
                break;
            case 3:
                text = "报名中";
                break;
            case 4:
                text = "进行中";
                break;
            case 5:
                text = "已结束";
                break;
        }
        return text;
    }

    public static String getCommentCount(int count){
        return "("+count+"条)";
    }

    public static boolean isContains(String shareUrl, String... keys) {
        for (String key : keys) {
            if (shareUrl.contains(key)) {
                return true;
            }
        }
        return false;
    }


}
