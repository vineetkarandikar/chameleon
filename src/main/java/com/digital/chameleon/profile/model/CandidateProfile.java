package com.digital.chameleon.profile.model;

import java.util.List;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import com.digital.chameleon.onboarding.profile.dto.Skill;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName = "candidateprofile")
public class CandidateProfile {

  @Id
  @Field(type = FieldType.Text, name = "emailId")
  private String emailId;

  @Field(type = FieldType.Text, name = "firstName")
  private String firstName;

  @Field(type = FieldType.Text, name = "lastName")
  private String lastName;

  @Field(type = FieldType.Text, name = "gender")
  private String gender;

  @Field(type = FieldType.Text, name = "resumeDoc")
  private String resumeDoc;

  @Field(type = FieldType.Text, name = "resumeDocType")
  private String resumeDocType;

  @Field(type = FieldType.Text, name = "resumeText")
  private String resumeText;

  @Field(type = FieldType.Nested, name = "skills")
  private List<Skill> skills;

  @Field(type = FieldType.Integer)
  private Integer phoneNo;
  
  @Field(type = FieldType.Integer)
  private Integer alternatePhoneNo;

  @Field(type = FieldType.Integer, name = "noticePeroidInMonths")
  private Integer noticePeroidInMonths;

  @Field(type = FieldType.Long, name = "lastUpdated")
  private Long lastUpdated;

  @Field(type = FieldType.Text, name = "profilePic")
  private String profilePic;

  @Field(type = FieldType.Text, name = "linkedinLink")
  private String linkedinLink;

  @Field(type = FieldType.Nested, name = "certifications")
  private List<Certification> certifications;

  @Field(type = FieldType.Text, name = "address")
  private String address;

  @Field(type = FieldType.Text, name = "state")
  private String state;

  @Field(type = FieldType.Text, name = "city")
  private String city;

  @Field(type = FieldType.Text, name = "country")
  private String country;

  @Field(type = FieldType.Nested, name = "previousOrganizationSummary")
  private List<PreviousOrganizationSummary> previousOrganizationSummary;

  @Field(type = FieldType.Integer, name = "lastSalaryDrawn")
  private Integer lastSalaryDrawn;

  @Field(type = FieldType.Text, name = "currency")
  private String currency;

  @Field(type = FieldType.Integer, name = "totalExperienceInMonths")
  private Integer totalExperienceInMonths;

  @Field(type = FieldType.Integer, name = "salaryExpected")
  private Integer salaryExpected;

}
