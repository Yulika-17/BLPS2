package com.nullnumber1.lab1.exception.not_found;

public class ResourceNotFoundException extends RuntimeException {
  public ResourceNotFoundException(String resourceType, String keyName, String keyValue) {
    super(resourceType + " with " + keyName + "[" + keyValue + "] not found in the system");
  }
}
