package com.shorten.api.exception;

/**
 * Thrown when the date is invalid when searching for UrlEntity
 */
public class InvalidDateException extends RuntimeException {

    public InvalidDateException() {
        super();
    }

    public InvalidDateException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public InvalidDateException(final String message) {
        super(message);
    }

    public InvalidDateException(final Throwable cause) {
        super(cause);
    }
}
