package com.leanengine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class LeanAccount {

    /**
     * Starts a Facebook login procedure in a WebView.
     *
     * @param facebookApplicationID Facebook Application ID as provided by Facebook.
     * @param webView               WebView where a web-base Facebook login procedure will be shown. WebView's properties will be
     *                              altered to suit the login procedure. WebView must not be manipulated after passed to this method!
     * @param loginListener         LoginListener's methods are called when login is completed.
     */
    public static void loginFacebook(String facebookApplicationID, WebView webView, LoginListener loginListener) {

    }

    /**
     * Starts a Facebook login procedure in external web browser.
     *
     * Application must have a custom scheme 'leanengine:appname'
     */
    public static void loginFacebookInBrowser() {


        // get the app context - it also checks that LeanEngine was properly initialized
        Context appContext = LeanEngine.getAppContext();

        Uri loginUri = LeanEngine.getFacebookLoginUri();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, loginUri);
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        appContext.startActivity(browserIntent);
    }

    public static void logout() {
        LeanEngine.resetLoginData();
    }

    public static boolean isLoggedIn() {
        return LeanEngine.getLoginData() != null;
    }
}
