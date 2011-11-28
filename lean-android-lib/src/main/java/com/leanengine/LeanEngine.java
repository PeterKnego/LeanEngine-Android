/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

/**
 * Main LeanEngine application context class, containing application-wide (singletons) data.
 * The {@link #init(android.content.Context, String)} must be called before using any other LeanEngine client functions.
 */
public class LeanEngine {

    private static String host;
    private static Uri hostUri;
    private static Uri facebookLoginUri;
    private static Uri googleLoginUri;
    private static Uri yahooLoginUri;
    private static Context appContext;
    private static String authToken;

    /**
     * Initializes the LeanEngine client. Must be called before using other LeanEngine functions.
     *
     * @param context Android Context.
     * @param host    The path to the host server, i.e. 'http://demo.lean-engine.com'
     */
    public static void init(Context context, String host) {
        LeanEngine.appContext = context.getApplicationContext();

        if (LeanEngine.host != null && !host.equals(LeanEngine.host)) {
            resetAuthToken();
        }

        LeanEngine.host = host;
        LeanEngine.facebookLoginUri = Uri.parse(host + "/facebook?type=mobile");
        LeanEngine.googleLoginUri = Uri.parse(host + "/openid?provider=google&type=mobile");
        LeanEngine.yahooLoginUri = Uri.parse(host + "/openid?provider=yahoo&type=mobile");
        LeanEngine.hostUri = Uri.parse(host);

        authToken = loadAuthToken();
    }

    /**
     * Returns currently used authentication token as set via {@link LeanEngine#storeAuthTokenToPreferences(String)}.
     * This token is internally saved in app preferences and survives app restarts.
     * <br/><br/>
     * It is used to authenticate against server.
     * Token might be expired - this can be checked via {@link com.leanengine.LeanAccount#getAccountData()}.
     *
     * @return Authentication token as provided by server when authentication procedure is successfully completed.
     */
    public static String getAuthToken() {
        return authToken;
    }

    /**
     * Saves authentication token to app preferences.
     *
     * @param authToken Authentication token acquired via authentication flow.
     */
    protected static void saveAuthData(String authToken) {
        LeanEngine.authToken = authToken;
        storeAuthTokenToPreferences(authToken);
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
        return Uri.parse(host + "/openid?provider=" + provider + "&type=mobile");
    }

    private static void storeAuthTokenToPreferences(String token) {
        SharedPreferences preferences = appContext.getSharedPreferences("leanengine", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_auth_token", token);
        editor.commit();
    }

    protected static void resetAuthToken() {
        authToken = null;
        SharedPreferences preferences = appContext.getSharedPreferences("leanengine", 0);
        if (preferences == null) return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("_auth_token");
        editor.commit();
    }

    protected static void clearCookies() {
        CookieSyncManager.createInstance(getAppContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    private static String loadAuthToken() {
        SharedPreferences preferences = appContext.getSharedPreferences("leanengine", 0);
        if (preferences == null) return null;
        return preferences.getString("_auth_token", null);
    }


}
