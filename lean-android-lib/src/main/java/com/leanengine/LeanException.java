package com.leanengine;

/**
 * Exception class wrapping LeanError
 */
public class LeanException extends RuntimeException {

    private LeanError error;

    public LeanException(LeanError error) {
        this.error = error;
    }
}
