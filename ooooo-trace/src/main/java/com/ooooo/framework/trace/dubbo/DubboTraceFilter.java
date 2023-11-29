package com.ooooo.framework.trace.dubbo;

import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ooooo.framework.trace.constant.TraceConstant;
import com.ooooo.framework.trace.util.TraceUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.constants.CommonConstants;
import org.apache.dubbo.common.extension.Activate;
import org.apache.dubbo.rpc.*;

import java.util.Date;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
@Activate(group = {CommonConstants.CONSUMER, CommonConstants.PROVIDER})
public class DubboTraceFilter implements Filter {

  @Override
  public Result invoke(Invoker<?> invoker, Invocation invocation) throws RpcException {
    Result result = null;
    Throwable t = null;
    Date startTime = new Date();
    String traceId = getOrCreateTraceId(invocation);
    try {
      result = invoker.invoke(invocation);
    } catch (Throwable e) {
      t = e;
      throw e;
    } finally {
      Date endTime = new Date();
      logDubbo(startTime, endTime,traceId, invocation, result, t);
    }
    return result;
  }

  private void logDubbo(Date startTime, Date endTime, String traceId, Invocation invocation, Result result, Throwable th) {
    try {
      String applicationName = getApplicationName(invocation);
      String url = getUrl(invocation);
      long cost = endTime.getTime() - startTime.getTime();
      logDubboRequest(traceId, applicationName, url, invocation);
      logDubboResponse(traceId, applicationName, url, result, cost);
    } catch (Throwable t) {
      log.warn("日志打印失败，不影响调用, {}", t.getMessage());
    }
  }

  private void logDubboRequest(String traceId, String applicationName, String url, Invocation invocation) {
    String content = getRequestContent(invocation);
    String logInfo = String.format("DubboRequest[%s][%s]:%s %s", traceId, applicationName, url, content);
    log.info(logInfo);
  }

  private void logDubboResponse(String traceId, String applicationName, String url, Result result, long cost) {
    String content = getResponseContent(result);
    String logInfo = String.format("DubboResponse[%s][%s]:%s %dms %s", traceId, applicationName, url, cost, content);
    log.info(logInfo);
  }

  private String getRequestContent(Invocation invocation) {
    Object[] arguments = invocation.getArguments();
    // todo 处理其他类型，如 byte[]
    return JSON.toJSONString(arguments);
  }

  private String getResponseContent(Result result) {
    Object value = result.getValue();
    // todo 处理其他类型, 如 byte[]
    return JSON.toJSONString(value);
  }

  private String getUrl(Invocation invocation) {
    String serviceName = invocation.getServiceName();
    serviceName = ClassUtil.getShortClassName(serviceName);
    return StrUtil.join(StrUtil.DOT, serviceName, invocation.getMethodName());
  }

  private String getOrCreateTraceId(Invocation invocation) {
    String traceId = null;
    boolean providerSide = RpcContext.getServiceContext().isProviderSide();
    if (providerSide) {
      // provider 只能接受 consumer 传递过来的 traceId
      traceId = invocation.getAttachment(TraceConstant.TRACE_ID);
    } else {
      traceId = TraceUtil.getTraceId();
      traceId = StrUtil.emptyToDefault(traceId, (String) invocation.getAttributes().get(TraceConstant.TRACE_ID));
      if (StrUtil.isBlank(traceId)) {
        // generate a traceId
        traceId = RandomUtil.randomString(6);
      }
    }
    // reset traceId
    TraceUtil.setTraceId(traceId);
    invocation.setAttachment(TraceConstant.TRACE_ID, traceId);
    return traceId;
  }

  private String getApplicationName(Invocation invocation) {
    if (invocation.getModuleModel() == null) {
      return null;
    }
    return invocation.getModuleModel().getApplicationModel().getApplicationName();
  }


}
