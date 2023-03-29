package edu.hcmus.doc.mainservice.model.exception;

public class FileServiceFailureException extends RuntimeException {

  public static final String FILE_SERVICE_FAILURE = "file_service.failure";

  public FileServiceFailureException(String message) {
    super(message);
  }
}
