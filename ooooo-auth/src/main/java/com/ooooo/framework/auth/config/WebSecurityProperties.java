package com.ooooo.framework.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = WebSecurityProperties.OOOOO_AUTH)
public class WebSecurityProperties {

  public static final String OOOOO_AUTH = "ooooo.auth";

  private List<String> ignoreUrls = new ArrayList<>();

  @NestedConfigurationProperty
  private SM4Properties sm4 = new SM4Properties();

}
