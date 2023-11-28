package com.ooooo.framework.web.support;

import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
public class ToStringDuplicatedSubmitKeyGenerator implements DuplicatedSubmitKeyGenerator {

  @Override
  public String generateKey(Object[] args) {
    StringBuilder sb = new StringBuilder();
    for (Object arg : args) {
      if (arg == null) {
        continue;
      }
      String className = arg.getClass().getName();
      String toString = arg.toString();
      if (toString.startsWith(className)) {
        // not override toString method
        continue;
      }
      sb.append(toString);
    }
    return sb.toString();
  }

}
