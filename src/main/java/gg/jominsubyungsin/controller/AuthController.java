package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.user.LoginResponse;
import gg.jominsubyungsin.service.SecurityService;
import gg.jominsubyungsin.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;

@Controller
@ResponseBody
@RequestMapping("/auth")
public class AuthController {

  @Autowired
  SecurityService securityService;
  @Autowired
  UserService userService;

  //유저 생성
  @PostMapping("/create")
  public Response userCreate(@RequestBody UserDto userDto){
    Response response = new Response();

    String hashPassword = securityService.hashPassword(userDto.getPassword());
    userDto.setPassword(hashPassword);

    if(null == userDto.getPassword()){
      response.setResult(false);
      response.setMessage("비밀번호 암호화 실패");
      response.setHttpStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES);
      response.setStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES.value());

      return response;
    }

    boolean userCreateResult = userService.userCreate(userDto);

    if(!userCreateResult){
      response.setResult(false);
      response.setMessage("유저 저장 실패");
      response.setHttpStatus(HttpStatus.BAD_REQUEST);
      response.setStatus(HttpStatus.BAD_REQUEST.value());

      return response;
    }

    response.setResult(true);
    response.setMessage("유저 저장 성공");
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());

    return response;
  }

  //로그인
  @PostMapping("/login")
  public LoginResponse login(@RequestBody UserDto userDto){
    LoginResponse loginResponse = new LoginResponse();

    //비밀번호 암호화
    String hashPassword = securityService.hashPassword(userDto.getPassword());
    userDto.setPassword(hashPassword);

    if(null == userDto.getPassword()){
      loginResponse.setResult(false);
      loginResponse.setMessage("비밀번호 암호화 실패");
      loginResponse.setHttpStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES);
      loginResponse.setStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES.value());

      return loginResponse;
    }

    Optional<UserEntitiy> findUserResponse = Optional.ofNullable(userService.login(userDto));

    if(findUserResponse.isEmpty()){
      loginResponse.setResult(false);
      loginResponse.setMessage("유저 찾기 실패");
      loginResponse.setHttpStatus(HttpStatus.OK);
      loginResponse.setStatus(HttpStatus.OK.value());

      return loginResponse;
    }
    //token발행
    String subject = findUserResponse.get().getEmail();

    long accessExpiredTime = 20 * 60 * 1000L;
    String accessToken = securityService.createToken(subject, accessExpiredTime, false);

    long refreshExpiredTime = 7 * 24 * 60* 60 * 1000L;
    String refreshToken = securityService.createToken(subject, refreshExpiredTime, true);

    if(accessToken == null || refreshToken == null){
      loginResponse.setResult(false);
      loginResponse.setMessage("토큰 발행 실패");
      loginResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
      loginResponse.setStatus(HttpStatus.BAD_REQUEST.value());

      return loginResponse;
    }
    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date(System.currentTimeMillis()+accessExpiredTime);
    String expiredTime = format.format(time);

    loginResponse.setResult(false);
    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setStatus(HttpStatus.OK.value());
    loginResponse.setExepiration(expiredTime);
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshToken);

    return loginResponse;
  }
  @GetMapping("/refresh")
  public LoginResponse tokenRefresh(@RequestHeader String Authorization){
    LoginResponse loginResponse = new LoginResponse();

    String subject = securityService.getRefreshTokenSubject(Authorization);

    if(null == subject){
      loginResponse.setResult(false);
      loginResponse.setMessage("토큰 디코딩 실패");
      loginResponse.setHttpStatus(HttpStatus.BAD_REQUEST);
      loginResponse.setStatus(HttpStatus.BAD_REQUEST.value());

      return loginResponse;
    }

    long accessExpiredTime = 20 * 60 * 1000L;
    String accessToken = securityService.createToken(subject, accessExpiredTime, false);

    long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
    String refreshTokenRemake = securityService.createToken(subject, refreshExpiredTime, true);

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date time = new Date(System.currentTimeMillis()+accessExpiredTime);
    String expiredTime = format.format(time);

    loginResponse.setResult(false);
    loginResponse.setMessage("로그인 성공");
    loginResponse.setHttpStatus(HttpStatus.OK);
    loginResponse.setStatus(HttpStatus.OK.value());
    loginResponse.setExepiration(expiredTime);
    loginResponse.setAccessToken(accessToken);
    loginResponse.setRefreshToken(refreshTokenRemake);
    return loginResponse;
  }

  @GetMapping("/send/email")
  public Response sendEmail(@RequestParam UserDto userDto){
    Response response = new Response();
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    response.setResult(true);
    response.setMessage("아아아아");
    return response;

  }
}
