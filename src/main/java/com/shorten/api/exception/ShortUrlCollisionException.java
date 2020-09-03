package com.shorten.api.exception;

/**
 * Thrown in the rare case that a short URL collision is thrown.
 */
public class ShortUrlCollisionException extends RuntimeException {

    public ShortUrlCollisionException() {
        super();
    }

    public ShortUrlCollisionException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public ShortUrlCollisionException(final String message) {
        super(message);
    }

    public ShortUrlCollisionException(final Throwable cause) {
        super(cause);
    }
}
