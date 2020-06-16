package com.example.redditapp.repository;

import com.example.redditapp.model.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
  Optional<RefreshToken> findByToken (String token);

  void deleteByToken(String Token);
}
