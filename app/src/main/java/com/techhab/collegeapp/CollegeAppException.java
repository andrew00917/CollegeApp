package com.techhab.collegeapp;

/**
 * Created by jhchoe on 10/16/14.
 *
 * This class handles runtime exceptions
 *
 */
public class CollegeAppException extends RuntimeException {

    static final long serialVersionUID = 1;

    /**
     * Constructs a new CollegeAppException.
     */
    public CollegeAppException() {
        super();
    }

    /**
     * Constructs a new CollegeAppException.
     *
     * @param message
     *            the detail message of this exception
     */
    public CollegeAppException(String message) {
        super(message);
    }

    /**
     * Constructs a new CollegeAppException.
     *
     * @param message
     *            the detail message of this exception
     * @param throwable
     *            the cause of this exception
     */
    public CollegeAppException(String message, Throwable throwable) {
        super(message, throwable);
    }

    /**
     * Constructs a new CollegeAppException.
     *
     * @param throwable
     *            the cause of this exception
     */
    public CollegeAppException(Throwable throwable) {
        super(throwable);
    }
}