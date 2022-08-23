package com.football.controller;

import com.football.constant.Constants;
import com.football.constant.UrlConfig;
import com.football.domain.ErrorModel;
import com.football.domain.Standing;
import com.football.service.RealTimeFootballDataService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController(UrlConfig.REST_API)
@Slf4j
@RequiredArgsConstructor
public class FootballController {

    private final RealTimeFootballDataService realTimeFootballDataService;

    @Operation(description = "Gets the list of all the standing for a league id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid API key",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "404", description = "No Data Found Found",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorModel.class)))})
    @GetMapping(value = UrlConfig.GET_STANDINGS, produces = {"application/hal+json"})
    public HttpEntity<CollectionModel<Standing>> getStandings(
        @RequestParam(name = Constants.REQUEST_PARAM_LEAGUE_ID) Integer leagueId,
        @RequestParam(name = "countryName") Optional<String> countryName,
        @RequestParam(name = "teamName") Optional<String> teamName) {
        log.info("FootballController: getStandings() called with league id - " + leagueId);
        List<Standing> standings = realTimeFootballDataService.getStandings(leagueId, countryName, teamName);
        log.info("FootballController: getStandings() returning results - " + standings.size() + " for the league - " +
                     leagueId);
        return new ResponseEntity<>(addSelfLink(leagueId, countryName, teamName, standings), HttpStatus.OK);
    }

    @Operation(description = "Gets the live score")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid API key",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "404", description = "No Data Found Found",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorModel.class)))})
    @GetMapping(value = UrlConfig.GET_LIVE_SCORE)
    public HttpEntity<Object> getLiveScore() {
        log.info("FootballController: getLiveScore() called");
        return new ResponseEntity<>(realTimeFootballDataService.getLiveScore(), HttpStatus.OK);
    }

    @Operation(description = "Gets player details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid API key",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "404", description = "No Data Found Found",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorModel.class)))})
    @GetMapping(value = UrlConfig.GET_PLAYER_DETAILS)
    public HttpEntity<Object> getPlayerDetails(@RequestParam(name = Constants.PLAYER_NAME) String playerName) {
        log.info("FootballController: getPlayerDetails() called for player name as " + playerName);
        return new ResponseEntity<>(realTimeFootballDataService.getPlayerDetails(playerName), HttpStatus.OK);
    }

    /**
     * adds a new self link by making a fake calle
     *
     * @param leagueId to make call
     * @param countryName
     * @param teamName
     * @param standings this will be updated
     * @return returns a collectionModel
     */
    private CollectionModel<Standing> addSelfLink(Integer leagueId, Optional<String> countryName,
        Optional<String> teamName, List<Standing> standings) {
        Link link = linkTo(
            methodOn(FootballController.class).getStandings(leagueId, countryName, teamName)).withSelfRel();
        return CollectionModel.of(standings, link);
    }
}
