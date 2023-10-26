package com.ooooo.infra.auth.endpoint;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccessToken {

  public static final AccessToken INVALID_ACCESS_TOKEN = new AccessToken(null, new Date());

  private String accessToken;

  @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Shanghai")
  private Date expiresAt;
}