/*
 * This software is released under the GNU Lesser General Public License v3.
 * For more information see http://www.gnu.org/licenses/lgpl.html
 *
 * Copyright (c) 2011, Peter Knego & Matjaz Tercelj
 * All rights reserved.
 */

package com.leanengine;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Main error class containing all error codes.
 * <p/>
 * There are four main types of errors:
 * <br/><br/>
 * 1. Network error, when network connection is not available or server is not accessible.
 * <br/>
 * 2. Not authorized error, when user is not logged in.
 * <br/>
 * 3. Request error, indicating there are wrong parameters in request.
 * <br/>
 * 4. Server error, when server could not fulfill the request.
 * <br/><br/>
 * When an error occurs, the application should note the error type and notify the user accordingly.
 * <br/><br/>
 * Every error also contains the detailed error code and description, indicating the cause or error. This error codes
 * and descriptions are not intended for end-user, but rather to pinpoint the cause of error.
 */
public class LeanError {

    public enum Type {

        /**
         * Indicates error was produced by the client, e.g. wrong arguments were supplied, etc..
         */
        RequestError(0),
        /**
         * Happens when user is not logged in.
         */
        NotAuthorizedError(1),
        /**
         * Server could not fulfill the request.
         */
        ServerError(2),
        /**
         * Network connection is not available or server is not accessible.
         */
        NetworkError(3);

        private int errorCode;

        Type(int errorCode) {
            this.errorCode = errorCode;
        }

        public static Type byType(int type) {
            switch (type) {
                case 0:
                    return RequestError;
                case 1:
                    return NotAuthorizedError;
                case 2:
                    return ServerError;
                case 3:
                    return NetworkError;
            }
            return null;
        }
    }

    private Type errorType;
    private int errorCode;
    private String errorMessage;

    private LeanError(int errorCode, String errorMessage) {
        this.errorType = Type.byType(errorCode / 100);
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    LeanError(Type errorType) {
        this.errorType = errorType;
    }

    LeanError(Type errorType, String additionalMessage) {
        this.errorType = errorType;
        this.errorMessage = additionalMessage;
    }

    /**
     * Returns the detailed error code, indicating the cause of error.
     * @return Error code
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the type of error as described in {@link LeanError.Type}
     * @return {@link LeanError.Type}
     */
    public Type getErrorType() {
        return errorType;
    }

    /**
     * Returns the detailed error message, describing the cause of error.
     * <br/><br/>
     * This error descriptions are not intended for end-user, instead they should be recorded in the error log,
     * to later pin-point the cause of error.
     * @return Description of error.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    static LeanError fromJSON(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            return new LeanError(json.getInt("code"), json.getString("message"));
        } catch (JSONException e) {
            return new LeanError(Type.ServerError, "Error reply is not in JSON format.");
        }
    }
}
