package com.ooooo.infra.auth.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.core.Authentication;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SecurityServices implements SecurityService {

  @Autowired(required = false)
  private List<SecurityService> securityServices;

  @Override
  public AccessToken authorize(HttpServletRequest request) {
    if (securityServices == null || securityServices.isEmpty()) {
      throw new IllegalArgumentException("SecurityService is null");
    }

    AccessToken accessToken = null;
    for (SecurityService authService : securityServices) {
      accessToken = authService.authorize(request);
      if (accessToken != null) {
        break;
      }
    }
    if (accessToken == null) {
      throw new IllegalArgumentException("not found suitable SecurityService");
    }
    return accessToken;
  }

  @Override
  public Authentication authenticate(HttpServletRequest request) {
    if (securityServices == null || securityServices.isEmpty()) {
      return null;
    }
    for (SecurityService securityService : securityServices) {
      Authentication authentication = securityService.authenticate(request);
      if (authentication != null) {
        return authentication;
      }
    }
    return null;
  }

  @PostConstruct
  public void init() {
    if (securityServices != null) {
      securityServices.remove(this);
      securityServices.sort(AnnotationAwareOrderComparator.INSTANCE);
    }
  }
}
