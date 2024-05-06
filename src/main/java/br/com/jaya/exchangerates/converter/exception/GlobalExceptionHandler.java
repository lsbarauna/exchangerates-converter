package br.com.jaya.exchangerates.converter.exception;

import br.com.jaya.exchangerates.converter.to.ErrorOutbound;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.nio.file.AccessDeniedException;
import java.util.List;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutbound> handlerException(Exception ex) {

        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        log.error("INTERNAL_SERVER_ERROR: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorOutbound);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorOutbound> handlerNoResourceFoundException(NoResourceFoundException ex) {
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.NOT_FOUND, ex.getMessage());
        log.error("NOT_FOUND: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorOutbound);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ErrorOutbound> handlerAccessDeniedException(ApplicationException ex) {
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.FORBIDDEN, ex.getMessage());
        log.error("FORBIDDEN: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorOutbound);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorOutbound> handlerAuthenticationException(AuthenticationException ex) {
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.UNAUTHORIZED, ex.getMessage());
        log.error("UNAUTHORIZED: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorOutbound);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorOutbound> handlerResourceNotFoundException(ApplicationException ex) {
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, ex.getMessage());
        log.error("BAD_REQUEST: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorOutbound);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorOutbound> handlerMethodArgumentNotValidException (MethodArgumentNotValidException ex) {

        BindingResult result = ex.getBindingResult();
        List<FieldError> fieldErrors = result.getFieldErrors();

        String erroMessage = "";

        for (FieldError fieldError : fieldErrors) {
            erroMessage = "Field " + fieldError.getField() + " "
                    + fieldError.getDefaultMessage();
            break;
        }

        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, erroMessage);
        log.error("BAD_REQUEST: " + erroMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorOutbound);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorOutbound> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, "Request body is not readable: " + ex.getMessage());
        log.error("BAD_REQUEST: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorOutbound);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorOutbound> handleInvalidFormatException(InvalidFormatException ex) {
        String errorMessage = "Invalid format for field '" + ex.getPath().get(0).getFieldName() +
                "': expected type " + ex.getTargetType().getSimpleName() +
                ", found value '" + ex.getValue() + "'";
        ErrorOutbound errorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, errorMessage);
        log.error("BAD_REQUEST: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorOutbound);
    }

}
