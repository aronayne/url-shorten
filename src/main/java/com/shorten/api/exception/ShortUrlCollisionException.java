package com.shorten.api.exception;

/**
 * Exception class thrown in the rare case that a short url collision is thrown.
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
