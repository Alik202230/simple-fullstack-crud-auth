package am.itspace.backend.exception;

import am.itspace.backend.exception.error.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
    Map<String, String> errors = new HashMap<>();
    ex.getBindingResult().getAllErrors().forEach(error -> {
      String fieldName = ((FieldError) error).getField();
      String errorMessage = error.getDefaultMessage();
      errors.put(fieldName, errorMessage);
    });
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errors);
  }

  @ExceptionHandler({
      UserAlreadyExistsException.class,
      UserNotFoundException.class,
      UsernamePasswordException.class,
      AuthorizationException.class,
      FileNotFountException.class,
      ProductNotFoundException.class,
      AccessDeniedException.class,
      CartNotFoundException.class
  })
  public ResponseEntity<ErrorResponse> handleException(Exception exception) {
    return switch (exception) {
      case UserAlreadyExistsException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.CONFLICT.value(),
            HttpStatus.CONFLICT,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.CONFLICT).body(errorResponse);
      }
      case UserNotFoundException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }
      case UsernamePasswordException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NO_CONTENT.value(),
            HttpStatus.NO_CONTENT,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NO_CONTENT).body(errorResponse);
      }
      case AuthorizationException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.UNAUTHORIZED.value(),
            HttpStatus.UNAUTHORIZED,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
      }

      case FileNotFountException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }

      case ProductNotFoundException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }

      case AccessDeniedException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.FORBIDDEN.value(),
            HttpStatus.FORBIDDEN,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
      }

      case CartNotFoundException ex -> {
        ErrorResponse errorResponse = new ErrorResponse(
            ex.getMessage(),
            HttpStatus.NOT_FOUND.value(),
            HttpStatus.NOT_FOUND,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
      }

      default -> {
        ErrorResponse error = new ErrorResponse("Server is about to down. Critical",
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            HttpStatus.INTERNAL_SERVER_ERROR,
            LocalDateTime.now()
        );
        yield ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
      }
    };
  }
}

