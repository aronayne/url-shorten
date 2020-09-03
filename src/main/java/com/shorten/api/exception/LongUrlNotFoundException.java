package com.shorten.api.exception;

/**
 * Exception class thrown when a long url is not found
 */
public class LongUrlNotFoundException extends RuntimeException {

    public LongUrlNotFoundException() {
        super();
    }

    public LongUrlNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LongUrlNotFoundException(final String message) {
        super(message);
    }

    public LongUrlNotFoundException(final Throwable cause) {
        super(cause);
    }
}
