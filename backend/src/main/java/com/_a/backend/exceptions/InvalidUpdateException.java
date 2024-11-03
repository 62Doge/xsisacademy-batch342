package com._a.backend.exceptions;

public class InvalidUpdateException extends RuntimeException {
  public InvalidUpdateException() {
    super("Update tidak valid.");
  }

  public InvalidUpdateException(String message) {
    super(message);
  }
}
