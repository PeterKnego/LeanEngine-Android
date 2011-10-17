package com.leanengine.rest;

import org.json.JSONException;

import java.io.IOException;

public class RestException extends IOException {

    private int httpResponseCode = 0;
    private boolean isUnauthorized = false;
    private Throwable cause;

    public RestException(int httpCode, String message) {
        super(message);
    }

    public RestException(Exception e) {
         cause = e;
    }

    public boolean isUnauthorized() {
        return isUnauthorized;
    }
}
