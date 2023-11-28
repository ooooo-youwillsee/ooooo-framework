package com.ooooo.framework.auth.endpoint;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.lang.Pair;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.ooooo.framework.auth.helper.JwtHelper;
import com.ooooo.framework.auth.helper.SM4Helper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public abstract class AbstractSecurityService implements SecurityService {

  public static final String SM4 = "SM4";

  public static final String BASE64 = "BASE64";

  public static final String JWT = "JWT";

  public static final String NONE = "NONE";

  @Autowired
  private SM4Helper sm4Helper;

  @Autowired
  private JwtHelper jwtHelper;

  @Override
  public AccessToken authorize(HttpServletRequest request) {
    AccessTokenAuthentication authentication = auth(request);
    if (authentication == null) {
      throw new IllegalArgumentException("authentication is null");
    }
    // Jwt token
    String accessToken = generateAccessToken(authentication);
    // SM4 encode
    accessToken = sm4Helper.encode("{" + JWT + "}" + accessToken);
    // build accessToken
    return new AccessToken(accessToken, authentication.getExpiresAt());
  }

  protected abstract AccessTokenAuthentication auth(HttpServletRequest request);

  protected String generateAccessToken(AccessTokenAuthentication authentication) {
    Map<String, Object> payload = new HashMap<>();
    if (authentication.getPrincipal() != null) {
      payload.put(AccessTokenAuthentication.PRINCIPAL, authentication.getPrincipal());
    }
    if (authentication.getDetails() != null) {
      payload.put(AccessTokenAuthentication.DETAILS, authentication.getDetails());
    }
    if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
      payload.put(AccessTokenAuthentication.AUTHORITIES, AccessTokenAuthentication.getAuthorities(authentication.getAuthorities()));
    }
    if (authentication.getName() != null) {
      payload.put(AccessTokenAuthentication.NAME, authentication.getName());
    }
    if (authentication.getExpiresAt() != null) {
      payload.put(AccessTokenAuthentication.EXPIRE_AT, authentication.getExpiresAt());
    } else {
      payload.put(AccessTokenAuthentication.EXPIRE_AT, new Date());
    }
    // jwt token encode
    return jwtHelper.signToken(null, payload);
  }


  @Override
  public Authentication authenticate(HttpServletRequest request) {
    String accessToken = getAccessToken(request);
    if (accessToken == null) {
      return null;
    }
    try {
      return generateAuthentication(accessToken);
    } catch (Throwable e) {
      log.error("authenticate error.", e);
    }
    return null;
  }

  protected Authentication generateAuthentication(String accessToken) {
    Pair<String, String> prefixedAccessToken = prefixedAccessToken(accessToken);
    String prefix = prefixedAccessToken.getKey();
    accessToken = prefixedAccessToken.getValue();
    switch (prefix) {
      case SM4:
        accessToken = sm4Helper.decode(prefixedAccessToken.getValue());
        return generateAuthentication(accessToken);
      case BASE64:
        accessToken = Base64.decodeStr(accessToken, StandardCharsets.UTF_8);
        return generateAuthentication(accessToken);
      case JWT:
        DecodedJWT decodedJWT = jwtHelper.verifyToken(accessToken);
        // build AccessTokenAuthentication
        AccessTokenAuthentication authentication = new AccessTokenAuthentication();
        authentication.setPrincipal(decodedJWT.getClaim(AccessTokenAuthentication.PRINCIPAL).asString());
        authentication.setDetails(decodedJWT.getClaim(AccessTokenAuthentication.DETAILS).asMap());
        authentication.setAuthorities(AccessTokenAuthentication.getAuthorities(decodedJWT.getClaim(AccessTokenAuthentication.AUTHORITIES).asList(String.class)));
        authentication.setName(decodedJWT.getClaim(AccessTokenAuthentication.NAME).asString());
        authentication.setExpiresAt(decodedJWT.getClaim(AccessTokenAuthentication.EXPIRE_AT).asDate());
        return authentication;
      default:
        return generateAuthentication("{" + SM4 + "}" + accessToken);
    }
  }

  /**
   * parse prefix, eg: accessToken: "{json}xxxxx"
   */
  protected Pair<String, String> prefixedAccessToken(String accessToken) {
    String prefix = NONE;
    int lIndex = accessToken.indexOf("{"), rIndex = accessToken.indexOf("}");
    if (lIndex == 0 && rIndex > lIndex) {
      prefix = accessToken.substring(lIndex + 1, rIndex);
      accessToken = accessToken.substring(rIndex + 1);
    }
    return Pair.of(prefix, accessToken);
  }

  protected String getAccessToken(HttpServletRequest request) {
    return request.getHeader(HttpHeaders.AUTHORIZATION);
  }

}
