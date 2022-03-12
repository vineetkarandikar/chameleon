package com.digital.chameleon.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.digital.chameleon.common.ApiResponse;
import com.digital.chameleon.common.DigitalChameleonApplicationException;
import com.digital.chameleon.onboarding.profile.dto.Profile;
import com.digital.chameleon.profile.model.CandidateProfile;
import com.digital.chameleon.profile.model.Certification;
import com.digital.chameleon.profile.model.PreviousOrganizationSummary;
import com.digital.chameleon.profile.repository.CandidateProfileRepository;
import com.digital.chameleon.security.entity.User;
import com.digital.chameleon.security.repo.UserRepository;
import com.digital.chameleon.security.service.UserDetailsImpl;

/*
 * @Author Vineetkabacus@gmail.com
 * 
 * @Purpose This class is used for all candidate profile operations.
 */

@Service
public class CandidateProfileService {

  private static final Logger logger = LoggerFactory.getLogger(CommonService.class);

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private CommonService commonService;

  @Autowired
  private CandidateProfileRepository candidateProfileRepository;

  public ApiResponse<String> saveCandidateProfile(Profile profile) throws IOException {
    User user = userRepository.findById(
        ((UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal())
            .getId())
        .get();
    CandidateProfile candidateProfile = new CandidateProfile();
    candidateProfile.setResumeDoc(profile.getResumeDoc());
    candidateProfile.setResumeDocType(profile.getResumeDocType());
    candidateProfile.setSkills(profile.getSkills());
    candidateProfile.setNoticePeroidInMonths(profile.getNoticePeroidInMonths());
    candidateProfile.setProfilePic(profile.getProfilePic());
    candidateProfile.setLinkedinLink(profile.getLinkedinLink());
    candidateProfile.setTotalExperienceInMonths(profile.getTotalExperienceInMonths());
    candidateProfile.setFirstName(profile.getFirstName());
    candidateProfile.setLastName(profile.getLastName());
    candidateProfile.setAddress(profile.getAddress());
    candidateProfile.setCity(profile.getCity());
    candidateProfile.setCountry(profile.getCountry());
    candidateProfile.setCurrency(profile.getCurrency());
    candidateProfile.setState(profile.getState());
    candidateProfile.setGender(profile.getGender());
    candidateProfile.setEmailId(profile.getEmailId());
    candidateProfile.setSalaryExpected(profile.getSalaryExpected());
    candidateProfile.setLastSalaryDrawn(profile.getLastSalaryDrawn());
    candidateProfile.setLastUpdated(System.currentTimeMillis() / 1000);
    candidateProfile.setPhoneNo(profile.getPhoneNo());
    candidateProfile.setAlternatePhoneNo(profile.getAlternatePhoneNo());
    List<Certification> certifications = new ArrayList<>();
    for (com.digital.chameleon.onboarding.profile.dto.Certification certificationIn : profile
        .getCertifications()) {
      Certification certification = new Certification();
      certification.setCertificationDoc(certificationIn.getCertificationDoc());
      certification.setName(certificationIn.getName());
      certifications.add(certification);
    }
    candidateProfile.setCertifications(certifications);
    for (com.digital.chameleon.onboarding.profile.dto.Certification certificationIn : profile
        .getCertifications()) {
      Certification certification = new Certification();
      certification.setCertificationDoc(certificationIn.getCertificationDoc());
      certification.setName(certificationIn.getName());
      certifications.add(certification);
    }
    List<PreviousOrganizationSummary> previousOrganizationSummaries = new ArrayList<>();
    for (com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary previousOrganizationSummaryIn : profile
        .getPreviousOrganizationSummary()) {
      PreviousOrganizationSummary previousOrganizationSummary = new PreviousOrganizationSummary();
      previousOrganizationSummary.setDesignation(previousOrganizationSummaryIn.getDesignation());
      previousOrganizationSummary.setOrgName(previousOrganizationSummaryIn.getOrgName());
      previousOrganizationSummary
          .setTenureInMonths(previousOrganizationSummaryIn.getTenureInMonths());
      previousOrganizationSummaries.add(previousOrganizationSummary);
    }
    candidateProfile.setPreviousOrganizationSummary(previousOrganizationSummaries);
    String resumeText = "NA";
    if (profile.getResumeDocType().equalsIgnoreCase("PDF")) {
      resumeText = commonService.convertBase64EncodedStringToPDFFileAndExtractText(
          profile.getResumeDoc(), user.getEmailId());
    } else if (profile.getResumeDocType().equalsIgnoreCase("DOCX")) {
      resumeText = commonService.convertBase64EncodedStringToDocFileAndExtractText(
          profile.getResumeDoc(), user.getEmailId());
    } else {
      throw new DigitalChameleonApplicationException(HttpStatus.BAD_REQUEST,
          "Please upload either PDF or Docx");
    }
    candidateProfile.setResumeText(resumeText);
    candidateProfileRepository.save(candidateProfile);
    ApiResponse<String> apiResponse = new ApiResponse<>();
    apiResponse.setErrorCode(null);
    apiResponse.setMessage("Profile Saved Successfully ");
    logger.info("Profile saved/updated successfully" + candidateProfile.toString());
    apiResponse.setResponse(HttpStatus.OK.toString());
    return apiResponse;
  }

  public ApiResponse<Profile> getCandidateProfile(String emailId) throws IOException {
    Optional<CandidateProfile> candidateProfileOptional =
        candidateProfileRepository.findById(emailId);
    if (candidateProfileOptional.isPresent()) {
      CandidateProfile candidateProfile = candidateProfileOptional.get();
      Profile profile = new Profile();
      profile.setAddress(candidateProfile.getAddress());
      profile.setAlternatePhoneNo(candidateProfile.getAlternatePhoneNo());
      List<com.digital.chameleon.onboarding.profile.dto.Certification> certifications =
          new ArrayList<>();
      for (Certification certification : candidateProfile.getCertifications()) {
        com.digital.chameleon.onboarding.profile.dto.Certification certificationIn =
            new com.digital.chameleon.onboarding.profile.dto.Certification();
        certificationIn.setCertificationDoc(certification.getCertificationDoc());
        certificationIn.setName(certification.getName());
        certifications.add(certificationIn);
      }
      profile.setCertifications(certifications);
      List<com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary> previousOrganizationSummarys =
          new ArrayList<>();
      for (PreviousOrganizationSummary previousOrganizationSummary : candidateProfile
          .getPreviousOrganizationSummary()) {
        com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary previousOrganizationSummaryIn =
            new com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary();
        previousOrganizationSummaryIn.setDesignation(previousOrganizationSummary.getDesignation());
        previousOrganizationSummaryIn.setOrgName(previousOrganizationSummary.getOrgName());
        previousOrganizationSummaryIn
            .setTenureInMonths(previousOrganizationSummary.getTenureInMonths());
        previousOrganizationSummarys.add(previousOrganizationSummaryIn);
      }
      profile.setPreviousOrganizationSummary(previousOrganizationSummarys);
      profile.setCity(candidateProfile.getCity());
      profile.setCountry(candidateProfile.getCountry());
      profile.setState(candidateProfile.getState());
      profile.setCurrency(candidateProfile.getCurrency());
      profile.setResumeDoc(candidateProfile.getResumeDoc());
      profile.setResumeDocType(candidateProfile.getResumeDocType());
      profile.setLastUpdated(candidateProfile.getLastUpdated());
      profile.setNoticePeroidInMonths(candidateProfile.getNoticePeroidInMonths());
      profile.setLinkedinLink(candidateProfile.getLinkedinLink());
      profile.setPhoneNo(candidateProfile.getPhoneNo());
      profile.setAlternatePhoneNo(candidateProfile.getAlternatePhoneNo());
      profile.setFirstName(candidateProfile.getFirstName());
      profile.setLastName(candidateProfile.getLastName());
      profile.setGender(candidateProfile.getGender());
      profile.setAddress(candidateProfile.getAddress());
      profile.setLastSalaryDrawn(candidateProfile.getLastSalaryDrawn());
      profile.setSalaryExpected(candidateProfile.getSalaryExpected());
      profile.setLastSalaryDrawn(candidateProfile.getLastSalaryDrawn());
      profile.setSkills(candidateProfile.getSkills());
      profile.setEmailId(candidateProfile.getEmailId());
      profile.setTotalExperienceInMonths(candidateProfile.getTotalExperienceInMonths());
      profile.setProfilePic(candidateProfile.getProfilePic());
      ApiResponse<Profile> apiResponse = new ApiResponse<>();
      apiResponse.setErrorCode(null);
      apiResponse.setMessage("Success ");
      apiResponse.setResponse(profile);
      apiResponse.setSuccess(true);
      logger.info("Profile fetched successfully" + profile.toString());
      return apiResponse;
    }
    throw new DigitalChameleonApplicationException(HttpStatus.NOT_FOUND,
        "For a given emailId no profile exists");
  }

  public ApiResponse<List<Profile>> getBySkillsAndLastUpdatedCandidateProfiles(String skills)
      throws IOException {
    List<CandidateProfile> candidateProfiles =
        candidateProfileRepository.findByResumeTextLikeOrderByLastUpdatedDesc(skills);
    List<Profile> profiles = new ArrayList<>();
    if (!candidateProfiles.isEmpty()) {
      for (CandidateProfile candidateProfileIn : candidateProfiles) {
        Profile profile = new Profile();
        profile.setAddress(candidateProfileIn.getAddress());
        profile.setAlternatePhoneNo(candidateProfileIn.getAlternatePhoneNo());
        List<com.digital.chameleon.onboarding.profile.dto.Certification> certifications =
            new ArrayList<>();
        for (Certification certification : candidateProfileIn.getCertifications()) {
          com.digital.chameleon.onboarding.profile.dto.Certification certificationIn =
              new com.digital.chameleon.onboarding.profile.dto.Certification();
          certificationIn.setCertificationDoc(certification.getCertificationDoc());
          certificationIn.setName(certification.getName());
          certifications.add(certificationIn);
        }
        profile.setCertifications(certifications);
        List<com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary> previousOrganizationSummarys =
            new ArrayList<>();
        for (PreviousOrganizationSummary previousOrganizationSummary : candidateProfileIn
            .getPreviousOrganizationSummary()) {
          com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary previousOrganizationSummaryIn =
              new com.digital.chameleon.onboarding.profile.dto.PreviousOrganizationSummary();
          previousOrganizationSummaryIn
              .setDesignation(previousOrganizationSummary.getDesignation());
          previousOrganizationSummaryIn.setOrgName(previousOrganizationSummary.getOrgName());
          previousOrganizationSummaryIn
              .setTenureInMonths(previousOrganizationSummary.getTenureInMonths());
          previousOrganizationSummarys.add(previousOrganizationSummaryIn);
        }
        profile.setPreviousOrganizationSummary(previousOrganizationSummarys);
        profile.setCity(candidateProfileIn.getCity());
        profile.setCountry(candidateProfileIn.getCountry());
        profile.setState(candidateProfileIn.getState());
        profile.setCurrency(candidateProfileIn.getCurrency());
        profile.setResumeDoc(candidateProfileIn.getResumeDoc());
        profile.setResumeDocType(candidateProfileIn.getResumeDocType());
        profile.setLastUpdated(candidateProfileIn.getLastUpdated());
        profile.setNoticePeroidInMonths(candidateProfileIn.getNoticePeroidInMonths());
        profile.setLinkedinLink(candidateProfileIn.getLinkedinLink());
        profile.setPhoneNo(candidateProfileIn.getPhoneNo());
        profile.setAlternatePhoneNo(candidateProfileIn.getAlternatePhoneNo());
        profile.setFirstName(candidateProfileIn.getFirstName());
        profile.setLastName(candidateProfileIn.getLastName());
        profile.setGender(candidateProfileIn.getGender());
        profile.setAddress(candidateProfileIn.getAddress());
        profile.setLastSalaryDrawn(candidateProfileIn.getLastSalaryDrawn());
        profile.setSalaryExpected(candidateProfileIn.getSalaryExpected());
        profile.setSkills(candidateProfileIn.getSkills());
        profile.setLastSalaryDrawn(candidateProfileIn.getLastSalaryDrawn());
        profile.setEmailId(candidateProfileIn.getEmailId());
        profile.setTotalExperienceInMonths(candidateProfileIn.getTotalExperienceInMonths());
        profile.setProfilePic(candidateProfileIn.getProfilePic());
        profiles.add(profile);
      }
      ApiResponse<List<Profile>> apiResponse = new ApiResponse<>();
      apiResponse.setErrorCode(null);
      apiResponse.setMessage("Success ");
      apiResponse.setResponse(profiles);
      logger.info("Profiles fetched successfully" + profiles.toString());
      apiResponse.setSuccess(true);
      return apiResponse;
    }
    throw new DigitalChameleonApplicationException(HttpStatus.NOT_FOUND,
        "For a given skills no profile exists");
  }

}
