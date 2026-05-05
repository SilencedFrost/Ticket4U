package com.ticket4u.core.exceptions;

public class CategoryNotFoundException extends RuntimeException {
  public CategoryNotFoundException(String message) {
    super(message);
  }

  public CategoryNotFoundException(Integer id) {
    super(String.format("Category not found with id: %s", id.toString()));
  }
}