package com.leanengine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import com.leanengine.rest.NetworkCallback;
import com.leanengine.rest.RestException;

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
     * <p/>
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

    public static void loginFacebook(final WebView webView, final NetworkCallback<Long> authCallback) {


        webView.setWebViewClient(new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                Log.e("FacebookOAuth", "shouldOverrideUrlLoading url:" + url);

                if (url != null && url.startsWith("leanengine://")) {
                    UrlQuerySanitizer query = new UrlQuerySanitizer(url);

                    String token = query.getValue("auth_token");
                    if (token != null) {
                        authCallback.onResult();
                    } else {
                        String errorCode = query.getValue("errorcode");
                        String errorMsg = query.getValue("errormsg");
                        authCallback.onFailure(new RestException(401, "errorCode=" + errorCode + " errorMsg=" + errorMsg));
                    }
                    return true;
                }
                return false;
            }
        });

        webView.loadUrl(LeanEngine.getFacebookLoginUri().toString());
    }

    public static void logout() {
        LeanEngine.resetLoginData();
    }

    public static boolean isLoggedIn() {
        return LeanEngine.getLoginData() != null;
    }
}
