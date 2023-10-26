package com.ooooo.infra.auth.endpoint;

import com.ooooo.infra.auth.service.AuthService;
import com.ooooo.infra.auth.service.SecurityService;
import com.ooooo.infra.context.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@RestController
public class AuthorizationEndpoint {

  public static final String TOKEN_URL = "/authless/token";

  @Autowired
  private SecurityService securityService;

  @Autowired(required = false)
  private List<AuthService> authServices;

  @PostMapping(TOKEN_URL)
  public Result<AccessToken> token(HttpServletRequest request, @RequestBody Map<String, Object> params) {
    Authentication authentication = getAuthentication(request, params);
    return Result.success(securityService.resolve(authentication));
  }

  private Authentication getAuthentication(HttpServletRequest request, Map<String, Object> params) {
    if (authServices == null || authServices.isEmpty()) {
      throw new IllegalArgumentException("AuthService is empty");
    }

    Authentication authentication = null;
    for (AuthService authService : authServices) {
      authentication = authService.auth(request, params);
      if (authentication != null) {
        break;
      }
    }
    if (authentication == null) {
      throw new IllegalArgumentException("not found suitable AuthService");
    }
    return authentication;
  }

  @PostConstruct
  public void init() {
    if (authServices != null) {
      authServices.sort(AnnotationAwareOrderComparator.INSTANCE);
    }
  }
}
