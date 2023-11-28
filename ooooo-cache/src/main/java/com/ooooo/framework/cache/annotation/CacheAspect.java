package com.ooooo.framework.cache.annotation;

import cn.hutool.core.util.StrUtil;
import com.ooooo.framework.cache.CacheService;
import com.ooooo.framework.context.expression.ExpressionParser;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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

  @Autowired
  private ExpressionParser expressionParser;

  @Pointcut("@annotation(com.ooooo.framework.cache.annotation.Cache)")
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

    // calc key and cached
    String name = generateName(cache, method);
    String key = generateKey(cache, method, pjp.getArgs());
    String finalKey = StrUtil.join(StrUtil.COLON, name, key);
    boolean passing = conditionPassing(cache, pjp.getArgs(), methodSignature.getParameterNames());

    Object value;
    if (passing) {
      value = getValueFromCache(pjp, finalKey, returnType);
    } else {
      value = pjp.proceed();
    }
    return value;
  }


  /**
   * get value from cache
   */
  private Object getValueFromCache(ProceedingJoinPoint pjp, String key, Class<?> returnType) throws Throwable {
    Object value = null;
    try {
      value = cacheService.get(key, returnType);
    } catch (Throwable e) {
      log.error("@Cache execute get key '{}' error", key, e);
    }
    if (value == null) {
      // execute proceed
      value = pjp.proceed();
      try {
        cacheService.set(key, value);
      } catch (Throwable e) {
        log.error("@Cache execute set key '{}' error", key, e);
      }
    }
    return value;
  }

  boolean conditionPassing(Cache cache, Object[] args, String[] parameterNames) {
    String condition = cache.condition();
    if (StrUtil.isBlank(condition)) {
      return true;
    }
    Map<String, Object> variables = new HashMap<>();
    for (int i = 0; i < args.length; i++) {
      variables.put(parameterNames[i], args[i]);
    }
    return expressionParser.parse(condition, variables, Boolean.class);
  }

  // todo CacheNameGenerator
  String generateName(Cache cache, Method method) {
    String name = cache.name();
    if (StrUtil.isBlank(name)) {
      name = method.getDeclaringClass().getName() + "#" + method.getName();
    }
    return name;
  }

  // todo CacheKeyGenerator
  String generateKey(Cache cache, Method method, Object[] args) {
    String key = cache.key();
    if (StrUtil.isBlank(key)) {
      key = StrUtil.join(",", args);
    }
    return key;
  }

}
