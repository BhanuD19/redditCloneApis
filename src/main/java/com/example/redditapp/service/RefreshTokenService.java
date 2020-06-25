package com.example.redditapp.service;

import com.example.redditapp.exceptions.RedditException;
import com.example.redditapp.model.RefreshToken;
import com.example.redditapp.repository.RefreshTokenRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
@AllArgsConstructor
@Transactional
public class RefreshTokenService {

  private final RefreshTokenRepository refreshTokenRepository;

  public RefreshToken generateRefreshToken() {
    RefreshToken refreshToken = new RefreshToken();
    refreshToken.setToken(UUID.randomUUID().toString());
    refreshToken.setCreatedDate(Instant.now());

    return refreshTokenRepository.save(refreshToken);
  }

  void validateRefreshToken(String token) {
    refreshTokenRepository.findByToken(token)
      .orElseThrow(() -> new RedditException("Invalid Refresh token"));
  }

  public void deleteRefreshToken(String token) {
    refreshTokenRepository.deleteByToken(token);
  }
}
