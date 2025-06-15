package am.itspace.backend.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SaveUserRequest {

  @NotNull(message = "First name should not be null")
  @NotEmpty(message = "First name is required")
  private String firstName;

  @NotNull(message = "Last name is should not be null")
  @NotEmpty(message = "Last name is required")
  private String lastName;

  @NotNull(message = "Email should not be null")
  @Email(message = "Invalid email address")
  private String email;

  @NotNull(message = "Password should not be null")
  @NotEmpty(message = "Password is required")
  private String password;

}
