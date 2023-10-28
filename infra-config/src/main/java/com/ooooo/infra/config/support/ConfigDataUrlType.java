package com.ooooo.infra.config.support;

import cn.hutool.core.util.StrUtil;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public enum ConfigDataUrlType {

  NACOS {
    @Override
    boolean match(String url) {
      return StrUtil.startWithIgnoreCase(url, ConfigDataAutoConfiguration.NACOS);
    }
  },

  ;

  abstract boolean match(String url);

}
