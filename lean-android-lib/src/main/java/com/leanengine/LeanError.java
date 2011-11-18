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
 *
 * Errors can happen on server side, in which case error codes will be below 100.
 * Client side generated errors have error codes above 100.
 */
public class LeanError {

    public enum Error {
        // input errors have codes above 100
        // they happen when client sends wrong requests
        IllegalEntityName(101, "Illegal LeanEntity name."),
        EmptyEntity(102, "LeanEntity contains no properties."),
        IllegalEntityFormat(103, "Illegal LeanEntity format."),
        EntityNotFound(104, "Entity not found."),
        QueryJSON(105, "Query JSON could not be parsed."),
        UnsupportedQueryFilterOperation(106, "Query contains unsupported filter operation: "),
        UnsupportedQuerySortOperation(107, "Query contains unsupported sort operation: "),
        ValueToJSON(108, "Value node could not be converted to a supported type."),
        LeanExceptionToJSON(120, "Error parsing error JSON data."),
        NoAccountAuthorized(121, "No account authorized to access server."),
        ServerNotAccessible(122, "Server is not accessible."),
        ErrorParsingJSON(123, "Server JSON could not be parsed."),
        RestTaskExecutionError(124, "REST background task execution error."),
        RestTaskInterrupted(125, "REST background task was interrupted"),
        CreatingJsonError(126, "Error adding property to JSON object."),
        JsonMissingField(127, "JSON missing required field."),

        // server errors have codes below 100
        // they happen when server has problems fulfilling request
        FacebookAuthError(1, "Facebook authorization error."),
        FacebookAuthParseError(2, "Facebook authorization error."),
        FacebookAuthConnectError(3, "Could not connect to Facebook authorization server."),
        FacebookAuthResponseError(4, "Facebook OAuth server error."),
        FacebookAuthMissingParam(5, "OAuth error: missing parameters in server reply."),
        FacebookAuthNoConnection(6, "Could not connect to Facebook authorization server."),
        FacebookAuthNotEnabled(7, "Server configuration error: Facebook login not enabled."),
        FacebookAuthMissingAppId(8, "Server configuration error: missing Facebook Application ID."),
        FacebookAuthMissingAppSecret(9, "Server configuration error: missing Facebook Application Secret."),
        FacebookAuthMissingCRSF(10, "Facebook OAuth request missing CSRF protection code."),
        OpenIdAuthFailed(11, "OpenID authentication failed."),
        OpenIdAuthNotEnabled(12, "Server configuration error: OpenID login not enabled."),
        ScriptExecutionError(20, "Error executing script: "),
        ScriptOutputError(21, "Illegal script result error: custom scripts must produce a Javascript object. Script: "),
        NotAuthorized(40, "No account active or account not authorized to access this resource."),
        AppEngineMissingIndex(41, "AppEngine query error: missing index. Try running this query on dev server to " +
                "automatically create needed indexes and then upload to production."),
        ServerSessionsNotEnabled(42, "Sessions not enabled on server."),
        MissingRedirectUrl(43, "Login request must have URL parameter 'onlogin' used for redirect on successful login."),
        ErrorSerializingToJson(44, "Object could not be serialized to JSON");


        public int errorCode;
        public String errorMessage;

        Error(int errorCode, String errorMessage) {
            this.errorCode = errorCode;
            this.errorMessage = errorMessage;
        }
    }

    private int errorCode;
    private String errorMessage;

    private LeanError(int errorCode, String errorMessage) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public LeanError(Error error) {
        this.errorCode = error.errorCode;
        this.errorMessage = error.errorMessage;
    }

    public LeanError(Error error, String additionalMessage) {
        this.errorCode = error.errorCode;
        this.errorMessage = error.errorMessage + additionalMessage;
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
            return new LeanError(Error.LeanExceptionToJSON);
        }
    }
}
