/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import android.content.Context;
import android.net.Uri;

public class LeanEngine {

    private static String host;
    private static Uri hostUri;
    private static Uri facebookLoginUri;
    private static Uri googleLoginUri;
    private static Uri yahooLoginUri;
    private static Context appContext;
    private static LoginResult loginResult;

    /**
     * Initializes the LeanEngine client.
     *
     * @param context Android Context.
     * @param host    The path to the host server, i.e. 'http://demo.lean-engine.com'
     */
    public static void init(Context context, String host) {
        LeanEngine.host = host;
        LeanEngine.facebookLoginUri = Uri.parse(host + "/facebook?type=mobile");
        LeanEngine.googleLoginUri = Uri.parse(host + "/openid?provider=google&type=mobile");
        LeanEngine.yahooLoginUri = Uri.parse(host + "/openid?provider=yahoo&type=mobile");
        LeanEngine.hostUri = Uri.parse(host);
        LeanEngine.appContext = context.getApplicationContext();
    }

    public static LoginResult getLoginResult() {
        return loginResult;
    }

    public static void setAuthData(String authToken){
         if(authToken!=null)
           loginResult = new LoginResult(authToken);
    }

    /**
     * Returns the Uri of the host, as initialized via {@link LeanEngine#init(Context, String)}
     *
     * @return The Uri of the host server.
     */
    public static Uri getHostURI() {
        if (LeanEngine.hostUri == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }
        return hostUri;
    }

    public static Context getAppContext() {

        if (LeanEngine.appContext == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }
        return appContext;
    }

    public static Uri getFacebookLoginUri() {
        return facebookLoginUri;
    }

    public static Uri getGoogleLoginUri() {
        return googleLoginUri;
    }

    public static Uri getYahooLoginUri() {
        return yahooLoginUri;
    }

    public static Uri getOpenIdLoginUri(String provider) {
        return Uri.parse(host + "/openid?provider="+provider+"&type=mobile");
    }

    protected static void resetLoginData() {
        loginResult = null;
        //todo must clear all caches
    }
}
