package com.ooooo.infra.db.datasource;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = "spring.datasource")
public class MultiDataSourceProperties extends HikariDataSourceProperties {

  /**
   * 动态数据源
   */
  @NestedConfigurationProperty
  private Map<String, HikariDataSourceProperties> multi = new HashMap<>();
}
