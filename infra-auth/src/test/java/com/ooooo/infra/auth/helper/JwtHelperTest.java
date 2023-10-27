package com.ooooo.infra.auth.helper;

import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
class JwtHelperTest {

  @Test
  void test() {
    JwtHelper jwtHelper = new JwtHelper();

    Map<String, Object> header = new HashMap<>();
    header.put("111", "222");

    Map<String, Object> payload = new HashMap<>();
    payload.put("username", "123");

    String token = jwtHelper.signToken(header, payload);
    log.info("token: \n{}", token);

    DecodedJWT jwtToken = jwtHelper.verifyToken(token);

    assertEquals("222", jwtToken.getHeaderClaim("111").asString());
    assertEquals("123", jwtToken.getClaim("username").asString());
  }

}