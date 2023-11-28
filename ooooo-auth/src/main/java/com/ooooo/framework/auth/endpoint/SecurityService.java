package com.ooooo.framework.auth.endpoint;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface SecurityService {

  /**
   * 授权
   *
   * @param request
   * @return
   */
  AccessToken authorize(HttpServletRequest request);

  /**
   * 认证
   *
   * @param request
   * @return
   */
  Authentication authenticate(HttpServletRequest request);
}
