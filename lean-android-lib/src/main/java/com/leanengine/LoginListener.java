package com.leanengine;

public interface LoginListener {

    public void onSuccess(String token);

    public void onCancel();


    /**
     * Called when lofgin error happens.
     * Error codes depend on provider:
     * Facebook: http://developers.facebook.com/docs/oauth/errors/
     * Twitter:
     */
    public void onError(LeanError error);

}
