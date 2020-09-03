package com.shorten.api.exception;

/**
 * Thrown when the max length of a long URL is exceeded.
 */
public class LongUrlLengthExceededException extends RuntimeException {

    public LongUrlLengthExceededException() {
        super();
    }

    public LongUrlLengthExceededException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public LongUrlLengthExceededException(final String message) {
        super(message);
    }

    public LongUrlLengthExceededException(final Throwable cause) {
        super(cause);
    }
}
