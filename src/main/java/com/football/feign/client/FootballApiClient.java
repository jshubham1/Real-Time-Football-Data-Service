package com.football.feign.client;

import com.football.constant.Constants;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(value = "footballApiService", url = "${football-api-service.url}")
public interface FootballApiClient {
    @GetMapping
    Object getStandings(@RequestParam(Constants.REQUEST_PARAM_ACTION) String action,
        @RequestParam(Constants.REQUEST_PARAM_LEAGUE_ID) Integer leagueId, @RequestParam(Constants.REQUEST_PARAM_API_KEY) String apiKey);

    @GetMapping
    Object getLiveScore(@RequestParam(Constants.REQUEST_PARAM_ACTION) String action, @RequestParam(Constants.LIVE_MATCH) Integer matchLive,
        @RequestParam(Constants.REQUEST_PARAM_API_KEY) String apiKey);

    @GetMapping
    Object getPlayerDetails(@RequestParam(Constants.REQUEST_PARAM_ACTION) String action, @RequestParam(
        Constants.PLAYER_NAME) String playerName,
        @RequestParam(Constants.REQUEST_PARAM_API_KEY) String apiKey);
}
