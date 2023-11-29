package com.ooooo.framework.trace.util;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class TraceUtil {

  private static ThreadLocal<TraceInfo> traceThreadLocal = ThreadLocal.withInitial(TraceInfo::new);

  public static String getTraceId() {
    return traceThreadLocal.get().getTraceId();
  }

  public static void setTraceId(String traceId) {
    traceThreadLocal.get().setTraceId(traceId);
  }

  @Getter
  @Setter
  private static class TraceInfo {

    private String traceId;
  }
}
