package com.ooooo.infra.dev;

import com.ooooo.infra.context.constant.ProfileConstants;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.env.Environment;
import org.springframework.test.context.ActiveProfiles;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest
public class DevEnvironmentPostProcessorTest {

  @Autowired
  private Environment environment;

  static {
    System.setProperty(ACTIVE_PROFILES_PROPERTY_NAME, ProfileConstants.DEV);
  }

  @Test
  void test() {
    String value = environment.getProperty(DevEnvironmentPostProcessor.LOCATION_PROPERTY);
    Assertions.assertThat(value).isEqualTo(DevEnvironmentPostProcessor.LOCATION_VALUE);
  }

  @SpringBootApplication
  public static class TestConfiguration {

  }
}
