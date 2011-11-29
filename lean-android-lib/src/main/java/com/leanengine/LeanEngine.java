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
 * The main LeanEngine context class, containing application-wide data.
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
     * @throws IllegalStateException If LeanEngine was not initialized before first use,
     *                               via {@link #init(android.content.Context, String)} method.
     */
    static String getAuthToken() throws IllegalStateException {
        if (LeanEngine.appContext == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }
        return authToken;
    }

    /**
     * Saves authentication token to app preferences.
     *
     * @param authToken Authentication token acquired via authentication flow.
     * @throws IllegalStateException If LeanEngine was not initialized before first use,
     *                               via {@link #init(android.content.Context, String)} method.
     */
    protected static void saveAuthData(String authToken) throws IllegalStateException {
        if (LeanEngine.appContext == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }
        LeanEngine.authToken = authToken;
        storeAuthTokenToPreferences(authToken);
    }

    /**
     * Returns the Uri of the host, as initialized via {@link LeanEngine#init(Context, String)}
     *
     * @return The Uri of the host server.
     * @throws IllegalStateException If LeanEngine was not initialized before first use,
     *                               via {@link #init(android.content.Context, String)} method.
     */
    public static Uri getHostURI() throws IllegalStateException {
        if (LeanEngine.hostUri == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }
        return hostUri;
    }

    static Context getAppContext() throws IllegalStateException {
        if (LeanEngine.appContext == null) {
            throw new IllegalStateException("Before use LeanEngine must be initialized with \"LeanEngine.init(context, hostPath)\"");
        }

        return appContext;
    }

    /**
     * Returns the server authentication Uri which uses Facebook OAuth authentication service.
     *
     * @return The server authentication Uri.
     */
    public static Uri getFacebookLoginUri() {
        return facebookLoginUri;
    }

    /**
     * Returns the server authentication Uri which uses Google OpenID authentication service.
     *
     * @return The server authentication Uri.
     */
    public static Uri getGoogleLoginUri() {
        return googleLoginUri;
    }

    /**
     * Returns the server authentication Uri which uses Yahoo OpenID authentication service.
     *
     * @return The server authentication Uri.
     */
    public static Uri getYahooLoginUri() {
        return yahooLoginUri;
    }

    /**
     * Returns the Uri of OpenID authentication service.
     *
     * @param provider The URL of the OpenID service.
     * @return The server authentication Uri.
     */
    public static Uri getOpenIdLoginUri(String provider) {
        return Uri.parse(host + "/openid?provider=" + provider + "&type=mobile");
    }

    static void storeAuthTokenToPreferences(String token) {
        SharedPreferences preferences = appContext.getSharedPreferences("_lean_engine_", 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("_auth_token", token);
        editor.commit();
    }

    static void resetAuthToken() {
        authToken = null;
        SharedPreferences preferences = appContext.getSharedPreferences("_lean_engine_", 0);
        if (preferences == null) return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("_auth_token");
        editor.commit();
    }

    static void clearCookies() {
        CookieSyncManager.createInstance(getAppContext());
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.removeAllCookie();
    }

    static String loadAuthToken() {
        SharedPreferences preferences = appContext.getSharedPreferences("_lean_engine_", 0);
        if (preferences == null) return null;
        return preferences.getString("_auth_token", null);
    }


}
