package com.football.footballstanding.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.football.footballstanding.domain.InvalidResponse;
import com.football.footballstanding.domain.Standing;
import com.football.footballstanding.exception.AuthenticationFailureException;
import com.football.footballstanding.exception.FootballStandingException;
import com.football.footballstanding.exception.NoDataFoundException;
import com.football.footballstanding.exception.NoDataFoundForTheFilterException;
import com.football.footballstanding.feign.client.FootballApiClient;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.football.footballstanding.constant.Constants.ACTION_GET_STANDING;
import static com.football.footballstanding.constant.Constants.AUTHENTICATION_FAILED_RESPONSE;
import static com.football.footballstanding.constant.Constants.GET_PLAYER;
import static com.football.footballstanding.constant.Constants.NO_LEAGUE_FOUND_RESPONSE;
import static com.football.footballstanding.constant.Constants.REQUEST_GET_EVENTS_LIVE_SCORE;

@Slf4j
@Service
public class FootBallStandingServiceImpl implements FootBallStandingService {

    private final String apiKey;

    private final FootballApiClient footballApiClient;

    public FootBallStandingServiceImpl(@Value("${football-api-service.api-key}") String apiKey,
        FootballApiClient footballApiClient) {
        this.apiKey = apiKey;
        this.footballApiClient = footballApiClient;
    }

    /**
     * This method returns a list of standings by calling a 3rd party API.
     *
     * @param leagueId to get the standings for this league
     * @param countryName
     * @param teamName
     * @return returns the list of the standings or throws an exception
     */
    @Override
    public List<Standing> getStandings(Integer leagueId, Optional<String> countryName, Optional<String> teamName) {
        log.info("FootBallStandingServiceImpl: getStandings() called with league id - " + leagueId);
        Object response;
        try {
            response = footballApiClient.getStandings(ACTION_GET_STANDING, leagueId, apiKey);
        } catch (Exception exception) {
            log.error("FootBallStandingServiceImpl: getStandings() : Exception Message - " + exception.getMessage());
            throw new FootballStandingException(exception.getMessage());
        }
        List<Standing> standings = filterResults(handleResponse(response, leagueId), countryName, teamName);
        if (standings.isEmpty()) {
            throw new NoDataFoundForTheFilterException(leagueId, countryName, teamName);
        }
        return standings;
    }

    private List<Standing> filterResults(List<Standing> standings, Optional<String> countryName,
        Optional<String> teamName) {
        //filter by both country name and team name
        if (countryName.isPresent() && StringUtils.isNotEmpty(countryName.get()) && teamName.isPresent() &&
                StringUtils.isNotEmpty(teamName.get())) {
            return standings.stream().filter(
                standing -> standing.getCountryName().equalsIgnoreCase(countryName.get().trim())).filter(
                standing -> standing.getTeamName().equalsIgnoreCase(teamName.get())).collect(Collectors.toList());
        }
        //filter by only country name
        if (countryName.isPresent() && StringUtils.isNotEmpty(countryName.get())) {
            return standings.stream().filter(
                standing -> standing.getCountryName().equalsIgnoreCase(countryName.get().trim())).collect(
                Collectors.toList());
        }
        //filter by only team name
        if (teamName.isPresent() && StringUtils.isNotEmpty(teamName.get())) {
            return standings.stream().filter(standing -> standing.getTeamName().equalsIgnoreCase(teamName.get().trim()))
                            .collect(Collectors.toList());
        }
        return standings;
    }

    /**
     * Handles the response based on the type of the reponse
     *
     * @param response response object from the API
     * @param leagueId to create the exception message
     * @return list of standings or throws an exception
     */
    private List<Standing> handleResponse(Object response, Integer leagueId) {
        log.debug(
            String.format("FootBallStandingServiceImpl: handleResponse() called with league id - %d and response %s",
                leagueId, response));
        if (response != null) {
            ObjectMapper mapper = new ObjectMapper();
            if (response instanceof ArrayList) {
                return mapper.convertValue(response, new TypeReference<List<Standing>>() {
                });
            } else if (response instanceof LinkedHashMap) {
                InvalidResponse invalidResponse = mapper.convertValue(response, InvalidResponse.class);
                log.error("FootBallStandingServiceImpl: handleResponse() : received invalid response - " +
                              invalidResponse.toString());
                if (NO_LEAGUE_FOUND_RESPONSE.equals(invalidResponse.getMessage())) {
                    throw new NoDataFoundException(leagueId);
                }
                if (AUTHENTICATION_FAILED_RESPONSE.equals(invalidResponse.getMessage())) {
                    throw new AuthenticationFailureException();
                }
            }
        } else {
            log.error("FootBallStandingServiceImpl: handleResponse() : received empty response from the Football API");
            throw new FootballStandingException("Received empty response from the Football API server");
        }
        return Collections.emptyList();
    }

    /**
     * Returns you a live score
     *
     * @return
     */
    @Override
    public Object getLiveScore() {
        log.info("FootBallStandingServiceImpl: getLiveScore()");
        Object response;
        try {
            response = footballApiClient.getLiveScore(REQUEST_GET_EVENTS_LIVE_SCORE, 1, apiKey);
        } catch (Exception exception) {
            log.error("FootBallStandingServiceImpl: getLiveScore() : Exception Message - " + exception.getMessage());
            throw new FootballStandingException(exception.getMessage());
        }
        return response;
    }

    /**
     * Returns you a live score
     *
     * @return
     */
    @Override
    public Object getPlayerDetails(String playerName) {
        log.info("FootBallStandingServiceImpl: getPlayerDetails()");
        Object response;
        try {
            response = footballApiClient.getPlayerDetails(GET_PLAYER, playerName, apiKey);
        } catch (Exception exception) {
            log.error("FootBallStandingServiceImpl: getPlayerDetails() : Exception Message - " + exception.getMessage());
            throw new FootballStandingException(exception.getMessage());
        }
        return response;
    }
}
