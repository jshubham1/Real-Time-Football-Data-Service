package com.football.footballstanding.controller;

import com.football.footballstanding.domain.Standing;
import com.football.footballstanding.exception.AuthenticationFailureException;
import com.football.footballstanding.exception.NoDataFoundException;
import com.football.footballstanding.exception.NoDataFoundForTheFilterException;
import com.football.footballstanding.service.FootBallStandingService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.football.footballstanding.constant.UrlConfig.GET_STANDINGS;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@WebMvcTest(controllers = FootballController.class)
class FootballControllerTest {
    @MockBean
    FootBallStandingService footBallStandingService;

    @Autowired
    MockMvc mockMvc; //Intellij is incorrectly saying that not able to autowire, it will work

    @Test
    public void shouldReturnListOfStandings() throws Exception {
        List<Standing> standings = Collections.singletonList(Standing.builder().build());
        when(footBallStandingService.getStandings(anyInt(), any(), any())).thenReturn(standings);
        MvcResult mvcResult = mockMvc.perform(get(GET_STANDINGS + "?league_id=100&countryName=india&teamName=sky"))
                                     .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.OK.value());
    }

    @Test
    public void shouldThrowNoDataFoundException() throws Exception {
        when(footBallStandingService.getStandings(anyInt(), any(), any())).thenThrow(new NoDataFoundException(100));
        MvcResult mvcResult = mockMvc.perform(get(GET_STANDINGS + "?league_id=100&countryName=india&teamName=sky"))
                                     .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldThrowNoDataFoundForTheFilterException() throws Exception {
        when(footBallStandingService.getStandings(anyInt(), any(), any())).thenThrow(
            new NoDataFoundForTheFilterException(100, Optional.of("india"), Optional.of("sky")));
        MvcResult mvcResult = mockMvc.perform(get(GET_STANDINGS + "?league_id=100&countryName=india&teamName=sky"))
                                     .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @Test
    public void shouldThrowAuthenticationFailureException() throws Exception {
        when(footBallStandingService.getStandings(anyInt(), any(), any())).thenThrow(
            new AuthenticationFailureException());
        MvcResult mvcResult = mockMvc.perform(get(GET_STANDINGS + "?league_id=100&countryName=india&teamName=sky"))
                                     .andReturn();
        MockHttpServletResponse response = mvcResult.getResponse();
        assertThat(response.getStatus()).isEqualTo(HttpStatus.UNAUTHORIZED.value());
    }
}