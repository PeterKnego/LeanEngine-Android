package com.leanengine;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LoginData {

    private String host;
    private String authToken;
    private int errorCode = 0;
    private String errorMessage;

    public LoginData(String authToken) {
        this.authToken = authToken;
    }

    public static LoginData parse(Uri data) {
        if (data == null) return null;

        LoginData loginData = new LoginData(data.getQueryParameter("auth_token"));
        loginData.host = data.getHost();

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
