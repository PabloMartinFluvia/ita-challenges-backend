package com.itachallenge.challenge.custom_v2.exceptions_v2;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.server.ServerWebInputException;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

//TODO: decide type returned:
// if an .error (with an exception with message)
// or any object representing the error (which includes the message)
@RestControllerAdvice
@Slf4j
public class AppExceptionHandlerPROVISIONAL {

    /*
    IMPORTANT:
    Mono IT'S NOT HANDLED BY THE SERVER when exception handling.
    -> Return a FLUX
     */

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ServerWebInputException.class)
    public Flux<ErrorDtoPROVISIONAL> handleServerWebInput(ServerWebInputException ex){
        String problemType = "";
        String reason = ex.getReason();
        if(reason != null){
            problemType = !reason.isBlank() ? reason : ex.getMessage();
        }else {
            problemType = ex.getMessage();
        }
        MethodParameter param = ex.getMethodParameter();
        String targetType = param != null ? param.getGenericParameterType().getTypeName() : "param type not specified";
        String targetParam = param != null ? param.getParameterName() : "param not specified";
        String error=problemType.concat("/").concat(targetType).concat("/").concat(targetParam);
        return Flux.just(new ErrorDtoPROVISIONAL(Set.of(error), HttpStatus.BAD_REQUEST.value()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ConstraintViolationException.class )
    public Flux<ErrorDtoPROVISIONAL> handleConstraintViolation(ConstraintViolationException ex) {
        Set<String> errors = ex.getConstraintViolations()
                .stream().map(ConstraintViolation::getMessage).collect(Collectors.toSet());
        return Flux.just(new ErrorDtoPROVISIONAL(errors, HttpStatus.BAD_REQUEST.value()));
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    public Flux<Object> handleCriticException(Exception ex){
        String stackTrace = Arrays.toString(ex.getStackTrace());
        ex.printStackTrace();
        log.error(stackTrace); //must be solved or handled when is detected
        return Flux.error(ex);
    }
}
