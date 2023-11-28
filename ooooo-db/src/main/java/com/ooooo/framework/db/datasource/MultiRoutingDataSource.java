package com.ooooo.framework.db.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class MultiRoutingDataSource extends AbstractRoutingDataSource {

  private final MultiDataSourceProperties multiDataSourceProperties;

  public MultiRoutingDataSource(MultiDataSourceProperties multiDataSourceProperties) {
    this.multiDataSourceProperties = multiDataSourceProperties;
    init();
  }

  private void init() {
    // set default datasource
    setDefaultTargetDataSource(multiDataSourceProperties.dataSource());

    // set multi datasource
    Map<Object, Object> multiDataSource = new HashMap<>();
    for (Entry<String, HikariDataSourceProperties> entry : multiDataSourceProperties.getMulti().entrySet()) {
      multiDataSource.put(entry.getKey(), entry.getValue().dataSource());
    }
    setTargetDataSources(multiDataSource);
  }


  @Override
  protected Object determineCurrentLookupKey() {
    String routingKey = MultiDataSourceContextHolder.getRoutingKey();
    return routingKey;
  }
}
