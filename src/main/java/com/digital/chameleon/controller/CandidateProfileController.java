package com.digital.chameleon.controller;

import java.io.IOException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.digital.chameleon.common.ApiResponse;
import com.digital.chameleon.onboarding.profile.dto.Profile;
import com.digital.chameleon.onboarding.profile.dto.SearchProfileDTO;
import com.digital.chameleon.onboarding.profile.dto.SearchProfileSkillDTO;
import com.digital.chameleon.service.CandidateProfileService;



/**
 * @author Vineetkabacus@gmail.com
 * @Purpose This class handle all candidate profile operations.
 */

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/v1/")
public class CandidateProfileController {

  private static final Logger logger = LoggerFactory.getLogger(CandidateProfileController.class);

  @Autowired
  private CandidateProfileService candidateProfileService;

  @PostMapping(value = "profile", produces = "application/json")
  public ResponseEntity<?> generateOtp(@RequestBody Profile profile) throws IOException {
    ApiResponse<String> apiResponse = candidateProfileService.saveCandidateProfile(profile);
    logger.info("Profile Creation completed");
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }

  @PostMapping(value = "search-profile-by-email", produces = "application/json")
  public ResponseEntity<?> generateOtp(@RequestBody SearchProfileDTO searchProfileDTO)
      throws IOException {
    logger.info("Started search profile by email for " + searchProfileDTO.toString());
    ApiResponse<Profile> apiResponse =
        candidateProfileService.getCandidateProfile(searchProfileDTO.getEmailId());
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }


  @PostMapping(value = "search-latest-profile-by-skills", produces = "application/json")
  public ResponseEntity<?> generateOtp(@RequestBody SearchProfileSkillDTO searchProfileSkillDTO)
      throws IOException {
    logger.info(
        "Started search profile by skills with lastupdated " + searchProfileSkillDTO.toString());
    ApiResponse<List<Profile>> apiResponse = candidateProfileService
        .getBySkillsAndLastUpdatedCandidateProfiles(searchProfileSkillDTO.getSkills());
    return new ResponseEntity<>(apiResponse, HttpStatus.OK);
  }
}
