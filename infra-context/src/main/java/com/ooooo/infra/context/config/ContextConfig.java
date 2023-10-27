package com.ooooo.infra.context.config;

import com.ooooo.infra.context.expression.ExpressionParser;
import com.ooooo.infra.context.expression.DefaultExpressionParser;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
public class ContextConfig {

  @Bean
  @ConditionalOnMissingBean
  public ExpressionParser expressionParser() {
    return new DefaultExpressionParser();
  }
}