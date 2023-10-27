package com.ooooo.infra.auth.endpoint;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class AuthenticateFilter extends OncePerRequestFilter {

  @Autowired
  private SecurityServices securityServices;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    SecurityContext context = SecurityContextHolder.getContext();

    if (context.getAuthentication() == null) {
      // it's not authenticated
      Authentication authentication = securityServices.authenticate(request);
      if (authentication != null && authentication.isAuthenticated()) {
        context.setAuthentication(authentication);
      }
    }

    filterChain.doFilter(request, response);
  }

}
