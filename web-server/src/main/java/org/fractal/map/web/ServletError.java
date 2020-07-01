package org.fractal.map.web;

public enum ServletError {

    UNKNOWN_ERROR( -1, "unknown error" ),
    SESSION_NOT_EXISTS( 1, "session not exists" ),
    MESSAGE_WAITING_INTERRUPTED( 2, "message waiting interrupted" ),
    MESSAGE_TIMEOUT( 3, "message timeout" ),
    SQUARE_BODY_NOT_AVAILABLE( 4, "sqaure body not available" );

    private final String errorMessage;
    private final int errorCode;

    private ServletError( int errorCode, String errorMessage ) {
        this.errorCode = errorCode;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public int getErrorCode() {
        return errorCode;
    }
}
