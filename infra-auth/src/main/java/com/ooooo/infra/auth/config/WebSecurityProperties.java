package com.ooooo.infra.auth.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Setter
@Getter
@ConfigurationProperties(prefix = WebSecurityProperties.CPE_AUTH)
public class WebSecurityProperties {

  public static final String CPE_AUTH = "cpe.auth";

  private List<String> ignoreUrls = new ArrayList<>();
}
