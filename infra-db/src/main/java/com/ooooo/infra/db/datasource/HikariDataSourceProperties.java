package com.ooooo.infra.db.datasource;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.convert.DurationUnit;

import javax.sql.DataSource;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */

@Getter
@Setter
public class HikariDataSourceProperties {

  private String connectionTestQuery;

  private String poolName;

  private String driverClassName;

  private String url;

  private String username;

  private String password;

  private int maxPoolSize = 10;

  private int minIdle = 1;

  private boolean autoCommit = true;

  private boolean registerMbeans = true;

  @DurationUnit(ChronoUnit.MINUTES)
  private Duration idleTimeout = Duration.ofMinutes(10);

  @DurationUnit(ChronoUnit.MINUTES)
  private Duration maxLifetime = Duration.ofMinutes(30);

  @DurationUnit(ChronoUnit.SECONDS)
  private Duration connectionTimeout = Duration.ofSeconds(30);

  private Map<String, String> dataSourceProperties = new HashMap<>();

  public DataSource dataSource() {
    HikariConfig config = new HikariConfig();
    config.setPoolName(getPoolName());
    config.setDriverClassName(getDriverClassName());
    config.setJdbcUrl(getUrl());
    config.setUsername(getUsername());
    config.setPassword(getPassword());
    config.setMinimumIdle(getMinIdle());
    config.setMaximumPoolSize(getMaxPoolSize());
    config.setAutoCommit(isAutoCommit());
    config.setIdleTimeout(getIdleTimeout().toMillis());
    config.setMaxLifetime(getMaxLifetime().toMillis());
    config.setConnectionTimeout(getConnectionTimeout().toMillis());
    config.setConnectionTestQuery(getConnectionTestQuery());
    config.setRegisterMbeans(isRegisterMbeans());
    for (Entry<String, String> entry : getDataSourceProperties().entrySet()) {
      String key = entry.getKey(), value = entry.getValue();
      config.addDataSourceProperty(key, value);
    }
    return new HikariDataSource(config);
  }


}


