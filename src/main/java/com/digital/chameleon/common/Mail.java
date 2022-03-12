package com.digital.chameleon.common;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Mail {
  private String from;
  private String mailTo;
  private String subject;
  private List<Object> attachments;
  private Map<String, Object> props;
}
