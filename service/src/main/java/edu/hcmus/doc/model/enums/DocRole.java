package edu.hcmus.doc.model.enums;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum DocRole {

  APPROVER("Approver"),
  REVIEWER("Reviewer"),
  SUBMITTER("Submitter");

  public final String value;
}
