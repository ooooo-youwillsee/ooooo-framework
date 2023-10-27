package com.ooooo.infra.cache;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface CacheService {

  String getName();

  <T> T get(String key, Class<T> clazz);

  void set(String key, Object value);
}
