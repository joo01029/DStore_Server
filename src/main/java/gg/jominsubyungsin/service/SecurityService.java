package gg.jominsubyungsin.service;

public interface SecurityService {
  String hashPassword(String password);

  String createToken(String subject, long ttlMillis, boolean MakeTokenForRefresh);

  String getAccessTokenSubject(String token);

  String getRefreshTokenSubject(String token);
}
