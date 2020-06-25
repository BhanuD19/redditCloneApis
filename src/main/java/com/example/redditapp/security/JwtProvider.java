package com.example.redditapp.security;

import com.example.redditapp.exceptions.RedditException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.io.InputStream;
import java.security.*;
import java.security.cert.CertificateException;
import java.time.Instant;
import java.util.Date;

@Service
public class JwtProvider {

  private KeyStore keyStore;
  @Value("${jwt.expiration.time}")
  private Long jwtExpirationInMillis;

  @PostConstruct
  public void init() {
    try {
      keyStore = KeyStore.getInstance("JKS");
      InputStream resourceAsStream = getClass().getResourceAsStream("/springblog.jks");
      keyStore.load(resourceAsStream, "secret".toCharArray());
    } catch (KeyStoreException | CertificateException | NoSuchAlgorithmException | IOException e) {
      throw new RedditException("Exception occured while loading keyStore" , e);
    }
  }

  public String generateToken(Authentication authentication) {
    User principal = (User) authentication.getPrincipal();
    return Jwts.builder()
      .setSubject(principal.getUsername())
      .setIssuedAt(Date.from(Instant.now()))
      .signWith(getPrivateKey())
      .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
      .compact();
  }

  public String generateTokenWithUsername(String username) {
    return Jwts.builder()
      .setSubject(username)
      .setIssuedAt(Date.from(Instant.now()))
      .signWith(getPrivateKey())
      .setExpiration(Date.from(Instant.now().plusMillis(jwtExpirationInMillis)))
      .compact();
  }
  public Long getJwtExpirationInMillis() {
    return jwtExpirationInMillis;
  }

  private PrivateKey getPrivateKey() {
    try {
      return (PrivateKey) keyStore.getKey("springblog", "secret".toCharArray());
    } catch (KeyStoreException | NoSuchAlgorithmException | UnrecoverableKeyException e){
      throw new RedditException("Exception occured while retrieving public key from keystore", e);
    }
  }

  public boolean validateToken(String jwt) {
    Jwts.parser().setSigningKey(getPublicKey()).parseClaimsJws(jwt);
    return true;
  }

  private PublicKey getPublicKey() {
    try {
      return keyStore.getCertificate("springBlog").getPublicKey();
    } catch (KeyStoreException e) {
      throw new RedditException("Exception occured while retrieving public key", e);
    }
  }

  public String getUsernameFromJwt(String token) {
    Claims claims = Jwts.parser()
      .setSigningKey(getPublicKey())
      .parseClaimsJws(token)
      .getBody();
    return claims.getSubject();
  }
}
