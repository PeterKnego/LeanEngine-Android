package com.leanengine;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;

public class LeanEngine {

    private static Uri hostUri;
    private static Uri facebookLoginUri;
    private static Context appContext;
    private static LoginData loginData;

    /**
     * Initializes the LeanEngine client.
     *
     * @param context Android Context.
     * @param host    The path to the host server, i.e. 'http://demo.lean-engine.com'
     */
    public static void init(Context context, String host) {
        LeanEngine.facebookLoginUri = Uri.parse(host + "/facebook?type=mobile");
        LeanEngine.hostUri = Uri.parse(host);
        LeanEngine.appContext = context.getApplicationContext();
    }

    public static LoginData getLoginData() {
        return loginData;
    }

    public static LoginData handleLogin(Activity activity) {
        // the URI that invoked the Activity - used when returning from browser login
        Uri intentData = activity.getIntent().getData();
        LoginData newLoginData = null;
        if (intentData != null) {
            newLoginData = LoginData.parse(intentData);
            if (loginData == null && newLoginData != null) {
                loginData = newLoginData;
                return newLoginData;
            }
        }
        return null;
    }

    public static void setAuthData(String authToken){
         if(authToken!=null)
           loginData = new LoginData(authToken);
    }

    /**
     * Returns the Uri of the host, as initialized via {@link LeanEngine#init(Context, String)}
     *
     * @return The Uri of the host server.
     */
    public static Uri getHost() {
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

    protected static void resetLoginData() {
        loginData = null;
        //todo must clear all caches
    }
}
