package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public boolean userCreate(UserDto userDto) {
    Optional<UserEntitiy> findUserByEmail;
    try {
      findUserByEmail = userRepository.findByEmail(userDto.getEmail());
    }catch (Exception e){
      throw e;
    }

    if(findUserByEmail.isPresent()){
      return false;
    }
    try {
      UserEntitiy saveUser = userDto.toEntity();
      userRepository.save(saveUser);
      return true;
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public UserEntitiy login(UserDto userDto) {
    String password = userDto.getPassword();
    String Email = userDto.getEmail();
    try {
      Optional<UserEntitiy> findUserByEmailAndPassword = userRepository.findByEmailAndPassword(Email, password);

      return findUserByEmailAndPassword.orElseGet(() -> null);
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public boolean userUpdate(UserUpdateDto userUpdateDto) {
    return userRepository.findByEmailAndPassword(userUpdateDto.getEmail(),userUpdateDto.getPassword())
            .map(found-> {
              found.setPassword(Optional.ofNullable(userUpdateDto.getChangePassword()).orElse(found.getPassword()));
              found.setName(Optional.ofNullable(userUpdateDto.getChangeName()).orElse(found.getName()));
              userRepository.save(found);

              return true;
            }).orElse(false);


  }

  @Override
  @Transactional
  public boolean userUpdateIntroduce(UserDto userDto) {
    return userRepository.findByEmail(userDto.getEmail())
            .map(found-> {
              found.setIntroduce(userDto.getIntroduce());
              userRepository.save(found);

              return true;
            }).orElse(false);


  }

  @Override
  @Transactional
  public boolean userDelete(UserDto userDto) {
    Optional<UserEntitiy> findUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

    if(findUser.isEmpty()){
      return false;
    }

    try{
      userRepository.deleteByEmail(userDto.getEmail());
      return true;
    }catch (Exception e){
      throw e;
    }
  }

}
