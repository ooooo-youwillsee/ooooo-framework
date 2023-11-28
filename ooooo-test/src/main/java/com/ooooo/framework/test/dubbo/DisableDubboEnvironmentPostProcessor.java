package com.ooooo.framework.test.dubbo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.Collections;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class DisableDubboEnvironmentPostProcessor implements EnvironmentPostProcessor {

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    Map<String, Object> source = Collections.singletonMap("dubbo.enabled", false);
    MapPropertySource propertySource = new MapPropertySource(getClass().getName(), source);
    environment.getPropertySources().addLast(propertySource);
  }
}
