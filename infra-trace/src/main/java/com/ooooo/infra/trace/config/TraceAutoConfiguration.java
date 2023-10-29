package com.ooooo.infra.trace.config;

import com.ooooo.infra.trace.web.WebContentCachingFilter;
import com.ooooo.infra.trace.web.WebTraceFilter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.DispatcherServlet;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
public class TraceAutoConfiguration {


  @Configuration
  @ConditionalOnClass(DispatcherServlet.class)
  static class Web {

    @Bean
    @ConditionalOnMissingBean
    public WebContentCachingFilter webContentCachingFilter() {
      return new WebContentCachingFilter();
    }

    @Bean
    @ConditionalOnMissingBean
    public WebTraceFilter webTraceFilter() {
      return new WebTraceFilter();
    }

  }
}
