package gg.jominsubyungsin.response;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Setter
@Getter
public class Response {
  private HttpStatus httpStatus;
  private int status;
  private String message;
  private boolean result;
}
