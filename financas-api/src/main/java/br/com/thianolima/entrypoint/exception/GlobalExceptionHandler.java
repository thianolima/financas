package br.com.thianolima.entrypoint.exception;


import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Set;
import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {
    private final MessageSource messageSource;

    public GlobalExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(DomainException.class)
    public ResponseEntity<ErrorResponse> handleDomainException(DomainException ex) {
        var locale = LocaleContextHolder.getLocale();
        var translatedMessage = messageSource.getMessage(ex.getMessageKey().getKey(), ex.getArgs(), locale);
        var errorResponse = new ErrorResponse(new ErrorDetails(translatedMessage));
        return ResponseEntity.badRequest().body(errorResponse);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorsResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        var errors = ex.getBindingResult().getAllErrors().stream().map(error -> {
            String errorMessage = error.getDefaultMessage();
            return new ErrorDetails(errorMessage);
        }).collect(Collectors.toSet());
        return ResponseEntity.badRequest().body(new ErrorsResponse(errors));
    }

    private record ErrorDetails(String message) {}
    private record ErrorResponse(ErrorDetails error) {}
    private record ErrorsResponse(Set<ErrorDetails> error) {}

}
