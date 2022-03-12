package com.digital.chameleon.onboarding.profile.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Profile {
  private String resumeDoc;
  private String resumeDocType;
  private List<Skill> skills;
  private String emailId;
  private Integer phoneNo;
  private Integer alternatePhoneNo;
  private String firstName;
  private String lastName;
  private String gender;
  private Integer noticePeroidInMonths;
  private Long lastUpdated;
  private String profilePic;
  private String linkedinLink;
  private List<Certification> certifications;
  private String address;
  private String state;
  private String city;
  private String country;
  private List<PreviousOrganizationSummary> previousOrganizationSummary;
  private Integer lastSalaryDrawn;
  private String currency;
  private Integer totalExperienceInMonths;
  private Integer salaryExpected;
}
