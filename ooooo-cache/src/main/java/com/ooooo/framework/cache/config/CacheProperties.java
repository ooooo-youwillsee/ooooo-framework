package com.ooooo.framework.cache.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.time.Duration;

import static com.ooooo.framework.cache.config.CacheProperties.PREFIX_CACHE;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Getter
@Setter
@ConfigurationProperties(prefix = PREFIX_CACHE)
public class CacheProperties {

  public static final String PREFIX_CACHE = "ooooo.cache";

  private String prefix;

  private Duration ttl = Duration.ofHours(12);
}
