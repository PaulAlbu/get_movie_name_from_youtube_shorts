package com.retrieve.movie.title.youtube_shorts.exceptions;

public class ExternalServiceException extends RuntimeException {
    public ExternalServiceException(String msg) {
        super(msg);
    }

    public ExternalServiceException(String msg, Throwable cause) {
        super(msg, cause);
    }

}
