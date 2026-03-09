package com.ticket4u.exception;

import java.util.UUID;

public class EventNotFoundException extends RuntimeException {
  public EventNotFoundException(String message) {
    super(message);
  }

  public  EventNotFoundException(UUID id) {
    super(String.format("Event not found with id: %s", id.toString()));
  }
}