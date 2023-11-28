package com.ooooo.framework.web.annotation;

import com.ooooo.framework.web.support.DuplicatedSubmitKeyGenerator;
import com.ooooo.framework.web.support.ToStringDuplicatedSubmitKeyGenerator;

import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface DuplicatedSubmit {

  /**
   * 指定key
   *
   * @return
   */
  String key() default "";

  /**
   * 根据策略来生成key
   *
   * @return
   */
  Class<? extends DuplicatedSubmitKeyGenerator> keyGenerator() default ToStringDuplicatedSubmitKeyGenerator.class;

  /**
   * 锁定的时间间隔
   *
   * @return
   */
  long lockInMilliSecond() default 3000;

}
