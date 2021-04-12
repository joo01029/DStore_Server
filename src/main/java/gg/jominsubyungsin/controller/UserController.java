package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
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
@RequestMapping("/user")
public class UserController {
  @Autowired
  UserService userService;
  @Autowired
  SecurityService securityService;

  @PostMapping("/set/introduce")
  public Response setIntroduce(@RequestBody UserDto userDto, @RequestHeader String Authorization){
    Response response = new Response();

    String subject = securityService.getRefreshTokenSubject(Authorization);

    userDto.setEmail(subject);

    boolean setIntruduceResult = userService.userUpdateIntroduce(userDto);

    if(!setIntruduceResult){
      response.setHttpStatus(HttpStatus.BAD_REQUEST);
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("자기 소개 변경 실패");
      response.setResult(false);

      return response;
    }

    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("자기 소개 변경 성공");
    response.setResult(true);

    return response;
  }
  @PutMapping("/update")
  public Response userUpdate (@RequestBody UserUpdateDto userUpdateDto){
    Response response = new Response();



    String hashNowPassword = securityService.hashPassword(userUpdateDto.getPassword());
    String hashChangePassword =
            userUpdateDto.getChangePassword() != null ? securityService.hashPassword(userUpdateDto.getChangePassword()):null;



    if(null == hashNowPassword){
      response.setResult(false);
      response.setMessage("비밀번호 암호화 실패");
      response.setHttpStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES);
      response.setStatus(HttpStatus.VARIANT_ALSO_NEGOTIATES.value());

      return response;
    }

    userUpdateDto.setPassword(hashNowPassword);
    userUpdateDto.setChangePassword(hashChangePassword);

    boolean userUpdateResult = userService.userUpdate(userUpdateDto);

    if(!userUpdateResult){
      response.setResult(false);
      response.setHttpStatus(HttpStatus.BAD_REQUEST);
      response.setStatus(HttpStatus.BAD_REQUEST.value());
      response.setMessage("유저 업데이트 실패");

      return response;
    }

    response.setResult(true);
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());
    response.setMessage("유저 업데이트 성공");

    return response;
  }

  @DeleteMapping("/delete")
  public Response userDelete(@RequestBody UserDto userDto){
    Response response = new Response();
    String hashPassword = securityService.hashPassword(userDto.getPassword());

    if(hashPassword == null){
      response.setMessage("비밀번호 암호화 실패");
      response.setResult(false);
      response.setHttpStatus(HttpStatus.BAD_REQUEST);
      response.setStatus(HttpStatus.BAD_REQUEST.value());

      return response;
    }

    userDto.setPassword(hashPassword);

    boolean userDeleteReuslt = userService.userDelete(userDto);

    if(!userDeleteReuslt){
      response.setMessage("유저 삭제 실패");
      response.setResult(false);
      response.setHttpStatus(HttpStatus.BAD_REQUEST);
      response.setStatus(HttpStatus.BAD_REQUEST.value());

      return response;
    }

    response.setMessage("유저 삭제 성공");
    response.setResult(true);
    response.setHttpStatus(HttpStatus.OK);
    response.setStatus(HttpStatus.OK.value());

    return response;
  }
}

