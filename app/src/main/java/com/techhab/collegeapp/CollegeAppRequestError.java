package com.techhab.collegeapp;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import java.net.HttpURLConnection;

/**
 * Created by jhchoe on 10/16/14.
 *
 * This class handles errors during request (authentication)
 */
public final class CollegeAppRequestError {

    /** Represents an invalid or unknown error code from the server. */
    public static final int INVALID_ERROR_CODE = -1;

    /**
     * Indicates that there was no valid HTTP status code returned, indicating
     * that either the error occurred locally, before the request was sent, or
     * that something went wrong with the HTTP connection. Check the exception
     * from getException()
     */
    public static final int INVALID_HTTP_STATUS_CODE = -1;

    private static final int INVALID_MESSAGE_ID = 0;

    private static final String CODE_KEY = "code";
    private static final String BODY_KEY = "body";
    private static final String ERROR_KEY = "error";
    private static final String ERROR_TYPE_FIELD_KEY = "type";
    private static final String ERROR_CODE_FIELD_KEY = "code";
    private static final String ERROR_MESSAGE_FIELD_KEY = "message";
    private static final String ERROR_CODE_KEY = "error_code";
    private static final String ERROR_SUB_CODE_KEY = "error_subcode";
    private static final String ERROR_MSG_KEY = "error_msg";
    private static final String ERROR_REASON_KEY = "error_reason";
    private static final String ERROR_USER_TITLE_KEY = "error_user_title";
    private static final String ERROR_USER_MSG_KEY = "error_user_msg";
    private static final String ERROR_IS_TRANSIENT_KEY = "is_transient";
    public static final String NON_JSON_RESPONSE_PROPERTY = "COLLEGE_APP_NON_JSON_RESULT";

    private static class Range {
        private final int start, end;

        private Range(int start, int end) {
            this.start = start;
            this.end = end;
        }

        boolean contains(int value) {
            return start <= value && value <= end;
        }
    }

    private static final int EC_UNKNOWN_ERROR = 1;
    private static final int EC_SERVICE_UNAVAILABLE = 2;
    private static final int EC_APP_TOO_MANY_CALLS = 4;
    private static final int EC_USER_TOO_MANY_CALLS = 17;
    private static final int EC_PERMISSION_DENIED = 10;
    private static final int EC_INVALID_SESSION = 102;
    private static final int EC_INVALID_TOKEN = 190;
    private static final Range EC_RANGE_PERMISSION = new Range(200, 299);
    private static final int EC_APP_NOT_INSTALLED = 458;
    private static final int EC_USER_CHECKPOINTED = 459;
    private static final int EC_PASSWORD_CHANGED = 460;
    private static final int EC_EXPIRED = 463;
    private static final int EC_UNCONFIRMED_USER = 464;

    private static final Range HTTP_RANGE_SUCCESS = new Range(200, 299);
    private static final Range HTTP_RANGE_CLIENT_ERROR = new Range(400, 499);
    private static final Range HTTP_RANGE_SERVER_ERROR = new Range(500, 599);

    private final int userActionMessageId;
    private final boolean shouldNotifyUser;
    private final Category category;
    private final int requestStatusCode;
    private final int errorCode;
    private final int subErrorCode;
    private final String errorType;
    private final String errorMessage;
    private final String errorUserTitle;
    private final String errorUserMessage;
    private final boolean errorIsTransient;
    private final JSONObject requestResult;
    private final JSONObject requestResultBody;
    private final Object batchRequestResult;
    private final HttpURLConnection connection;
    private final CollegeAppException exception;

    private CollegeAppRequestError(int requestStatusCode, int errorCode,
                                 int subErrorCode, String errorType, String errorMessage, String errorUserTitle, String errorUserMessage,
                                 boolean errorIsTransient, JSONObject requestResultBody, JSONObject requestResult, Object batchRequestResult,
                                 HttpURLConnection connection, CollegeAppException exception) {

        this.requestStatusCode = requestStatusCode;
        this.errorCode = errorCode;
        this.subErrorCode = subErrorCode;
        this.errorType = errorType;
        this.errorMessage = errorMessage;
        this.requestResultBody = requestResultBody;
        this.requestResult = requestResult;
        this.batchRequestResult = batchRequestResult;
        this.connection = connection;
        this.errorUserTitle = errorUserTitle;
        this.errorUserMessage = errorUserMessage;
        this.errorIsTransient = errorIsTransient;

        boolean isLocalException = false;
        if (exception != null) {
            this.exception = exception;
            isLocalException =  true;
        }
        else {
            this.exception = new CollegeAppServiceException(this, errorMessage);
        }

        // Initializes the error categories based on the documented error codes as outlined here
        // https://developers.facebook.com/docs/reference/api/errors/
        Category errorCategory = null;
        int messageId = INVALID_MESSAGE_ID;
        boolean shouldNotify = false;
        if (isLocalException) {
            errorCategory = Category.CLIENT;
            messageId = INVALID_MESSAGE_ID;
        }
        else {
            if (errorCode == EC_UNKNOWN_ERROR || errorCode == EC_SERVICE_UNAVAILABLE) {
                errorCategory = Category.SERVER;
            }
            else if (errorCode == EC_APP_TOO_MANY_CALLS || errorCode == EC_USER_TOO_MANY_CALLS) {
                errorCategory = Category.THROTTLING;
            }
            else if (errorCode == EC_INVALID_SESSION || errorCode == EC_INVALID_TOKEN) {
                if (subErrorCode == EC_USER_CHECKPOINTED || subErrorCode == EC_UNCONFIRMED_USER) {
                    errorCategory = Category.AUTHENTICATION_RETRY;
                    messageId = R.string.com_techhab_requesterror_web_login;
                    shouldNotify = true;
                }
                else {
                    errorCategory = Category.AUTHENTICATION_REOPEN_SESSION;

                    if ((subErrorCode == EC_APP_NOT_INSTALLED) || (subErrorCode == EC_EXPIRED)) {
                        messageId = R.string.com_techhab_requesterror_relogin;
                    }
                    else if (subErrorCode == EC_PASSWORD_CHANGED) {
                        messageId = R.string.com_techhab_requesterror_password_changed;
                    }
                    else {
                        messageId = R.string.com_techhab_requesterror_reconnect;
                        shouldNotify = true;
                    }
                }
            }

            if (errorCategory == null) {
                if (HTTP_RANGE_CLIENT_ERROR.contains(requestStatusCode)) {
                    errorCategory = Category.BAD_REQUEST;
                }
                else if (HTTP_RANGE_SERVER_ERROR.contains(requestStatusCode)) {
                    errorCategory = Category.SERVER;
                }
                else {
                    errorCategory = Category.OTHER;
                }
            }
        }

        // Notify user when error_user_msg is present
        shouldNotify = errorUserMessage!= null && errorUserMessage.length() > 0;

        this.category = errorCategory;
        this.userActionMessageId = messageId;
        this.shouldNotifyUser = shouldNotify;
    }

    private CollegeAppRequestError(int requestStatusCode, int errorCode,
                                 int subErrorCode, String errorType, String errorMessage, String errorUserTitle, String errorUserMessage,
                                 boolean errorIsTransient, JSONObject requestResultBody, JSONObject requestResult, Object batchRequestResult,
                                 HttpURLConnection connection) {
        this(requestStatusCode, errorCode, subErrorCode, errorType, errorMessage, errorUserTitle, errorUserMessage,
                errorIsTransient, requestResultBody, requestResult, batchRequestResult, connection, null);
    }

    CollegeAppRequestError(HttpURLConnection connection, Exception exception) {
        this(INVALID_HTTP_STATUS_CODE, INVALID_ERROR_CODE, INVALID_ERROR_CODE,
                null, null, null, null, false, null, null, null, connection,
                (exception instanceof CollegeAppException) ?
                        (CollegeAppException) exception : new CollegeAppException(exception));
    }

    public CollegeAppRequestError(int errorCode, String errorType, String errorMessage) {
        this(INVALID_HTTP_STATUS_CODE, errorCode, INVALID_ERROR_CODE, errorType, errorMessage,
                null, null, false, null, null, null, null, null);
    }

    /**
     * Returns the resource id for a user-friendly message for the application to
     * present to the user.
     *
     * @return a user-friendly message to present to the user
     */
    public int getUserActionMessageId() {
        return userActionMessageId;
    }

    /**
     * Returns whether direct user action is required to successfully continue with the
     * operation. If user action is required, apps can also call getUserActionMessageId()
     * in order to get a resource id for a message to show the user.
     *
     * @return whether direct user action is required
     */
    public boolean shouldNotifyUser() {
        return shouldNotifyUser;
    }

    /**
     * Returns the category in which the error belongs. Applications can use the category
     * to determine how best to handle the errors (e.g. exponential backoff for retries if
     * being throttled).
     *
     * @return the category in which the error belong
     */
    public Category getCategory() {
        return category;
    }

    /**
     * Returns the HTTP status code for this particular request.
     *
     * @return the HTTP status code for the request
     */
    public int getRequestStatusCode() {
        return requestStatusCode;
    }

    /**
     * Returns the error code returned.
     *
     * @return the error code returned
     */
    public int getErrorCode() {
        return errorCode;
    }

    /**
     * Returns the sub-error code returned.
     *
     * @return the sub-error code returned
     */
    public int getSubErrorCode() {
        return subErrorCode;
    }

    /**
     * Returns the type of error as a raw string. This is generally less useful
     * than using the getCategory() method, but can provide further details
     * on the error.
     *
     * @return the type of error as a raw string
     */
    public String getErrorType() {
        return errorType;
    }

    /**
     * Returns the error message returned.
     *
     * @return the error message returned
     */
    public String getErrorMessage() {
        if (errorMessage != null) {
            return errorMessage;
        }
        else {
            return exception.getLocalizedMessage();
        }
    }

    /**
     * A message suitable for display to the user, describing a user action necessary to enable functionality.
     * Not all errors yield a message suitable for user display; however in all cases where
     * shouldNotifyUser() returns true, this method returns a non-null message suitable for display.
     *
     * @return the error message returned
     */
    public String getErrorUserMessage() {
        return errorUserMessage;
    }

    /**
     * A short summary of the error suitable for display to the user.
     * Not all errors yield a title/message suitable for user display; however in all cases where
     * getErrorUserTitle() returns valid String - user should be notified.
     *
     * @return the error message returned
     */
    public String getErrorUserTitle() {
        return errorUserTitle;
    }

    /**
     * @return true if given error is transient and may succeed if the initial action is retried as-is.
     * Application may use this information to display a "Retry" button, if user should be notified about this error.
     */
    public boolean getErrorIsTransient() {
        return errorIsTransient;
    }

    /**
     * Returns the body portion of the response corresponding to the request.
     *
     * @return the body of the response for the request
     */
    public JSONObject getRequestResultBody() {
        return requestResultBody;
    }

    /**
     * Returns the full JSON response for the corresponding request. In a non-batch request,
     * this would be the raw response in the form of a JSON object. In a batch request, this
     * result will contain the body of the response as well as the HTTP headers that pertain
     * to the specific request (in the form of a "headers" JSONArray).
     *
     * @return the full JSON response for the request
     */
    public JSONObject getRequestResult() {
        return requestResult;
    }

    /**
     * Returns the full JSON response for the batch request. If the request was not a batch
     * request, then the result from this method is the same as getRequestResult().
     * In case of a batch request, the result will be a JSONArray where the elements
     * correspond to the requests in the batch. Callers should check the return type against
     * either JSONObject or JSONArray and cast accordingly.
     *
     * @return the full JSON response for the batch
     */
    public Object getBatchRequestResult() {
        return batchRequestResult;
    }

    /**
     * Returns the HTTP connection that was used to make the request.
     *
     * @return the HTTP connection used to make the request
     */
    public HttpURLConnection getConnection() {
        return connection;
    }

    /**
     * Returns the exception associated with this request, if any.
     *
     * @return the exception associated with this request
     */
    public CollegeAppException getException() {
        return exception;
    }

    @Override
    public String toString() {
        return new StringBuilder("{HttpStatus: ")
                .append(requestStatusCode)
                .append(", errorCode: ")
                .append(errorCode)
                .append(", errorType: ")
                .append(errorType)
                .append(", errorMessage: ")
                .append(getErrorMessage())
                .append("}")
                .toString();
    }

    static CollegeAppRequestError checkResponseAndCreateError(JSONObject singleResult,
                                                              Object batchResult, HttpURLConnection connection) {
        try {
            if (singleResult.has(CODE_KEY)) {
                int responseCode = singleResult.getInt(CODE_KEY);
                Object body = getStringPropertyAsJSON(singleResult, BODY_KEY, NON_JSON_RESPONSE_PROPERTY);

                if (body != null && body instanceof JSONObject) {
                    JSONObject jsonBody = (JSONObject) body;
                    // Does this response represent an error from the service? We might get either an "error"
                    // with several sub-properties, or else one or more top-level fields containing error info.
                    String errorType = null;
                    String errorMessage = null;
                    String errorUserMessage = null;
                    String errorUserTitle = null;
                    boolean errorIsTransient = false;
                    int errorCode = INVALID_ERROR_CODE;
                    int errorSubCode = INVALID_ERROR_CODE;

                    boolean hasError = false;
                    if (jsonBody.has(ERROR_KEY)) {
                        errorType = jsonBody.optString(ERROR_TYPE_FIELD_KEY, null);
                        errorMessage = jsonBody.optString(ERROR_MESSAGE_FIELD_KEY, null);
                        errorCode = jsonBody.optInt(ERROR_CODE_FIELD_KEY, INVALID_ERROR_CODE);
                        errorSubCode = jsonBody.optInt(ERROR_SUB_CODE_KEY, INVALID_ERROR_CODE);
                        errorUserMessage =  jsonBody.optString(ERROR_USER_MSG_KEY, null);
                        errorUserTitle =  jsonBody.optString(ERROR_USER_TITLE_KEY, null);
                        errorIsTransient = jsonBody.optBoolean(ERROR_IS_TRANSIENT_KEY, false);
                        hasError = true;
                    }
                    else if (jsonBody.has(ERROR_CODE_KEY) || jsonBody.has(ERROR_MSG_KEY) || jsonBody.has(ERROR_REASON_KEY)) {
                        errorType = jsonBody.optString(ERROR_REASON_KEY, null);
                        errorMessage = jsonBody.optString(ERROR_MSG_KEY, null);
                        errorCode = jsonBody.optInt(ERROR_CODE_KEY, INVALID_ERROR_CODE);
                        errorSubCode = jsonBody.optInt(ERROR_SUB_CODE_KEY, INVALID_ERROR_CODE);
                        hasError = true;
                    }

                    if (hasError) {
                        return new CollegeAppRequestError(responseCode, errorCode, errorSubCode,
                                errorType, errorMessage, errorUserTitle, errorUserMessage, errorIsTransient, jsonBody,
                                singleResult, batchResult, connection);
                    }
                }
            }
        } catch (JSONException e) {
            // defer the throwing of a JSONException to the graph object proxy
        }
        return null;
    }

    // Returns either a JSONObject or JSONArray representation of the 'key' property of 'jsonObject'.
    public static Object getStringPropertyAsJSON(JSONObject jsonObject, String key, String nonJSONPropertyKey) throws JSONException {
        Object value = jsonObject.opt(key);
        if (value != null && value instanceof String) {
            JSONTokener tokener = new JSONTokener((String) value);
            value = tokener.nextValue();
        }

        if (value != null && !(value instanceof JSONObject || value instanceof JSONArray)) {
            if (nonJSONPropertyKey != null) {
                // College sometimes gives us back a non-JSON value such as
                // literal "true" or "false" as a result.
                // If we got something like that, we present it to the caller as
                // a GraphObject with a single
                // property. We only do this if the caller wants that behavior.
                jsonObject = new JSONObject();
                jsonObject.putOpt(nonJSONPropertyKey, value);
                return jsonObject;
            }
            else {
                throw new CollegeAppException("Got an unexpected non-JSON object.");
            }
        }

        return value;

    }

    /**
     * An enum that represents the SDK classification for the error that occurred.
     */
    public enum Category {
        /**
         * Indicates that the error is authentication related, and that the app should retry
         * the request after some user action.
         */
        AUTHENTICATION_RETRY,

        /**
         * Indicates that the error is authentication related, and that the app should close
         * the session and reopen it.
         */
        AUTHENTICATION_REOPEN_SESSION,

        /** Indicates that the error is permission related. */
        PERMISSION,

        /**
         * Indicates that the error implies the server had an unexpected failure or may be
         * temporarily unavailable.
         */
        SERVER,

        /** Indicates that the error results from the server throttling the client. */
        THROTTLING,

        /**
         * Indicates that the error is app-related but cannot be categorized at this time,
         * and is likely newer than the current version of the SDK.
         */
        OTHER,

        /**
         * Indicates that the error is an application error resulting in a bad or malformed
         * request to the server.
         */
        BAD_REQUEST,

        /**
         * Indicates that this is a client-side error. Examples of this can include, but are
         * not limited to, JSON parsing errors or java.io.IOException.
         */
        CLIENT
    };
}
