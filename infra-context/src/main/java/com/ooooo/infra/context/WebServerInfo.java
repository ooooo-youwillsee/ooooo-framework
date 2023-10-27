package com.ooooo.infra.context;

import lombok.Getter;
import lombok.Setter;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Setter
@Getter
public class WebServerInfo {

  private String applicationName;

  private int port;
}
