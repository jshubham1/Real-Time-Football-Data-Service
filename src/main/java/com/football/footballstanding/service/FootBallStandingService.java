package com.football.footballstanding.service;

import com.football.footballstanding.domain.Standing;

import java.util.List;
import java.util.Optional;

public interface FootBallStandingService {
    List<Standing> getStandings(Integer leagueId, Optional<String> countryName, Optional<String> teamName);

    Object getLiveScore();

    Object getPlayerDetails(String playerName);
}
