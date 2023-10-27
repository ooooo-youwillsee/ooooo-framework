package com.ooooo.infra.cache.aspect;

import com.ooooo.infra.cache.CacheService;
import com.ooooo.infra.cache.annotation.Cache;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@Aspect
@Component
public class CacheAspect {

  @Autowired
  private CacheService cacheService;

  @Pointcut("@annotation(com.ooooo.infra.cache.annotation.Cache)")
  public void cache() {

  }

  @Around("cache()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    if (!(pjp.getSignature() instanceof MethodSignature)) {
      return pjp.proceed();
    }

    MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
    Method method = methodSignature.getMethod();
    Class<?> returnType = method.getReturnType();
    Cache cache = method.getAnnotation(Cache.class);

    // get value from cache
    Object value = null;
    try {
      value = cacheService.get(cache.key(), returnType);
    } catch (Throwable e) {
      log.error("@Cache execute get key '{}' error", cache.key(), e);
    }
    if (value == null) {
      // execute db
      value = pjp.proceed();
      try {
        cacheService.set(cache.key(), value);
      } catch (Throwable e) {
        log.error("@Cache execute set key '{}' error", cache.key(), e);
      }
    }
    return value;
  }

}
