package com.ooooo.framework.auth;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.ooooo.framework.auth.endpoint.AbstractSecurityService;
import com.ooooo.framework.auth.endpoint.AccessTokenAuthentication;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Date;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class UsernamePasswordSecurityService extends AbstractSecurityService {

  @Override
  protected AccessTokenAuthentication auth(HttpServletRequest request) {
    Map<String, Object> body;
    try {
      body = JSON.parseObject(request.getInputStream(), Map.class);
    } catch (IOException e) {
      log.error("Parsing parameter error", e);
      return null;
    }
    String username = MapUtil.getStr(body, "username");
    String password = MapUtil.getStr(body, "password");
    // validate username and password
    AccessTokenAuthentication authentication = new AccessTokenAuthentication();
    if (StrUtil.equals(username, "tom") && StrUtil.equals(password, "123")) {
      authentication.setPrincipal("1");
      authentication.setExpiresAt(DateUtil.offsetHour(new Date(), 1).toJdkDate());
      return authentication;
    }
    return authentication;
  }
}
