package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<UserEntitiy,Long> {
  Optional<UserEntitiy> findByEmail(String email);
  Optional<UserEntitiy> findById(Long Id);
  void deleteByEmail(String email);
  Optional<UserEntitiy> findByEmailAndPassword(String email, String password);
}
