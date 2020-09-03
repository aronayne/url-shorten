package com.shorten.api.exception;

/**
 * Thrown in the rare case that a short URL collision is thrown.
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
