package com.example.redditapp.service;

import com.example.redditapp.dto.AuthenticationResponse;
import com.example.redditapp.dto.LoginRequest;
import com.example.redditapp.dto.RefreshTokenRequest;
import com.example.redditapp.dto.RegisterRequest;
import com.example.redditapp.exceptions.RedditException;
import com.example.redditapp.model.NotificationEmail;
import com.example.redditapp.model.User;
import com.example.redditapp.model.VerificationToken;
import com.example.redditapp.repository.UserRepository;
import com.example.redditapp.repository.VerificationTokenRepository;
import com.example.redditapp.security.JwtProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AnonymousAuthenticationProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;

@Service
@AllArgsConstructor
@Slf4j
public class AuthService {

  private final PasswordEncoder passwordEncoder;
  private final UserRepository userRepository;
  private final VerificationTokenRepository verificationTokenRepository;
  private final MailService mailService;
  private final AuthenticationManager authenticationManager;
  private final JwtProvider jwtProvider;
  private final RefreshTokenService refreshTokenService;

  @Transactional
  public void signup(RegisterRequest registerRequest){
    User user = new User();
    user.setUsername(registerRequest.getUsername());
    user.setEmail(registerRequest.getEmail());
    user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
    user.setCreated(Instant.now());
    user.setEnabled(false);
    try {
      userRepository.save(user);
      log.info("User saved successfully");
    } catch (Exception e) {
      log.info(String.valueOf(e));
    }

    String token = generateVerificationToken(user);
    mailService.sendMail(new NotificationEmail("Please Activate your Account", user.getEmail(),
      "Thank you for signing up with us, please click on the below url to activate your account :" + "http://localhost:8080/api/auth/accountverification/" +token));

    }

  private String generateVerificationToken(User user) {
    String token = UUID.randomUUID().toString();
    VerificationToken verificationToken= new VerificationToken();
    verificationToken.setToken(token);
    verificationToken.setUser(user);
    verificationTokenRepository.save(verificationToken);
    return token;
  }

  @Transactional(readOnly = true)
  public User getCurrentUser() {
    org.springframework.security.core.userdetails.User principal = (org.springframework.security.core.userdetails.User) SecurityContextHolder
      .getContext().getAuthentication().getPrincipal();
    return userRepository.findByUsername(principal.getUsername())
      .orElseThrow(() -> new UsernameNotFoundException(principal.getUsername() +" user not found"));
  }

  public void verifyAccount(String token) {
    Optional<VerificationToken> verificationTokenOptional= verificationTokenRepository.findByToken(token);
    verificationTokenOptional.orElseThrow(() -> new RedditException("Invalid token"));
    fetchUserAndEnable(verificationTokenOptional.orElseThrow(() -> new RedditException("Invalid token")));
  }

  @Transactional
  public void fetchUserAndEnable(VerificationToken verificationToken) {
    String username = verificationToken.getUser().getUsername();
    User user = userRepository.findByUsername(username).orElseThrow(() -> new RedditException("User with name " + username + " not found"));
    user.setEnabled(true);
    try {
      userRepository.save(user);
    } catch (Exception e) {
      log.info(String.valueOf(e));
    }
  }

  public AuthenticationResponse login(LoginRequest loginRequest) {
      Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
        loginRequest.getPassword()));
      SecurityContextHolder.getContext().setAuthentication(authenticate);
      String authenticationToken= jwtProvider.generateToken(authenticate);
    System.out.println("this is the authenticatoion token" + authenticationToken);

    return new AuthenticationResponse().builder()
      .authenticationToken(authenticationToken)
      .refreshToken(refreshTokenService.generateRefreshToken().getToken())
      .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
      .username(loginRequest.getUsername())
      .build();
  }

  public AuthenticationResponse refreshToken(RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.validateRefreshToken(refreshTokenRequest.getRefreshToken());
    String token = jwtProvider.generateTokenWithUsername(refreshTokenRequest.getUsername());
    return AuthenticationResponse.builder()
      .authenticationToken(token)
      .refreshToken(refreshTokenRequest.getRefreshToken())
      .expiresAt(Instant.now().plusMillis(jwtProvider.getJwtExpirationInMillis()))
      .username(refreshTokenRequest.getUsername())
      .build();
  }

  public boolean isLoggedIn() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return !(authentication instanceof AnonymousAuthenticationProvider) && authentication.isAuthenticated();
  }
}
