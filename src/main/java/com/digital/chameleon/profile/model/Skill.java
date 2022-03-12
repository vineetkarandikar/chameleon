package com.digital.chameleon.profile.model;

import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Skill {
  
  @Field(type = FieldType.Text)
  private String name;
  
  @Field(type = FieldType.Integer)
  private Integer expereienceInMonths;
}
