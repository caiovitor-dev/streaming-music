package dev.caiovitor.streamingmusic.exception.global;

import dev.caiovitor.streamingmusic.dto.ErrorResponseDTO;
import dev.caiovitor.streamingmusic.dto.ValidationErrorResponseDTO;
import dev.caiovitor.streamingmusic.exception.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;


@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationErrorResponseDTO> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, WebRequest request){

        Map<String,String> errors = new HashMap<>();

        ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                new ValidationErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.BAD_REQUEST.value(),
                        "Validation Error",
                        HttpStatus.BAD_REQUEST.getReasonPhrase(),
                        getPath(request),
                        errors
                )
        );

    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEmailAlreadyExists(EmailAlreadyExistsException ex, WebRequest request){

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                new ErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.CONFLICT.value(),
                        ex.getMessage(),
                        HttpStatus.CONFLICT.getReasonPhrase(),
                        getPath(request)
                        )
        );

    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleRoleNotFound(RoleNotFoundException ex, WebRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        getPath(request)
                )
        );

    }

    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenNotFound(TokenNotFoundException ex, WebRequest request){

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.NOT_FOUND.value(),
                        ex.getMessage(),
                        HttpStatus.NOT_FOUND.getReasonPhrase(),
                        getPath(request)
                )
        );

    }
@ExceptionHandler(TokenExpiredException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenExpired(TokenExpiredException ex, WebRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        getPath(request)
                )
        );

    }
    @ExceptionHandler(TokenRevokedException.class)
    public ResponseEntity<ErrorResponseDTO> handleTokenRevoked(TokenRevokedException ex, WebRequest request){

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                new ErrorResponseDTO(
                        LocalDateTime.now()
                        ,HttpStatus.UNAUTHORIZED.value(),
                        ex.getMessage(),
                        HttpStatus.UNAUTHORIZED.getReasonPhrase(),
                        getPath(request)
                )
        );

    }


    private String getPath(WebRequest request){
        return request.getDescription(false).replace("uri=","");
    }
}
