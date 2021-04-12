package gg.jominsubyungsin.domain.dto.email;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EmailDto {
  private String senderName;
  private String senderMail;
  private String receiveMail;
  private String subject;
  private String message;

  @Override
  public String toString() {
    return "EmailDTO [senderName=" + senderName + ", senderMail=" + senderMail + ", receiveMail=" + receiveMail
            + ", subject=" + subject + ", message=" + message + "]";
  }
}
