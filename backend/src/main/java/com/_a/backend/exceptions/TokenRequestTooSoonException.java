package com._a.backend.exceptions;

public class TokenRequestTooSoonException extends RuntimeException {
  public TokenRequestTooSoonException(String message) {
    super(message);
  }

  public TokenRequestTooSoonException(String message, Throwable cause) {
    super(message, cause);
  }
}
