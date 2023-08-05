package edu.hcmus.doc.mainservice.model.exception;

public class DocBusinessException extends RuntimeException {

  public static final String DOCUMENT_REQUIRED = "doc.exception.document_required";

  public DocBusinessException(String message) {
    super(message);
  }
}
