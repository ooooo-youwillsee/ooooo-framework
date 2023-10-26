package com.ooooo.infra.dev;

import com.ooooo.infra.context.constant.ProfileConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.context.config.ConfigDataEnvironmentPostProcessor;
import org.springframework.boot.env.EnvironmentPostProcessor;
import org.springframework.core.Ordered;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.core.env.MutablePropertySources;
import org.springframework.core.env.Profiles;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.core.env.AbstractEnvironment.ACTIVE_PROFILES_PROPERTY_NAME;

/**
 * <pre>
 *  see ConfigDataEnvironment#getInitialImportContributors(Binder)
 * </pre>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see ConfigDataEnvironmentPostProcessor
 * @since 1.0.0
 */
@Slf4j
public class DevEnvironmentPostProcessor implements EnvironmentPostProcessor, Ordered {

  public static final String NAME = "dev";

  public static final String LOCATION_PROPERTY = "spring.config.location";

  public static String CLASSPATH_CONFIG = "optional:classpath:/;optional:classpath:/config/";

  public static String FILE_CONFIG = "optional:file:../../;optional:file:../;optional:file:./;optional:file:./config/;optional:file:./config/*/";

  public static String LOCATION_VALUE;

  static {
    LOCATION_VALUE = CLASSPATH_CONFIG + ";" + FILE_CONFIG;
  }

  @Override
  public void postProcessEnvironment(ConfigurableEnvironment environment, SpringApplication application) {
    String activeProfiles = environment.getProperty(ACTIVE_PROFILES_PROPERTY_NAME);
    if (activeProfiles == null || !activeProfiles.contains(ProfileConstants.DEV)) {
      return;
    }
    log.info("[dev environment, reset '{}']", LOCATION_PROPERTY);
    MutablePropertySources propertySources = environment.getPropertySources();
    if (propertySources.contains(NAME)) {
      return;
    }
    propertySources.addFirst(createDevPropertySource());
  }

  private MapPropertySource createDevPropertySource() {
    Map<String, Object> map = new HashMap<>();
    map.put(LOCATION_PROPERTY, LOCATION_VALUE);
    return new MapPropertySource(NAME, map);
  }


  @Override
  public int getOrder() {
    return ConfigDataEnvironmentPostProcessor.ORDER - 5;
  }
}
