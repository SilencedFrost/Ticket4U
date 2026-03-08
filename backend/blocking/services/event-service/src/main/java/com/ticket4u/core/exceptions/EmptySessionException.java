package com.ticket4u.core.exceptions;

import java.util.UUID;

public class EmptySessionException extends RuntimeException {
  public EmptySessionException(String message) {
    super(message);
  }

  public EmptySessionException(UUID id) {
    super(String.format("Event with id: %s does not have sessions", id.toString()));
  }
}