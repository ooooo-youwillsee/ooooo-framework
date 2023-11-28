package com.ooooo.framework.auth;

import com.ooooo.framework.context.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@SpringBootApplication
@RestController
public class AuthTestConfiguration {

  @Bean
  public UsernamePasswordSecurityService usernamePasswordSecurityService() {
    return new UsernamePasswordSecurityService();
  }

  /**
   * 测试 /authless/** 不拦截请求
   */
  @PostMapping("/authlesss/test1")
  public Result<?> test1() {
    return Result.success("test1");
  }

  /**
   * <p>@AuthenticationPrincipal 是 spring 原生提供的注解</p>
   *
   * @param principal
   * @return
   */
  @PostMapping("/auth/test2")
  public Result<?> test2(@AuthenticationPrincipal String principal) {
    return Result.success("test2:" + principal);
  }

}
