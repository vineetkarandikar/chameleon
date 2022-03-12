package com.digital.chameleon.security.service;

import java.util.UUID;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.digital.chameleon.onboarding.security.dto.SignUpDTO;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;

/**
 * @author Vineetkabacus@gmail.com
 * @Purpose This class handle all application user related service operations.
 */
@Service
public class UserService {

  @Autowired
  private UserRepository userRepository;

  public Boolean addUser(SignUpDTO signUpDTO) {
    User user = new User();
    user.setId(UUID.randomUUID().toString());
    user.setEmailId(signUpDTO.getEmailId());
    user.setMobile(signUpDTO.getMobile());
    user.setPanNumber(signUpDTO.getPanNumber());
    user.setFirstName(signUpDTO.getFirstName());
    user.setLastName(signUpDTO.getLastName());
    if (signUpDTO.getFirstName() == null || signUpDTO.getLastName() == null) {
      user.setFirstName(signUpDTO.getEmailId());
      user.setLastName(" ");
    }
    userRepository.save(user);
    return true;
  }

  public Boolean isUserExist(SignUpDTO signUpDTO) {
    if ((userRepository.findByEmailId(signUpDTO.getEmailId()) != null)) {
      return true;
    } else if ((userRepository.findByMobile(signUpDTO.getMobile()) != null)) {
      return true;
    } else {
      return false;
    }
  }

}
