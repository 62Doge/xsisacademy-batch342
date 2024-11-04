package com._a.backend.exceptions;

public class NewPasswordConfirmationException extends RuntimeException {
  public NewPasswordConfirmationException(String message) {
    super(message);
  }

  public NewPasswordConfirmationException(String message, Throwable cause) {
    super(message, cause);
  }
}
