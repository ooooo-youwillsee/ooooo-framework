package com.ooooo.framework.cache;

import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface CacheService {

  String getName();

  <T> T get(String key, Class<T> clazz);

  void set(String key, Object value);

  void delete(String key);
}
