package com.ooooo.infra.cache;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;

import javax.annotation.PostConstruct;
import java.util.List;

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
      T t = cacheService.get(key, clazz);
      if (t != null) {
        return t;
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
      throw new RuntimeException("cache set key '" + key + "' error");
    }
  }

  @PostConstruct
  public void init() {
    if (cacheServices != null) {
      cacheServices.sort(AnnotationAwareOrderComparator.INSTANCE);
    }
  }
}
