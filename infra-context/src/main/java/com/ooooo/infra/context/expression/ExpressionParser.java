package com.ooooo.infra.context.expression;

import java.util.Map;

/**
 * SpEL Expression Parser
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 */
public interface ExpressionParser {

    String EXPRESSION = "expression";

    <T> T parse(String expression, Map<String, Object> variables, Class<T> returnClazz);
}
