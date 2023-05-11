package edu.hcmus.doc.mainservice.model.exception;

public class UserPasswordIncorrectException extends DocException {

  private static final String USER_PASSWORD_INCORRECT = "user.password.incorrect";

  public UserPasswordIncorrectException() {
    super(USER_PASSWORD_INCORRECT);
  }
}
