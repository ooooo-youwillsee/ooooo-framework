package com.ooooo.framework.cache;

import com.alibaba.fastjson.JSON;
import com.ooooo.framework.cache.config.CacheProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 * todo value serialization
 */
public class RedisCacheService implements CacheService {

  private static final String NAME = "redis";

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Autowired
  private CacheProperties cacheProperties;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    String value = redisTemplate.opsForValue().get(prefixKey(key));
    return JSON.parseObject(value, clazz);
  }

  @Override
  public void set(String key, Object value) {
    redisTemplate.opsForValue().set(prefixKey(key), JSON.toJSONString(value), cacheProperties.getTtl());
  }

  @Override
  public void delete(String key) {
    redisTemplate.delete(key);
  }

  private String prefixKey(String key) {
    if (cacheProperties.getPrefix() != null) {
      return cacheProperties.getPrefix() + key;
    }
    return key;
  }
}
