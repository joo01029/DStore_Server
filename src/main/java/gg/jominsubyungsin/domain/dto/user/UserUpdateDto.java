package gg.jominsubyungsin.domain.dto.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateDto {
  private String Email;
  private String password;
  private String changePassword;
  private String changeName;
}
