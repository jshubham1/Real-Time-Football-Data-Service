package com.football.footballstanding.controller;

import com.football.footballstanding.domain.ErrorModel;
import com.football.footballstanding.domain.Standing;
import com.football.footballstanding.service.FootBallStandingService;
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

import static com.football.footballstanding.constant.Constants.PLAYER_NAME;
import static com.football.footballstanding.constant.Constants.REQUEST_PARAM_LEAGUE_ID;
import static com.football.footballstanding.constant.UrlConfig.GET_LIVE_SCORE;
import static com.football.footballstanding.constant.UrlConfig.GET_PLAYER_DETAILS;
import static com.football.footballstanding.constant.UrlConfig.GET_STANDINGS;
import static com.football.footballstanding.constant.UrlConfig.REST_API;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@RestController(REST_API)
@Slf4j
@RequiredArgsConstructor
public class FootballController {

    private final FootBallStandingService footBallStandingService;

    @Operation(description = "Gets the list of all the standing for a league id")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid API key",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "404", description = "No Data Found Found",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorModel.class)))})
    @GetMapping(value = GET_STANDINGS, produces = {"application/hal+json"})
    public HttpEntity<CollectionModel<Standing>> getStandings(
        @RequestParam(name = REQUEST_PARAM_LEAGUE_ID) Integer leagueId,
        @RequestParam(name = "countryName") Optional<String> countryName,
        @RequestParam(name = "teamName") Optional<String> teamName) {
        log.info("FootballController: getStandings() called with league id - " + leagueId);
        List<Standing> standings = footBallStandingService.getStandings(leagueId, countryName, teamName);
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
    @GetMapping(value = GET_LIVE_SCORE)
    public HttpEntity<Object> getLiveScore() {
        log.info("FootballController: getLiveScore() called");
        return new ResponseEntity<>(footBallStandingService.getLiveScore(), HttpStatus.OK);
    }

    @Operation(description = "Gets player details")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Success"),
        @ApiResponse(responseCode = "401", description = "Invalid API key",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "404", description = "No Data Found Found",
            content = @Content(schema = @Schema(implementation = ErrorModel.class))),
        @ApiResponse(responseCode = "500", description = "Internal Server Error",
            content = @Content(schema = @Schema(implementation = ErrorModel.class)))})
    @GetMapping(value = GET_PLAYER_DETAILS)
    public HttpEntity<Object> getPlayerDetails(@RequestParam(name = PLAYER_NAME) String playerName) {
        log.info("FootballController: getPlayerDetails() called for player name as " + playerName);
        return new ResponseEntity<>(footBallStandingService.getPlayerDetails(playerName), HttpStatus.OK);
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
