package com.ooooo.framework.db.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import static com.ooooo.framework.db.config.DBProperties.OOOOO_DB;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = OOOOO_DB)
public class DBProperties {

  public static final String OOOOO_DB = "ooooo.db";

  public static final String ENABLE_MULTI_DATA_SOURCE = "enableMultiDataSource";

  private int maxRows = 1000;

}
