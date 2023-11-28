package com.ooooo.framework.config;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public interface ConfigDataUrlListener {

  /**
   * example: nacos://xxx.yaml?group=demo
   */
  String getUrl();

  void configChanged(ConfigData data);
}
