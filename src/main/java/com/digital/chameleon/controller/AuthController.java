package com.digital.chameleon.controller;

/**
 * @author Vineetkabacus@gmail.com
 * @Purpose This class handle all application authorization request.
 */
import java.io.IOException;
import javax.mail.MessagingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.digital.chameleon.common.ApiResponse;
import com.digital.chameleon.common.DigitalChameleonApplicationException;
import com.digital.chameleon.onboarding.security.dto.AuthDTO;
import com.digital.chameleon.onboarding.security.dto.GenerateOTPDTO;
import com.digital.chameleon.onboarding.security.dto.SignUpDTO;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;
import com.digital.chameleon.security.service.OtpService;
import com.digital.chameleon.security.service.UserDetailsImpl;
import com.digital.chameleon.security.service.UserService;
import com.digital.chameleon.service.CommonService;
import com.google.zxing.WriterException;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class);

  @Autowired
  private OtpService otpService;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private UserService userService;

  @Autowired
  private CommonService commonService;

  @PostMapping(value = "generate-otp", produces = "application/json")
  public ResponseEntity<?> generateOtp(@RequestBody GenerateOTPDTO generateOTPDTO)
      throws WriterException, IOException, MessagingException {
    Integer otp = otpService.sendOtp(generateOTPDTO);
    User user = userRepository.findByEmailId(generateOTPDTO.getEmailId());
    commonService.sendOTP(user, otp);
    ApiResponse<String> apiResponse = new ApiResponse<>();
    apiResponse.setErrorCode(null);
    apiResponse.setMessage("OTP is sent successfully to " + generateOTPDTO);
    apiResponse.setResponse(HttpStatus.OK.toString());
    logger.info("Generated OTP successfully ");
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }

  @PostMapping(value = "verify-otp", produces = "application/json")
  public ResponseEntity<?> verifyOtp(@RequestBody AuthDTO authDTO) {
    try {
      ApiResponse<String> apiResponse = otpService.verifyAuthRequest(authDTO);
      logger.info("Verify OTP successfully ");
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    } catch (Exception exception) {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          exception.getMessage());
    }
  }

  @PostMapping(value = "signup", produces = "application/json")
  public ResponseEntity<?> customerSignUp(@RequestBody SignUpDTO signUpDTO) {
    logger.info("signUp request is " + signUpDTO);
    if (userService.isUserExist(signUpDTO)) {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "A user already exists with the same details. Enter unique values.");
    }
    if (userService.addUser(signUpDTO)) {
      ApiResponse<String> apiResponse = new ApiResponse<>();
      apiResponse.setErrorCode(null);
      apiResponse.setMessage("Signup successful! Sign in to continue.");
      apiResponse.setResponse(HttpStatus.OK.toString());
      logger.info("Signup successful! ");
      return new ResponseEntity<>(apiResponse, HttpStatus.OK);
    }
    throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST, "NA");

  }


  @PostMapping(value = "logout", produces = "application/json")
  public ResponseEntity<?> logOutUser() {
    try {
      User user = userRepository.findById(
          ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
              .getId())
          .get();
      otpService.invalidateToken(user);
      ApiResponse<String> apiResponse = new ApiResponse<>();
      apiResponse.setErrorCode(null);
      apiResponse.setMessage("Logout Success");
      apiResponse.setResponse(HttpStatus.OK.toString());
      logger.info("Logout Success! ");
      return new ResponseEntity<Object>(apiResponse, HttpStatus.OK);
    } catch (Exception exception) {
      throw new DigitalChameleonApplicationException(HttpStatus.UNAUTHORIZED,
          exception.getMessage());
    }
  }
}
