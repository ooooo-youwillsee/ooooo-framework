package com.ooooo.infra.cache.annotation;

import java.lang.annotation.*;
import java.lang.reflect.Method;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cache {

  /**
   * 默认为className#methodName
   *
   * @return
   * @see CacheAspect#generateName(Cache, Method)
   */
  String name() default "";

  /**
   * 默认通过参数计算
   *
   * @return
   * @see CacheAspect#generateKey(Cache, Method, Object[])
   */
  String key() default "";

  /**
   * 默认为ture
   *
   * @return
   * @see CacheAspect#conditionPassing(Cache, Object[], String[]) ()
   */
  String condition() default "";
}
