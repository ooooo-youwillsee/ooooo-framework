package com.ooooo.framework.auth.endpoint;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @see AuthenticateFilter
 * @since 1.0.0
 */
@Getter
@Setter
public class AccessTokenAuthentication implements Authentication {

  public static final String PRINCIPAL = "principal";
  public static final String DETAILS = "details";
  public static final String AUTHORITIES = "authorities";
  public static final String NAME = "name";
  public static final String UNIQUE_ID = "uniqueId";
  public static final String EXPIRE_AT = "expireAt";

  /**
   * 作为用户的唯一标识, string 类型
   */
  private Object principal;

  /**
   * 用户其他信息，map 类型
   */
  private Object details;

  private Collection<? extends GrantedAuthority> authorities;

  private String name;

  private boolean authenticated;

  /**
   * 为空
   */
  private Object credentials;

  // ===========extension properties===============

  private Date expiresAt;

  public void setExpiresAt(Date expiresAt) {
    if (!(expiresAt instanceof Date)) {
      throw new IllegalArgumentException("expiresAt must be Date");
    }
    this.expiresAt = expiresAt;
    this.authenticated = expiresAt.after(new Date());
  }

  public void setAuthenticated(boolean authenticated) {
    throw new IllegalArgumentException("you can't set the field, you should set field named 'expiresAt'");
  }

  public void setPrincipal(Object principal) {
    if (principal == null) {
      return;
    }
    if (!(principal instanceof String)) {
      throw new IllegalArgumentException("principal must be String");
    }
    this.principal = principal;
  }

  public void setDetails(Object details) {
    if (details == null) {
      return;
    }
    if (!(details instanceof Map)) {
      throw new IllegalArgumentException("details must be Map");
    }
    this.details = details;
  }

  public static String[] getAuthorities(Collection<? extends GrantedAuthority> authorities) {
    if (authorities == null || authorities.isEmpty()) {
      return new String[0];
    }
    return authorities.stream().map(GrantedAuthority::getAuthority).toArray(String[]::new);
  }

  public static Collection<? extends GrantedAuthority> getAuthorities(List<String> authorities) {
    if (authorities == null || authorities.isEmpty()) {
      return Collections.emptyList();
    }
    return authorities.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toList());
  }

}
