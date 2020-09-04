package com.shorten.api.exception;

/**
 * Stores messages for exceptions
 */
public enum ExceptionMessage {

    INVALID_DATE("The date(s) parameter value is invalid"),
    LONG_URL_LENGTH_EXCEEEDED("Long URL length exceeds the max allowable length"),
    SHORT_URL_COLLISION("A short URL collision occurred");

    public final String message;

    private ExceptionMessage(String message) {
        this.message = message;
    }

}
