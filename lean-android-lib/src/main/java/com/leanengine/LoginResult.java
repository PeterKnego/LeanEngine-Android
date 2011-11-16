/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import android.net.Uri;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class LoginResult {

    private String host;
    private String authToken;
    private int errorCode = 0;
    private String errorMessage;

    public LoginResult(String authToken) {
        this.authToken = authToken;
    }

    public static LoginResult parse(Uri data) {
        if (data == null) return null;

        LoginResult loginResult = new LoginResult(data.getQueryParameter("auth_token"));
        loginResult.host = data.getHost();

        if (data.getQueryParameter("errorcode") != null)
            loginResult.errorCode = Integer.parseInt(data.getQueryParameter("errorcode"));

        try {
            if (data.getQueryParameter("errormsg") != null)
                loginResult.errorMessage = URLDecoder.decode(data.getQueryParameter("errormsg"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            // should not happen - UTF-8 is always supported
        }
        return loginResult;
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
