package com.football.exception;

public class RealTimeFootballDataException extends RuntimeException {
    public RealTimeFootballDataException(String exceptionMessage) {
        super(
            "Oops! Something went wrong, we are working on it please try again after sometime. Detailed Exception - " +
                exceptionMessage);
    }
}
