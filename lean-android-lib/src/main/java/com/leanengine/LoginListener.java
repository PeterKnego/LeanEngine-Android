package com.leanengine;

import com.leanengine.rest.RestException;

import java.util.Map;

public interface LoginListener {

    public void onSuccess(String token);

    public void onCancel();


    /**
     * Called when lofgin error happens.
     * Error codes depend on provider:
     * Facebook: http://developers.facebook.com/docs/oauth/errors/
     * Twitter:
     */
    public void onError(RestException exception);

}
