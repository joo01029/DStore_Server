package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

@Service
public class EmailServiceImpl implements EmailService {
  @Autowired
  private JavaMailSender javaMailSender;

  @Override
  public void sendMail(EmailDto emailDto) {
    try {
      MimeMessage msg = javaMailSender.createMimeMessage();
      MimeMessageHelper messageHelper = new MimeMessageHelper(msg, true, "UTF-8");
      messageHelper.setFrom(emailDto.getSenderMail(), emailDto.getSenderName());
      messageHelper.setTo(emailDto.getReceiveMail());
      messageHelper.setSubject(emailDto.getSubject());
      // 이메일 본문 (인코딩을 해야 한글이 깨지지 않음)
      messageHelper.setText(emailDto.getMessage(), true);

      javaMailSender.send(msg);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

}
