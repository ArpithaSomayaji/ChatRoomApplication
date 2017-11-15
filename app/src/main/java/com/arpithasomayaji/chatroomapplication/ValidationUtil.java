package com.arpithasomayaji.chatroomapplication;

import java.util.regex.Pattern;

/**
 * Created by Arpitha.Somayaji on 10/23/2017.
 */
public class ValidationUtil {

    private final static String EMAIL_FORMAT = "^.+@.+\\.[A-Za-z]{2}[A-Za-z]*$";
    public static boolean isTextEmpty(String str) {
        return (str == null || str.isEmpty());
    }
    public static boolean isEmailFormatValid(String emailText){
        return (Pattern.matches(EMAIL_FORMAT, emailText));
    }

}
