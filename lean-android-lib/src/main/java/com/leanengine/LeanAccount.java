package com.leanengine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.webkit.WebView;

public class LeanAccount {

    /**
     * Starts a Facebook login procedure in external web browser.
     * <p/>
     * Application must have a custom scheme 'leanengine:appname'
     *
     * @param context Context used for starting the browser intent.
     */
    public static void loginFacebookInBrowser(Context context) {

        Uri loginUri = LeanEngine.getFacebookLoginUri();
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, loginUri);
        browserIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(browserIntent);
    }

    //todo make public after it's done
    /**
        * Starts a Facebook login procedure in a WebView.
        *
        * @param webView               WebView where a web-base Facebook login procedure will be shown. WebView's properties will be
        *                              altered to suit the login procedure. WebView must not be manipulated after passed to this method!
        * @param authCallback          Callback
        */
    private static void loginFacebook(final WebView webView, final NetworkCallback<Long> authCallback) {

        //todo enable user passing in their own WebView

    }

    public static void logout() {
        LeanEngine.resetLoginData();
    }

    public static boolean isLoggedIn() {
        return LeanEngine.getLoginData() != null;
    }
}
