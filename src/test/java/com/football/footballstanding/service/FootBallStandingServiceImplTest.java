package com.football.footballstanding.service;

import com.football.footballstanding.domain.Standing;
import com.football.footballstanding.exception.AuthenticationFailureException;
import com.football.footballstanding.exception.FootballStandingException;
import com.football.footballstanding.exception.NoDataFoundException;
import com.football.footballstanding.exception.NoDataFoundForTheFilterException;
import com.football.footballstanding.feign.client.FootballApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class FootBallStandingServiceImplTest {

    @InjectMocks
    FootBallStandingServiceImpl footBallStandingService;

    @Mock
    FootballApiClient footballApiClient;

    @BeforeEach
    void setUp() {
        footBallStandingService = new FootBallStandingServiceImpl("XXXXX", footballApiClient);
    }

    @Test
    public void shouldReturnListOfStandings() {
        List<Standing> expectedStandings = getExpectedStandings();
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(expectedStandings);
        List<Standing> actualStandings = footBallStandingService.getStandings(100, Optional.ofNullable(null),
            Optional.ofNullable(null));
        Assertions.assertEquals(expectedStandings, actualStandings);
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldReturnListOfStandingsWithCountryNameAndTeamNameFilter() {
        List<Standing> expectedStandings = getExpectedStandings();
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(expectedStandings);
        List<Standing> actualStandings = footBallStandingService.getStandings(100, Optional.of("India"),
            Optional.of("sky"));
        Assertions.assertEquals(expectedStandings, actualStandings);
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldThrowNoDataFoundForTheFilterException() {
        List<Standing> expectedStandings = getExpectedStandings();
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(expectedStandings);
        assertThrows(NoDataFoundForTheFilterException.class,
            () -> footBallStandingService.getStandings(100, Optional.of("RANDOM_COUNTRY"), Optional.of("TEAM")),
            String.format("No standings for the league id - %d, countryName - %s and teamName - %s", 100,
                "RANDOM_COUNTRY", "TEAM"));
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldThrowNoDataFoundException() {
        LinkedHashMap invalidResponse = getInvalidResponse("400", "No league found (please check your plan)!!");
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(invalidResponse);
        assertThrows(NoDataFoundException.class,
            () -> footBallStandingService.getStandings(100, Optional.ofNullable(null), Optional.ofNullable(null)),
            String.format("No league found for the id %d (please check your plan or the league id)", 100));
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldThrowAuthenticationFailureException() {
        LinkedHashMap invalidResponse = getInvalidResponse("401", "Authentification failed!");
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(invalidResponse);
        assertThrows(AuthenticationFailureException.class,
            () -> footBallStandingService.getStandings(100, Optional.ofNullable(null), Optional.ofNullable(null)),
            "Authentication failed! Invalid API key, please check your API Key");
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldThrowFootballStandingException() {
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenReturn(null);
        assertThrows(FootballStandingException.class,
            () -> footBallStandingService.getStandings(100, Optional.ofNullable(null), Optional.ofNullable(null)),
            "Received empty response from the Football API server");
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    @Test
    public void shouldThrowFootballStandingExceptionWhenApiIsDown() {
        when(footballApiClient.getStandings(eq("get_standings"), eq(100), eq("XXXXX"))).thenThrow(
            new RuntimeException("ERROR"));
        assertThrows(FootballStandingException.class,
            () -> footBallStandingService.getStandings(100, Optional.ofNullable(null), Optional.ofNullable(null)),
            "Oops! Something went wrong, we are working on it please try again after sometime. Detailed Exception - " +
                "ERROR");
        verify(footballApiClient, times(1)).getStandings(eq("get_standings"), eq(100), eq("XXXXX"));
    }

    private LinkedHashMap<String, String> getInvalidResponse(String errorCode, String message) {
        LinkedHashMap<String, String> linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("error", errorCode);
        linkedHashMap.put("message", message);
        return linkedHashMap;
    }

    private List<Standing> getExpectedStandings() {
        List<Standing> expectedStandings = new ArrayList<>();
        expectedStandings.add(Standing.builder().teamId("23").leagueId("100").countryName("India").teamName("SKY")
                                      .build());
        return expectedStandings;
    }

}