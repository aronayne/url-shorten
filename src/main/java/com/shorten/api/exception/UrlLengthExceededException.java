package com.shorten.api.exception;

public class UrlLengthExceededException extends RuntimeException {

    public UrlLengthExceededException() {
        super();
    }

    public UrlLengthExceededException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UrlLengthExceededException(final String message) {
        super(message);
    }

    public UrlLengthExceededException(final Throwable cause) {
        super(cause);
    }
}
