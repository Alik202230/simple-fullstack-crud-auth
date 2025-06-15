package am.itspace.backend.dto;

import am.itspace.backend.entity.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserAuthResponse {

  private String token;
  private String firstName;
  private String lastName;
  private Long userId;
  private Role role;

}
