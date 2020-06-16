package com.example.redditapp.service;

import com.example.redditapp.model.User;
import com.example.redditapp.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String username) {
    Optional<User> userOptional = userRepository.findByUsername(username);
    User user = userOptional.orElseThrow(() -> new UsernameNotFoundException("No user found with "+username));
    return new org.springframework.security.core.userdetails.User(user.getUsername()
      ,user.getPassword(), user.isEnabled(),
      true,true,true,
      getAuhtorities("USER"));
  }

  private Collection<? extends GrantedAuthority> getAuhtorities(String role) {
    return Collections.singletonList(new SimpleGrantedAuthority(role));
  }
}