package com.football.service;

import com.football.domain.Standing;

import java.util.List;
import java.util.Optional;

public interface RealTimeFootballDataService {
    List<Standing> getStandings(Integer leagueId, Optional<String> countryName, Optional<String> teamName);

    Object getLiveScore();

    Object getPlayerDetails(String playerName);
}
