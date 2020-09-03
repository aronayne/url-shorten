package com.shorten.api.exception;

/**
 * Thrown when a URL entity is not found
 */
public class UrlEntityNotFoundException extends RuntimeException {

    public UrlEntityNotFoundException() {
        super();
    }

    public UrlEntityNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UrlEntityNotFoundException(final String message) {
        super(message);
    }

    public UrlEntityNotFoundException(final Throwable cause) {
        super(cause);
    }
}
