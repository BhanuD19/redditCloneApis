package com.example.redditapp.Controller;

import com.example.redditapp.dto.AuthenticationResponse;
import com.example.redditapp.dto.LoginRequest;
import com.example.redditapp.dto.RefreshTokenRequest;
import com.example.redditapp.dto.RegisterRequest;
import com.example.redditapp.service.AuthService;
import com.example.redditapp.service.RefreshTokenService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/auth")
@AllArgsConstructor
public class AuthController {

  private final AuthService authService;
  private final RefreshTokenService refreshTokenService;

  @PostMapping("/signup")
  public ResponseEntity<String> signup(@RequestBody RegisterRequest registerRequest) {
    authService.signup(registerRequest);
    return new ResponseEntity<>("User Resgistration Successful", HttpStatus.OK);
  }

  @GetMapping("accountverification/{token}")
  public ResponseEntity<String> verifyAccount(@PathVariable String token) {
    authService.verifyAccount(token);
    return new ResponseEntity<>("Account activated successfully", HttpStatus.OK);
  }

  @PostMapping("/login")
  public AuthenticationResponse login(@RequestBody LoginRequest loginRequest) {
      AuthenticationResponse auth= authService.login(loginRequest);
    System.out.println("This is the authentcation response to return to the client" + auth);
    return auth;
  }

  @PostMapping("/refresh/token")
  public AuthenticationResponse refreshTokens(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    return authService.refreshToken(refreshTokenRequest);
  }

  @PostMapping("/logout")
  public ResponseEntity<String> logout(@Valid @RequestBody RefreshTokenRequest refreshTokenRequest) {
    refreshTokenService.deleteRefreshToken(refreshTokenRequest.getRefreshToken());
    return ResponseEntity.status(HttpStatus.OK).body("Refresh token deleted successfully");
  }
}
