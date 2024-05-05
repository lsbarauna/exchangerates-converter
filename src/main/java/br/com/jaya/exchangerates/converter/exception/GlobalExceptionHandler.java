package br.com.jaya.exchangerates.converter.exception;

import br.com.jaya.exchangerates.converter.to.ErrorOutbound;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
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
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorOutbound> handlerException(Exception ex) {
        ex.printStackTrace();
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.INTERNAL_SERVER_ERROR, "Something went wrong");
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(ErrorOutbound);
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErrorOutbound> handlerNoResourceFoundException(NoResourceFoundException ex) {
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.NOT_FOUND, ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorOutbound);
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseBody
    public ResponseEntity<ErrorOutbound> handlerAccessDeniedException(ApplicationException ex) {
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.FORBIDDEN, ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(ErrorOutbound);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorOutbound> handlerAuthenticationException(AuthenticationException ex) {
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.UNAUTHORIZED, ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(ErrorOutbound);
    }

    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorOutbound> handlerResourceNotFoundException(ApplicationException ex) {
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorOutbound);
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

        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, erroMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorOutbound);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorOutbound> handlerHttpMessageNotReadableException(HttpMessageNotReadableException ex) {
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, "Request body is not readable: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorOutbound);
    }

    @ExceptionHandler(InvalidFormatException.class)
    public ResponseEntity<ErrorOutbound> handleInvalidFormatException(InvalidFormatException ex) {
        String errorMessage = "Invalid format for field '" + ex.getPath().get(0).getFieldName() +
                "': expected type " + ex.getTargetType().getSimpleName() +
                ", found value '" + ex.getValue() + "'";
        ErrorOutbound ErrorOutbound = new ErrorOutbound(HttpStatus.BAD_REQUEST, errorMessage);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorOutbound);
    }

}
