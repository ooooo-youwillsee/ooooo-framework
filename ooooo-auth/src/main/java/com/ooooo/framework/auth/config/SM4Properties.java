package com.ooooo.framework.auth.config;

import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import lombok.Getter;
import lombok.Setter;

/**
 * @see cn.hutool.crypto.symmetric.SM4
 */
@Setter
@Getter
public class SM4Properties {

  /**
   * @see cn.hutool.crypto.Mode
   */
  private String mode = Mode.ECB.name();

  /**
   * @see cn.hutool.crypto.Padding
   */
  private String padding = Padding.ZeroPadding.name();

  private String key = StrUtil.repeat('o', 16);
}