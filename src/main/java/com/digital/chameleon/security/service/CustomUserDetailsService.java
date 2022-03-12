package com.digital.chameleon.security.service;

import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import com.digital.chameleon.common.DigitalChameleonApplicationException;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  @Autowired
  UserRepository userRepository;

  @Override
  public UserDetails loadUserByUsername(String userId) {
    Optional<User> user = userRepository.findById(userId);
    if (!user.isPresent()) {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "User not found with id!");
    }
    if (user.get().getStatus()) {
      return UserDetailsImpl.build(user.get());
    } else {
      throw new DigitalChameleonApplicationException(HttpStatus.UNAUTHORIZED,
          "User Logged out Already!");
    }
  }
}
