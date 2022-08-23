package com.football.exception;

public class NoDataFoundException extends RuntimeException {
    public NoDataFoundException(Integer leagueId) {
        super(String.format("No league found for the id %d (please check your plan or the league id)", leagueId));
    }
}


