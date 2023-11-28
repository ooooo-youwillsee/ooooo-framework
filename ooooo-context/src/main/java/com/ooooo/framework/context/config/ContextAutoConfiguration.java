package com.ooooo.framework.context.config;

import com.ooooo.framework.context.expression.ExpressionParser;
import com.ooooo.framework.context.expression.DefaultExpressionParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
public class ContextAutoConfiguration {

  @Bean
  @ConditionalOnMissingBean
  public ExpressionParser expressionParser() {
    return new DefaultExpressionParser();
  }
}
