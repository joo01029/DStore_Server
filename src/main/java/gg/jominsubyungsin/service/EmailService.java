package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public interface EmailService {
  public void sendMail(EmailDto emailDto);
}
