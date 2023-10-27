package com.ooooo.infra.cache.annotation;

import org.springframework.context.annotation.Primary;

import java.lang.annotation.*;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface Cache {

  String key();

}
