package am.itspace.backend.exception.error;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class ErrorResponse {

  private String message;
  private int code;
  private HttpStatus httpStatus;
  private LocalDateTime timestamp;

  public ErrorResponse(String message, int code, HttpStatus httpStatus,  LocalDateTime timestamp) {
    this.message = message;
    this.code = code;
    this.httpStatus = httpStatus;
    this.timestamp = timestamp;
  }

}
