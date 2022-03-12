package com.digital.chameleon.profile.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PreviousOrganizationSummary {
  
  @Field(type = FieldType.Text)
  private String orgName;
  
  @Field(type = FieldType.Text)
  private String designation;
  
  @Field(type = FieldType.Integer)
  private Integer tenureInMonths;
}
