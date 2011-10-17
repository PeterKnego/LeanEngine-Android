package com.leanengine;

import java.util.Map;

public interface LoginListener {

    public void onSuccess(LeanAccount account);

    /**
     * Called when lofgin error happens.
     * @param replyCodes A map of error codes.
     * Error codes depend on provider:
     * Facebook: http://developers.facebook.com/docs/oauth/errors/
     * Twitter:
     */
    public void onError(Map<String, String> replyCodes);

}
