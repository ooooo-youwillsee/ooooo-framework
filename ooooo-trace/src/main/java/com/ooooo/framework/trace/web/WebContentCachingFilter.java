package com.ooooo.framework.trace.web;

import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class WebContentCachingFilter extends OncePerRequestFilter implements OrderedFilter {

  public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 10;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    if (requestWrapper == null) {
      requestWrapper = new ContentCachingRequestWrapper(request);
    }

    ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (responseWrapper == null) {
      responseWrapper = new ContentCachingResponseWrapper(response);
    }

    filterChain.doFilter(requestWrapper, responseWrapper);

    if (!isAsyncStarted(requestWrapper)) {
      responseWrapper.copyBodyToResponse();
    }
  }


  @Override
  public int getOrder() {
    return ORDER;
  }
}
