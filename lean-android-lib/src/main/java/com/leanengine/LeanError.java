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
 * Errors can happen on server side, in which case error codes will be below 100.
 * Client side generated errors have error codes above 100.
 */
public class LeanError {

    public enum Error {

        ReplyError(0),
        NotAuthorizedError(1),
        ServerError(2),
        NetworkError(3);

        public int errorCode;

        Error(int errorCode) {
            this.errorCode = errorCode;
        }
    }

    private int errorCode;
    private int detailCode;
    private String errorMessage;

    private LeanError(int detailCode, String errorMessage) {
        this.errorCode = detailCode / 100;
        this.detailCode = detailCode;
        this.errorMessage = errorMessage;
    }

    protected LeanError(Error error) {
        this.errorCode = error.errorCode;
    }

    protected LeanError(Error error, String additionalMessage) {
        this.errorCode = error.errorCode;
        this.errorMessage = additionalMessage;
    }

    public int getDetailCode() {
        return detailCode;
    }

    public int getErrorCode() {
        return errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public static LeanError fromJSON(String jsonString) {
        JSONObject json = null;
        try {
            json = new JSONObject(jsonString);
            return new LeanError(json.getInt("code"), json.getString("message"));
        } catch (JSONException e) {
            return new LeanError(Error.ServerError, "Error reply is not in JSON format.");
        }
    }
}
