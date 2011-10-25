package com.leanengine;

import org.json.JSONException;
import org.json.JSONObject;

public class LeanError {

    public enum Error {
        // input errors have codes above 100
        // they happen when client sends wrong requests
        IllegalEntityName(101, "Illegal LeanEntity name."),
        EmptyEntity(102, "LeanEntity contains no properties."),
        IllegalEntityFormat(103, "Illegal LeanEntity format."),
        EntityNotFound(104, "Entity not found."),
        EntityToJSON(105, "Entity missing "),
        LeanExceptionToJSON(106, "Error parsing error JSON data."),
        NoAccountAuthorized(107, "No account authorized to access server."),
        ServerNotAccessible(108, "Server is not accessible."),
        ReplyNotJSON(109, "Server reply is not a valid JSON."),
        RestTaskExecutionError(110, "REST background task execution error."),
        RestTaskInterrupted(111, "REST background task was interrupted"),

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
        ScriptOutputError(21, "Illegal script result error: custom scripts must produce a Javascript object. Script: ");

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