package com.ooooo.infra.auth.service.impl;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.ooooo.infra.auth.endpoint.AccessToken;
import com.ooooo.infra.auth.endpoint.AccessTokenAuthentication;
import com.ooooo.infra.auth.service.SecurityService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;


/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class DefaultSecurityService implements SecurityService {

  @Override
  public AccessToken resolve(Authentication authentication) {
    if (!(authentication instanceof AccessTokenAuthentication)) {
      return AccessToken.INVALID_ACCESS_TOKEN;
    }
    AccessTokenAuthentication accessTokenAuthentication = (AccessTokenAuthentication) authentication;

    // payload
    Map<String, Object> payload = new HashMap<>();
    payload.put(PRINCIPAL, accessTokenAuthentication.getPrincipal());
    payload.put(DETAILS, accessTokenAuthentication.getDetails());
    payload.put(AUTHORITIES, getAuthorities(accessTokenAuthentication.getAuthorities()));
    payload.put(NAME, accessTokenAuthentication.getName());
    payload.put(UNIQUE_ID, accessTokenAuthentication.getUniqueId());
    payload.put(EXPIRES_AT, accessTokenAuthentication.getExpiresAt());

    // jwt token encode
    String accessToken = JwtTokenUtil.signToken(null, payload);
    // SM4 encode
    accessToken = SM4Util.encode("{jwt}" + accessToken);
    // build accessToken
    return new AccessToken(accessToken, accessTokenAuthentication.getExpiresAt());
  }

  @Override
  public Authentication resolve(String accessToken) {
    if (StringUtils.isBlank(accessToken)) {
      return null;
    }

    AccessTokenAuthentication authentication = getAuthentication(accessToken);
    return authentication;
  }

  private AccessTokenAuthentication getAuthentication(String accessToken) {
    // parse prcefix, eg: accessToken: "{json}xxxxx"
    String prefix = "NONE";
    int lIndex = accessToken.indexOf("{"), rIndex = accessToken.indexOf("}");
    if (lIndex == 0 && rIndex > lIndex) {
      prefix = accessToken.substring(lIndex + 1, rIndex);
      accessToken = accessToken.substring(rIndex + 1);
    }

    switch (prefix) {
      case "base64":
        accessToken = new String(Base64.getDecoder().decode(accessToken), StandardCharsets.UTF_8);
        return getAuthentication(accessToken);
      case "json":
        return JSON.parseObject(accessToken, AccessTokenAuthentication.class);
      case "sm4":
        // SM4 decode
        String jwtToken = SM4Util.decode(accessToken);
        return getAuthentication(jwtToken);
      case "jwt":
        // JWT decode
        DecodedJWT decodedJWT = JwtTokenUtil.verifyToken(accessToken);
        // build AccessTokenAuthentication
        AccessTokenAuthentication authentication = new AccessTokenAuthentication();
        authentication.setPrincipal(decodedJWT.getClaim(PRINCIPAL).asMap());
        authentication.setDetails(decodedJWT.getClaim(DETAILS).asMap());
        authentication.setAuthorities(getAuthorities(decodedJWT.getClaim(AUTHORITIES).asList(String.class)));
        authentication.setName(decodedJWT.getClaim(NAME).asString());
        authentication.setUniqueId(decodedJWT.getClaim(UNIQUE_ID).asString());
        authentication.setExpiresAt(decodedJWT.getClaim(EXPIRES_AT).asDate());
        return authentication;
      default:
        return getAuthentication("{sm4}" + accessToken);
    }
  }


}
