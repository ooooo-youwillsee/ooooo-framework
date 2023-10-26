package com.ooooo.infra.auth.service;

import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface AuthService {

  Authentication auth(HttpServletRequest request, Map<String, Object> params);
}
