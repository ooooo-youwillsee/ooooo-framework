package com.ooooo.framework.trace.web;

import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.ooooo.framework.trace.constant.TraceConstant;
import com.ooooo.framework.trace.util.TraceUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.StringUtils;
import org.springframework.boot.web.servlet.filter.OrderedFilter;
import org.springframework.core.Ordered;
import org.springframework.http.MediaType;
import org.springframework.util.MultiValueMap;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;
import org.springframework.web.util.WebUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class WebTraceFilter extends OncePerRequestFilter implements OrderedFilter {

  public static final int ORDER = Ordered.HIGHEST_PRECEDENCE + 20;

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    ContentCachingRequestWrapper requestWrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
    ContentCachingResponseWrapper responseWrapper = WebUtils.getNativeResponse(response, ContentCachingResponseWrapper.class);
    if (requestWrapper == null || responseWrapper == null) {
      filterChain.doFilter(request, response);
      return;
    }

    Date startTime = new Date();
    try {
      filterChain.doFilter(request, response);
    } finally {
      Date endTime = new Date();
      logHTTP(startTime, endTime, requestWrapper, responseWrapper);
    }
  }

  private void logHTTP(Date startTime, Date endTime, ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
    try {
      String traceId = getOrCreateTraceId(request);
      String requestUri = getRequestUri(request);
      long cost = endTime.getTime() - startTime.getTime();
      logHTTPRequest(traceId, requestUri, request);
      logHTTPResponse(traceId, requestUri, response, cost);
    } catch (Throwable t) {
      log.warn("日志打印失败，不影响调用, {}", t.getMessage());
    }
  }

  private String getOrCreateTraceId(ContentCachingRequestWrapper request) {
    String traceId = TraceUtil.getTraceId();
    traceId = StrUtil.emptyToDefault(traceId, request.getHeader(TraceConstant.TRACE_ID));
    traceId = StrUtil.emptyToDefault(traceId, request.getParameter(TraceConstant.TRACE_ID));
    traceId = StrUtil.emptyToDefault(traceId, (String) request.getAttribute(TraceConstant.TRACE_ID));
    // generate a traceId
    if (StrUtil.isBlank(traceId)) {
      traceId = RandomUtil.randomString(6);
    }
    // reset traceId
    TraceUtil.setTraceId(traceId);
    return traceId;
  }

  private void logHTTPRequest(String traceId, String requestUri, ContentCachingRequestWrapper request) {
    String content = getContent(request);
    log.info(String.format("HttpRequest[%s][%s][%s][%s][%s][%d]:%s %s", traceId, request.getProtocol(), request.getMethod(), request.getContentType(), request.getCharacterEncoding(), request.getContentLengthLong(), requestUri, content));
  }

  private void logHTTPResponse(String traceId, String requestUri, ContentCachingResponseWrapper response, long cost) {
    String content = getContent(response);
    log.info(String.format("HttpResponse[%s][%s][%s]:%s %dms %s", traceId, response.getStatus(), response.getContentType(), requestUri, cost, content));
  }


  private static String getRequestUri(HttpServletRequest request) {
    String requestUri = null;
    if (StrUtil.isEmpty(request.getQueryString())) {
      requestUri = request.getRequestURI();
    } else {
      requestUri = request.getRequestURI() + "?" + request.getQueryString();
    }
    return requestUri;
  }

  private String getContent(ContentCachingRequestWrapper request) {
    String content = null;
    String contentType = request.getContentType();
    if (contentType == null) {
      contentType = MediaType.APPLICATION_JSON_VALUE;
    }

    // content-type 可能是 "application/json;charset=UTF-8"
    if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
      content = handleApplicationJSON(request);
      // todo 日志脱敏
    } else if (contentType.contains(MediaType.APPLICATION_FORM_URLENCODED_VALUE)) {
      content = handleApplicationFormUrlEncoded(request);
    } else if (contentType.contains(MediaType.MULTIPART_FORM_DATA_VALUE)) {
      content = handleMultiPartData(request);
    } else {
      log.warn("WebRequest parameter analysis not support. contentType: {}", contentType);
    }
    return content;
  }

  private String getContent(ContentCachingResponseWrapper response) {
    String contentType = response.getContentType();
    if (contentType == null) {
      contentType = MediaType.APPLICATION_JSON_VALUE;
    }

    String content = null;
    if (contentType.contains(MediaType.APPLICATION_JSON_VALUE)) {
      content = handleApplicationJSON(response);
      // todo 日志脱敏
    } else {
      log.warn("WebResponse parameter analysis not support. contentType: {}", contentType);
    }
    return content;
  }

  private String handleMultiPartData(ContentCachingRequestWrapper request) {
    StringBuilder sb = new StringBuilder();
    // form
    MultipartHttpServletRequest multipartHttpServletRequest = ((MultipartHttpServletRequest) request);
    Map<String, String[]> parameterMap = multipartHttpServletRequest.getParameterMap();
    if (MapUtil.isNotEmpty(parameterMap)) {
      parameterMap.forEach((key, value) -> sb.append("[").append(key).append("=").append(StringUtils.join(value, ",")).append("]"));
    }
    // file
    MultiValueMap<String, MultipartFile> multiFileMap = multipartHttpServletRequest.getMultiFileMap();
    if (MapUtil.isNotEmpty(multiFileMap)) {
      multiFileMap.forEach((key, entity) -> {
        MultipartFile multipartFile = entity.get(0);
        String value = String.format("%s,%s,%s,%sKB", multipartFile.getName(), multipartFile.getContentType(), multipartFile.getOriginalFilename(), multipartFile.getSize() / 1024);
        sb.append("[").append(key).append("=").append(value).append("]");
      });
    }
    return sb.toString();
  }

  private String handleApplicationFormUrlEncoded(ContentCachingRequestWrapper request) {
    StringBuilder sb = new StringBuilder();
    Map<String, String[]> parameterMap = request.getParameterMap();
    if (MapUtil.isNotEmpty(parameterMap)) {
      parameterMap.forEach((key, value) -> sb.append("[").append(key).append("=").append(StringUtils.join(value, ",")).append("]"));
    }
    return sb.toString();
  }

  @SneakyThrows
  private String handleApplicationJSON(ContentCachingRequestWrapper request) {
    String encoding = StrUtil.emptyToDefault(request.getCharacterEncoding(), StandardCharsets.UTF_8.name());
    return new String(request.getContentAsByteArray(), Charset.forName(encoding));
  }

  private String handleApplicationJSON(ContentCachingResponseWrapper response) {
    String encoding = StrUtil.emptyToDefault(response.getCharacterEncoding(), StandardCharsets.UTF_8.name());
    return new String(response.getContentAsByteArray(), Charset.forName(encoding));
  }

  @Override
  public int getOrder() {
    return ORDER;
  }
}
