package com.leanengine;

import java.io.IOException;

/**
 * Exception class wrapping LeanError
 */
public class LeanException extends IOException {

    private LeanError error;
    private String message;

     public LeanException(LeanError leanError) {
        this.error = leanError;
    }

    public LeanException(LeanError.Error error) {
        this.error = new LeanError(error);
    }

     public LeanException(LeanError.Error error, String message) {
         this.error = new LeanError(error);
         this.message = message;
     }

    public LeanError getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}
