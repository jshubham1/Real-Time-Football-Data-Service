package com.football.footballstanding.exception.advice;

import com.football.footballstanding.domain.ErrorModel;
import com.football.footballstanding.exception.AuthenticationFailureException;
import com.football.footballstanding.exception.FootballStandingException;
import com.football.footballstanding.exception.NoDataFoundException;
import com.football.footballstanding.exception.NoDataFoundForTheFilterException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;

@Slf4j
@RestControllerAdvice
public class GlobalControllerAdvice extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NoDataFoundException.class)
    public ResponseEntity<Object> handleNodataFoundException(NoDataFoundException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorModel(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(NoDataFoundForTheFilterException.class)
    public ResponseEntity<Object> handleNoDataFoundForTheFilterException(NoDataFoundForTheFilterException ex, WebRequest request) {
        return new ResponseEntity<>(getErrorModel(HttpStatus.BAD_REQUEST, ex.getMessage()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationFailureException.class)
    public ResponseEntity<Object> handleAuthenticationFailureException(AuthenticationFailureException ex,
        WebRequest request) {
        return new ResponseEntity<>(getErrorModel(HttpStatus.UNAUTHORIZED, ex.getMessage()), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(FootballStandingException.class)
    public ResponseEntity<Object> handleFootballStandingException(AuthenticationFailureException ex,
        WebRequest request) {
        return new ResponseEntity<>(getErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, ex.getMessage()),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Object> handleGenericException(Exception ex, WebRequest request) {
        log.error(ex.getMessage());
        String msg = "Oops! Something went wring we are looking into it!";
        return new ResponseEntity<>(getErrorModel(HttpStatus.INTERNAL_SERVER_ERROR, msg),
            HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ErrorModel getErrorModel(HttpStatus httpStatus, String message) {
        return ErrorModel.builder().timeStamp(LocalDateTime.now()).errorCode(httpStatus.value()).userMessage(message)
            .build();
    }
}
