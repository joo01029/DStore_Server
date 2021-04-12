package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.logging.Logger;

@Service
public class SecurityServiceImpl implements SecurityService{
  @Override
  public String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      md.update(password.getBytes(), 0, password.getBytes().length);
      return new BigInteger(1, md.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      Logger logger = (Logger) LoggerFactory.getLogger(UserEntitiy.class);
      logger.warning(e.getMessage());
      return null;
    }
  }

  private static final String ACCESSSECRET_KEY = "asdwqedsaasdwqesadsjosjndknaskmlcnkejdoknlksamlcnsqlkndoqkwacsdqwa";
  private static final String REFRESHSECRET_KEY = "wqeniodnvsksadowqjndlknmsandokwqmlkdnjnfcosajkjqkwdmklsankclnwondkwqnldsneqdsndk";

  @Override
  public String createToken(String subject, long ttlMillis, boolean MakeTokenForRefresh){
    if(ttlMillis <= 0)
      throw new RuntimeException("Expiry time must be greater than Zero : ["+ttlMillis+"] ");

    try {
      Key signingKey = makeSigningKey(MakeTokenForRefresh);

      return encodingToken(subject, signingKey, ttlMillis);
    }catch (Exception e){
      throw e;
    }
  }

  public Key makeSigningKey(boolean MakeTokenForRefresh){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] secretKey;

    try {
      if (MakeTokenForRefresh) {
        secretKey = DatatypeConverter.parseBase64Binary(REFRESHSECRET_KEY);
      } else {
        secretKey = DatatypeConverter.parseBase64Binary(ACCESSSECRET_KEY);
      }
      return new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());
    }catch (Exception e){
      throw e;
    }
  }

  public String encodingToken(String subject, Key secretKey,long ttlMillis){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    try {
      return Jwts.builder()
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
              .signWith(secretKey, signatureAlgorithm)
              .compact();
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public String getAccessTokenSubject(String token) {
    Claims claims = decodingToken(token, ACCESSSECRET_KEY);
    return claims.getSubject();

  }

  public Claims decodingToken(String token, String key){
    Claims claims;
    try{
      claims = Jwts.parserBuilder()
              .setSigningKey(DatatypeConverter.parseBase64Binary(key))
              .build()
              .parseClaimsJws(token)
              .getBody();
    } catch (ExpiredJwtException e){
      System.out.println(e);
      return null;
    } catch (Exception e){
      System.out.println(e);
      return null;
    }
    return claims;

  }

  @Override
  public String getRefreshTokenSubject(String token) {
    Claims claims = decodingToken(token, REFRESHSECRET_KEY);
    return claims.getSubject();
  }
}
