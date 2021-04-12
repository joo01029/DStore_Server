package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;

import java.util.Optional;

public interface UserService {
  boolean userCreate(UserDto userDto);
  UserEntitiy login(UserDto userDto);
  boolean userUpdate(UserUpdateDto userDto);
  boolean userDelete(UserDto userDto);
  boolean userUpdateIntroduce(UserDto userDto);
}
