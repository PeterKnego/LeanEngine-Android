package com.leanengine;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LoginData {

    private String host;
    private String authToken;
    private int errorCode = 0;
    private String errorMessage;

    public static LoginData parse(Uri data) {
        if (data == null) return null;

        LoginData loginData = new LoginData();
        loginData.host = data.getHost();
        loginData.authToken = data.getQueryParameter("auth_token");

        if (data.getQueryParameter("errorcode") != null)
            loginData.errorCode = Integer.parseInt(data.getQueryParameter("errorcode"));

        try {
            if (data.getQueryParameter("errormsg") != null)
                loginData.errorMessage = URLDecoder.decode(data.getQueryParameter("errormsg"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should not happen - UTF-8 is always supported
        }
        return loginData;
    }

    public String getHost() {
        return host;
    }

    public String getAuthToken() {
        return authToken;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
