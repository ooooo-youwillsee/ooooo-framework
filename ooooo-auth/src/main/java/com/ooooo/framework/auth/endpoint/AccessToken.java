package com.ooooo.framework.auth.endpoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

  public static final AccessToken INVALID_ACCESS_TOKEN = new AccessToken(null, new Date());

  private String token;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  private Date expireAt;
}