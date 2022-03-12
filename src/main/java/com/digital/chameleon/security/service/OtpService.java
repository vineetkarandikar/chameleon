package com.digital.chameleon.security.service;

import java.util.List;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import com.digital.chameleon.common.ApiResponse;
import com.digital.chameleon.common.DigitalChameleonApplicationException;
import com.digital.chameleon.onboarding.security.dto.AuthDTO;
import com.digital.chameleon.onboarding.security.dto.GenerateOTPDTO;
import com.digital.chameleon.security.entity.OtpRequest;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.OtpRepository;
import com.digital.chameleon.security.repo.UserRepository;
/**
 * @author Vineetkabacus@gmail.com
 * @Purpose This class handle all application OTP related operations.
 */

@Service
public class OtpService {

  @Autowired
  private OtpRepository otpRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private TokenProvider tokenProvider;

  private int generateOtp() {
    return ((int) (Math.random() * (10000 - 1000))) + 1000;
  }


  public int sendOtp(GenerateOTPDTO generateOTPDTO) {
    if (null == generateOTPDTO)
      throw new RuntimeException("Request is not valid.");
    int otp = generateOtp();
    User user = null;
    OtpRequest otpRequest = new OtpRequest();
    if (isEmailValid(generateOTPDTO.getEmailId())) {
      otpRequest.setEmailId(generateOTPDTO.getEmailId());
      user = userRepository.findByEmailId(generateOTPDTO.getEmailId());
    }
    if (otpRequest.getEmailId() == null) {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "Request is not valid!");
    }
    if (user == null) {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "User does not exists!");
    }
    otpRequest.setOtp(otp);
    otpRequest.setId(UUID.randomUUID().toString());
    otpRequest.setExpiryTime(System.currentTimeMillis() + 600000);
    otpRequest.setEmailId(user.getEmailId());
    otpRequest.setMobile(user.getMobile());
    List<OtpRequest> otpRequestIns = otpRepository.findByEmailId(user.getEmailId());
    for (OtpRequest otpRequestIn : otpRequestIns) {
      otpRepository.deleteById(otpRequestIn.getId());
    }
    otpRepository.save(otpRequest);
    user.setStatus(false);
    userRepository.save(user);
    return otp;
  }

  public ApiResponse<String> verifyAuthRequest(AuthDTO authRequest) {
    List<OtpRequest> otpRequests = null;
    OtpRequest otpRequest = null;
    User user = null;
    if (null == authRequest.getEmailId())
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "Request is not valid!");
    if (isEmailValid(authRequest.getEmailId())) {
      otpRequests = otpRepository.findByEmailId(authRequest.getEmailId());
      if (otpRequests.get(0) != null) {
        otpRequest = otpRequests.get(0);
      }
      user = userRepository.findByEmailId(authRequest.getEmailId());
    }
    if (otpRequest != null) {
      if (otpRequest.getOtp() == authRequest.getOtp()) {
        if (otpRequest.getExpiryTime() < System.currentTimeMillis()) {
          otpRepository.deleteById(otpRequest.getId());
          throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST, "OTP Expired!");
        }
        user.setStatus(true);
        userRepository.save(user);
        ApiResponse<String> apiResponse = new ApiResponse<>();
        apiResponse.setErrorCode("NA");
        apiResponse.setMessage("JWT-TOKEN");
        apiResponse.setResponse(tokenProvider.createTokenForUser(user));
        return apiResponse;
      } else {
        user.setStatus(false);
        userRepository.save(user);
        throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST, "Invalid OTP!");
      }
    }
    return null;
  }

  private boolean isEmailValid(String email) {
    String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
    CharSequence inputStr = email;
    Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
    Matcher matcher = pattern.matcher(inputStr);
    if (matcher.matches()) {
      return true;
    }
    return false;
  }

  private boolean isPhoneNumberValid(String phoneNumber) {
    String expression = "^(\\+91[\\-\\s]?)?[0]?(91)?[789]\\d{9}$";
    CharSequence inputStr = phoneNumber;
    Pattern pattern = Pattern.compile(expression);
    Matcher matcher = pattern.matcher(inputStr);
    if (matcher.matches()) {
      return true;
    }
    return false;
  }

  public void invalidateToken(User user) {
    User userIn = user;
    userIn.setStatus(false);
    userRepository.save(userIn);
    List<OtpRequest> otpRequests = null;
    otpRequests = otpRepository.findByEmailId(user.getEmailId());
    for (OtpRequest otpRequest : otpRequests) {
      otpRepository.deleteById(otpRequest.getId());
    }
    otpRequests = otpRepository.findByMobile(user.getMobile());
    for (OtpRequest otpRequest : otpRequests) {
      otpRepository.deleteById(otpRequest.getId());
    }
  }

}
