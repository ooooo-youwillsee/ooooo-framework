package com.ooooo.framework.context.expression.function;

import org.springframework.expression.EvaluationContext;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface ExpressionFunction {

  String getName();

  Object getValue();

}
