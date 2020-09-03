package com.shorten.api.exception;

public class UrlNotFoundException extends RuntimeException {

    public UrlNotFoundException() {
        super();
    }

    public UrlNotFoundException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public UrlNotFoundException(final String message) {
        super(message);
    }

    public UrlNotFoundException(final Throwable cause) {
        super(cause);
    }
}
