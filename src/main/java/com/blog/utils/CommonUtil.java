package com.blog.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Value;

import java.util.regex.Pattern;

public class CommonUtil {

    public static Boolean usernameVerify(String username){
        if(username!=null){
            return Pattern.matches("^[a-zA-Z0-9]{3,20}$", username);
        }
        return false;
    }


    public static Boolean emailVerify(String email){
        if(email!=null){
            return Pattern.matches("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", email);
        }
        return false;
    }
}
