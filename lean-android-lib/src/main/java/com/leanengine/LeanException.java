package com.leanengine;

import java.io.IOException;

/**
 * Exception class wrapping LeanError
 */
public class LeanException extends IOException {

    private LeanError error;

    public LeanException(LeanError error) {
        this.error = error;
    }

    public LeanError getError() {
        return error;
    }
}
