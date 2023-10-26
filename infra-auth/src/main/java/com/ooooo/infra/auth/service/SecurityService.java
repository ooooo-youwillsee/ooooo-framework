package com.ooooo.infra.auth.service;

import com.ooooo.infra.auth.endpoint.AccessToken;
import org.springframework.security.core.Authentication;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface SecurityService {

  AccessToken resolve(Authentication authentication);

  Authentication resolve(String accessToken);
}
