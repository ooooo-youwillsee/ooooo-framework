package com.ooooo.infra.auth.endpoint;

import com.ooooo.infra.context.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@RestController
public class AuthorizeEndpoint {

  public static final String TOKEN_URL = "/authless/token";

  @Autowired
  private SecurityServices securityServices;

  @PostMapping(TOKEN_URL)
  public Result<AccessToken> token(HttpServletRequest request) {
    AccessToken accessToken = securityServices.authorize(request);
    return Result.success(accessToken);
  }


}
