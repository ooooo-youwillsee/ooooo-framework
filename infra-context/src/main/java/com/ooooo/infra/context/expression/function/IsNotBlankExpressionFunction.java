package com.ooooo.infra.context.expression.function;

import cn.hutool.core.util.StrUtil;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
public class IsNotBlankExpressionFunction implements ExpressionFunction {

  public static final String IS_NOT_BLANK = "isNotBlank";

  public static final Method METHOD = ReflectionUtils.findMethod(IsNotBlankExpressionFunction.class, IS_NOT_BLANK, Object[].class);


  @Override
  public String getName() {
    return IS_NOT_BLANK;
  }

  @Override
  public Object getValue() {
    return METHOD;
  }


  public static boolean isNotBlank(Object... args) {
    if (args == null) {
      return false;
    }

    for (Object arg : args) {
      if (arg == null) {
        return false;
      }
      if (arg instanceof String && StrUtil.isBlank((String) arg)) {
        return false;
      }
    }

    return true;
  }

}
