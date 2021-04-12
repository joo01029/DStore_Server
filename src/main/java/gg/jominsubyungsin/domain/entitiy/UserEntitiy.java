package gg.jominsubyungsin.domain.entitiy;

import lombok.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Entity
@Table(name = "user")
public class UserEntitiy {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  @Column
  private boolean mailAccess;

  @Column(nullable = false)
  private String password;

  @Column(length = 50, nullable = false)
  private String name;

  @Column
  private String introduce;

  @Column
  private String profileImage;

  @Builder
  public UserEntitiy(Long id, String email, String password, String name){
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.mailAccess = false;
    this.introduce = null;
    this.profileImage = null;
  }

}
