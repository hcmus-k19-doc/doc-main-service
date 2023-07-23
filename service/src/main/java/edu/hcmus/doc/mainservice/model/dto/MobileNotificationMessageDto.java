package edu.hcmus.doc.mainservice.model.dto;

import lombok.Data;

@Data
public class MobileNotificationMessageDto {

  private String title;
  private String body;
  private String token;
}
