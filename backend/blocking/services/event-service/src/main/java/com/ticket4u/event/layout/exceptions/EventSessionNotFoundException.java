package com.ticket4u.event.layout.exceptions;

import java.util.UUID;

public class EventSessionNotFoundException extends RuntimeException {
  public EventSessionNotFoundException(String message) {
    super(message);
  }

  public EventSessionNotFoundException(UUID id) {
    super(String.format("Session not found with id: %s", id.toString()));
  }
}