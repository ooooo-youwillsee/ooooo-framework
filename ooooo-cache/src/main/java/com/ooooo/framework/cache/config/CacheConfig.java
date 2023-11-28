package com.ooooo.framework.cache.config;

import com.ooooo.framework.cache.RedisCacheService;
import com.ooooo.framework.cache.CacheService;
import com.ooooo.framework.cache.CacheServices;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(CacheProperties.class)
public class CacheConfig {

  @Bean
  @Primary
  public CacheServices cacheServices() {
    return new CacheServices();
  }

  @Configuration
  @ConditionalOnClass(RedisConnectionFactory.class)
  static class Redis {

    @Bean
    public CacheService redisCacheService() {
      return new RedisCacheService();
    }
  }


}
