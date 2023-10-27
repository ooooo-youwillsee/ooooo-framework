package com.ooooo.infra.context.result;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Result<T> {

  public static final String SUCCESS_CODE = "0";

  public static final String SUCCESS_MESSAGE = "OK";

  public static final String ERROR_CODE = "-1";

  private String code;

  private String message;

  private T data;

  public static <T> Result<T> success(String code, String message, T data) {
    return new Result<>(code, message, data);
  }

  public static <T> Result<T> success(T data) {
    return new Result<>(SUCCESS_CODE, SUCCESS_MESSAGE, data);
  }

  public static <T> Result<T> fail(String code, String message) {
    return new Result<>(code, message, null);
  }

  public static <T> Result<T> fail( String message) {
    return new Result<>("-1", message, null);
  }
}
