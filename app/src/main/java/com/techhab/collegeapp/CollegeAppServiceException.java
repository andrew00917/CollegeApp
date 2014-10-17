package com.techhab.collegeapp;

/**
 * Created by jhchoe on 10/16/14.
 *
 * Error coming from college service response to request
 */
public class CollegeAppServiceException extends CollegeAppException {

    private final CollegeAppRequestError error;

    private static final long serialVersionUID = 1;

    /**
     * Constructs a new FacebookServiceException.
     *
     * @param error the error from the request
     */
    public CollegeAppServiceException(CollegeAppRequestError error, String errorMessage) {
        super(errorMessage);
        this.error = error;
    }

    /**
     * Returns an object that encapsulates complete information representing the error returned by Facebook.
     *
     * @return complete information representing the error.
     */
    public final CollegeAppRequestError getRequestError() {
        return error;
    }

    @Override
    public final String toString() {
        return new StringBuilder()
                .append("{CollegeAppServiceException: ")
                .append("httpResponseCode: ")
                .append(error.getRequestStatusCode())
                .append(", collegeAppErrorCode: ")
                .append(error.getErrorCode())
                .append(", collegeAppErrorType: ")
                .append(error.getErrorType())
                .append(", message: ")
                .append(error.getErrorMessage())
                .append("}")
                .toString();
    }

}