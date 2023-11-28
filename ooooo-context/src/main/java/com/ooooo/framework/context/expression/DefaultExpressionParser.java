package com.ooooo.framework.context.expression;

import com.ooooo.framework.context.expression.function.ExpressionFunction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.expression.*;
import org.springframework.core.convert.ConversionService;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.spel.SpelParserConfiguration;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.expression.spel.support.StandardTypeConverter;
import org.springframework.expression.spel.support.StandardTypeLocator;

import java.util.List;
import java.util.Map;

import static org.springframework.expression.ParserContext.TEMPLATE_EXPRESSION;

/**
 * SpEL Expression Parser
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 */

public class DefaultExpressionParser implements ExpressionParser {

  private static final String[] EXPRESSION_SYMBOL = {"#"};

  private final SpelExpressionParser parser = new SpelExpressionParser(new SpelParserConfiguration(true, true));

  @Autowired
  private ApplicationContext applicationContext;

  @Autowired(required = false)
  private List<ExpressionFunction> functions;


  @Override
  public <T> T parse(String expression, Map<String, Object> variables, Class<T> returnClazz) {
    // wrap expression
    expression = wrapExpression(expression);
    // context
    EvaluationContext context = createEvaluationContext();
    setVariables(context, variables);
    setFunctions(context, functions);
    // parse expression
    Expression parseExpression = parser.parseExpression(expression, TEMPLATE_EXPRESSION);
    return parseExpression.getValue(context, variables, returnClazz);
  }

  private String wrapExpression(String expression) {
    if (expression == null) {
      throw new IllegalArgumentException("expression is null");
    }
    expression = expression.trim();
    if (expression.startsWith(TEMPLATE_EXPRESSION.getExpressionPrefix())) {
      return expression;
    }

    // for example:
    // conditional: "#organ_flag == '1'"
    for (String symbol : EXPRESSION_SYMBOL) {
      if (expression.startsWith(symbol)) {
        return TEMPLATE_EXPRESSION.getExpressionPrefix() + expression + TEMPLATE_EXPRESSION.getExpressionSuffix();
      }
    }
    return expression;
  }

  private void setVariables(EvaluationContext evaluationContext, Map<String, Object> variables) {
    variables.forEach(evaluationContext::setVariable);
    evaluationContext.setVariable("variables", variables);
  }

  private void setFunctions(EvaluationContext context, List<ExpressionFunction> functions) {
    if (functions != null) {
      for (ExpressionFunction function : functions) {
        context.setVariable(function.getName(), function.getValue());
      }
    }
  }

  private EvaluationContext createEvaluationContext() {
    StandardEvaluationContext standardEvaluationContext = new StandardEvaluationContext(applicationContext.getAutowireCapableBeanFactory());
    standardEvaluationContext.addPropertyAccessor(new BeanExpressionContextAccessor());
    standardEvaluationContext.addPropertyAccessor(new BeanFactoryAccessor());
    standardEvaluationContext.addPropertyAccessor(new MapAccessor());
    standardEvaluationContext.addPropertyAccessor(new EnvironmentAccessor());
    standardEvaluationContext.setBeanResolver(new BeanFactoryResolver(applicationContext));
    standardEvaluationContext.setTypeLocator(new StandardTypeLocator(((ConfigurableBeanFactory) applicationContext.getAutowireCapableBeanFactory()).getBeanClassLoader()));
    ConversionService conversionService = ((ConfigurableBeanFactory) applicationContext.getAutowireCapableBeanFactory()).getConversionService();
    if (conversionService != null) {
      standardEvaluationContext.setTypeConverter(new StandardTypeConverter(conversionService));
    }
    return standardEvaluationContext;
  }
}
