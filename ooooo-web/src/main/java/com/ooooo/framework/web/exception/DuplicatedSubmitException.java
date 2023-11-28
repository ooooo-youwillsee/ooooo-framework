package com.ooooo.framework.web.exception;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class DuplicatedSubmitException extends RuntimeException{

  public DuplicatedSubmitException(String message) {
    super(message);
  }
}
