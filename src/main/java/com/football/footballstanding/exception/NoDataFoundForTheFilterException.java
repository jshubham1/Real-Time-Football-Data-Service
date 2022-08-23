package com.football.footballstanding.exception;

import java.util.Optional;

public class NoDataFoundForTheFilterException extends RuntimeException {
    public NoDataFoundForTheFilterException(Integer leagueId, Optional<String> countryName, Optional<String> teamName) {
        super(String.format("No standings for the league id - %d, countryName - %s and teamName - %s", leagueId,
            countryName.orElse("{}"), teamName.orElse("{}")));
    }
}
