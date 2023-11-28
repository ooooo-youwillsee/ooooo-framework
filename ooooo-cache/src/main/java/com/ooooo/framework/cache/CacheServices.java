package com.ooooo.framework.cache;

import com.ooooo.framework.cache.exception.CacheOperationException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.function.Supplier;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class CacheServices implements CacheService {

  private static final String NAME = "caches";

  @Autowired(required = false)
  private List<CacheService> cacheServices;

  @Override
  public String getName() {
    return NAME;
  }

  @Override
  public <T> T get(String key, Class<T> clazz) {
    if (cacheServices == null) {
      return null;
    }
    for (CacheService cacheService : cacheServices) {
      T v = cacheService.get(key, clazz);
      if (v != null) {
        return v;
      }
    }
    return null;
  }

  @Override
  public void set(String key, Object value) {
    if (cacheServices == null) {
      return;
    }
    boolean success = false;
    for (CacheService cacheService : cacheServices) {
      try {
        cacheService.set(key, value);
        success = true;
      } catch (Exception e) {
        log.warn("'{}' cache set key '{}' error", cacheService.getName(), key);
      }
    }
    if (!success) {
      throw new CacheOperationException("cache set key '" + key + "' error");
    }
  }

  @Override
  public void delete(String key) {
    if (cacheServices == null) {
      return;
    }
    for (CacheService cacheService : cacheServices) {
      try {
        cacheService.delete(key);
      } catch (Exception e) {
        log.warn("'{}' cache delete key '{}' error", cacheService.getName(), key);
      }
    }
  }

  @PostConstruct
  public void init() {
    if (cacheServices != null) {
      cacheServices.sort(AnnotationAwareOrderComparator.INSTANCE);
    }
  }
}
