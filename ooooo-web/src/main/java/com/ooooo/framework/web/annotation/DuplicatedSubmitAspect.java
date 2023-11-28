package com.ooooo.framework.web.annotation;

import cn.hutool.core.util.StrUtil;
import com.ooooo.framework.web.exception.DuplicatedSubmitException;
import com.ooooo.framework.web.support.DuplicatedSubmitKeyGenerator;
import lombok.Setter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.util.WebUtils;

import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Aspect
@Component
public class DuplicatedSubmitAspect implements ApplicationContextAware {

  @Autowired
  private StringRedisTemplate redisTemplate;

  @Setter
  private ApplicationContext applicationContext;

  @Pointcut("@within(com.ooooo.framework.web.annotation.DuplicatedSubmit) || @annotation(com.ooooo.framework.web.annotation.DuplicatedSubmit)")
  public void duplicatedSubmit() {
  }

  @Around("duplicatedSubmit()")
  public Object around(ProceedingJoinPoint pjp) throws Throwable {
    DuplicatedSubmit duplicatedSubmit = findAnnotation(pjp);
    if (duplicatedSubmit == null) {
      return pjp.proceed();
    }

    boolean success = canSubmit(pjp, duplicatedSubmit);
    if (success) {
      return pjp.proceed();
    }
    throw new DuplicatedSubmitException("您不能重复提交，请稍后再试");
  }

  private boolean canSubmit(ProceedingJoinPoint pjp, DuplicatedSubmit duplicatedSubmit) {
    String key = duplicatedSubmit.key();
    if (StrUtil.isBlank(key)) {
      // use keyGenerator
      Class<? extends DuplicatedSubmitKeyGenerator> clazz = duplicatedSubmit.keyGenerator();
      DuplicatedSubmitKeyGenerator generator = applicationContext.getBean(clazz);
      key = generator.generateKey(pjp.getArgs());
    }

    Boolean success = redisTemplate.opsForValue().setIfAbsent(key, "1", duplicatedSubmit.lockInMilliSecond(), TimeUnit.MILLISECONDS);
    return Boolean.TRUE.equals(success);
  }

  private DuplicatedSubmit findAnnotation(ProceedingJoinPoint pjp) {
    if (!(pjp.getSignature() instanceof MethodSignature)) {
      return null;
    }
    Method method = ((MethodSignature) pjp.getSignature()).getMethod();
    DuplicatedSubmit duplicatedSubmit = method.getAnnotation(DuplicatedSubmit.class);
    if (duplicatedSubmit != null) {
      return duplicatedSubmit;
    }
    return method.getDeclaringClass().getAnnotation(DuplicatedSubmit.class);
  }
}
