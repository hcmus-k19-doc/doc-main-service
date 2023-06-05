package edu.hcmus.doc.mainservice.model.exception;

public class DocMainServiceRuntimeException extends RuntimeException {

  public DocMainServiceRuntimeException(String message) {
    super(message);
  }

  public DocMainServiceRuntimeException(String message, Throwable cause) {
    super(message, cause);
  }
}
