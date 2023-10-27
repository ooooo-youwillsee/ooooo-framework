package com.ooooo.infra.cache.config;

import com.ooooo.infra.cache.CacheService;
import com.ooooo.infra.cache.CacheServices;
import com.ooooo.infra.cache.RedisCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;

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

  @ConditionalOnClass(RedisConnectionFactory.class)
  static class Redis {

    @Bean
    public CacheService redisCacheService() {
      return new RedisCacheService();
    }
  }


}
