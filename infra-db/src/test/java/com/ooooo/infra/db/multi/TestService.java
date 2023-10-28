package com.ooooo.infra.db.multi;

import com.ooooo.infra.db.annotation.MultiDataSource;
import com.ooooo.infra.db.datasource.MultiDataSourceContextHolder;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
@MultiDataSource(name = "test")
public class TestService {
  
  public String test() {
    return MultiDataSourceContextHolder.getRoutingKey();
  }
  
  @MultiDataSource(name = "test2")
  public String test2() {
    return MultiDataSourceContextHolder.getRoutingKey();
  }
  
}
