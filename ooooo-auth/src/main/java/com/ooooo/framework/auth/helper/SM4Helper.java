package com.ooooo.framework.auth.helper;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.Mode;
import cn.hutool.crypto.Padding;
import cn.hutool.crypto.symmetric.SM4;

import java.nio.charset.StandardCharsets;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class SM4Helper {

  private final SM4 sm4;

  public SM4Helper(String mode, String padding, String key) {
    this.sm4 = new SM4(Mode.valueOf(mode), Padding.valueOf(padding), key.getBytes(StandardCharsets.UTF_8));
  }

  public String decode(String value) {
    byte[] decode = Base64.decode(value);
    return sm4.decryptStr(decode);
  }

  public String encode(String value) {
    byte[] encrypt = sm4.encrypt(value);
    return Base64.encode(encrypt);
  }
}
