package com.ooooo.infra.config.nacos;


import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;

import javax.annotation.PostConstruct;
import java.util.Properties;

import static com.alibaba.nacos.api.PropertyKeyConst.*;
import static com.ooooo.infra.config.nacos.NacosServerProperties.OOOOO_CONFIG;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Setter
@Getter
public class NacosServerProperties implements EnvironmentAware {

  public static final String OOOOO_CONFIG = "ooooo.config.nacos";

  @Setter
  public Environment environment;

  private String namespace;

  private String serverAddr;

  private String username;

  private String password;

  private Properties properties = new Properties();

  private boolean enabled;

  public Properties getProperties() {
    properties.put(SERVER_ADDR, serverAddr);
    properties.put(NAMESPACE, namespace);
    if (username != null) {
      properties.put(USERNAME, username);
    }
    if (password != null) {
      properties.put(PASSWORD, password);
    }
    return properties;
  }

  @PostConstruct
  public void init() {
    if (!isEnabled()) {
      return;
    }

    if (serverAddr == null) {
      serverAddr = environment.getProperty("spring.cloud.nacos.config.server-addr", environment.getProperty("spring.cloud.nacos.server-addr"));
    }
    if (StrUtil.isBlank(serverAddr)) {
      throw new IllegalArgumentException("property key['spring.cloud.nacos.server-addr'] are not configured");
    }

    if (namespace == null) {
      namespace = environment.getProperty("spring.cloud.nacos.config.namespace");
    }
    if (StrUtil.isBlank(namespace)) {
      throw new IllegalArgumentException("property key['spring.cloud.nacos.config.namespace'] are not configured");
    }

    if (username == null) {
      username = environment.getProperty("spring.cloud.nacos.config.username", environment.getProperty("spring.cloud.nacos.username"));
    }
    if (password == null) {
      password = environment.getProperty("spring.cloud.nacos.config.password", environment.getProperty("spring.cloud.nacos.password"));
    }
  }


}
