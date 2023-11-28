package com.ooooo.framework.auth.config;

import com.ooooo.framework.auth.endpoint.*;
import com.ooooo.framework.auth.helper.JwtHelper;
import com.ooooo.framework.auth.helper.SM4Helper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.security.servlet.SecurityFilterAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 * <a href="https://docs.spring.io/spring-security/reference/servlet/architecture.html">spring security doc</a>
 *
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity(securedEnabled = true)
@EnableConfigurationProperties(WebSecurityProperties.class)
@AutoConfigureAfter(SecurityFilterAutoConfiguration.class)
public class WebSecurityAutoConfiguration extends WebSecurityConfigurerAdapter {

  @Autowired
  private WebSecurityProperties webSecurityProperties;

  @Override
  public void configure(WebSecurity web) throws Exception {
    web.ignoring()
        .mvcMatchers("/authless/**")
        .mvcMatchers(AuthorizeEndpoint.TOKEN_URL)
        .and()
    ;

    for (String ignoreUrl : webSecurityProperties.getIgnoreUrls()) {
      web.ignoring().mvcMatchers(ignoreUrl);
    }
  }

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http.authorizeRequests().anyRequest().authenticated().and()
        .httpBasic().disable()
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
        .csrf().disable()
        .cors(Customizer.withDefaults())
        .anonymous().disable()
        .addFilterBefore(authenticateFilter(), UsernamePasswordAuthenticationFilter.class)
        .exceptionHandling()
        .authenticationEntryPoint(invalidTokenAuthenticationEntryPoint())
        .accessDeniedHandler(noPermissonAccessDeniedHandler()).and()
    ;
  }

  @Override
  protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    // override, then config
    auth.inMemoryAuthentication();
  }

  @Bean
  public AuthenticateFilter authenticateFilter() {
    return new AuthenticateFilter();
  }

  @Bean
  @ConditionalOnMissingBean
  public NoPermissionAccessDeniedHandler noPermissonAccessDeniedHandler() {
    return new NoPermissionAccessDeniedHandler();
  }

  @Bean
  @ConditionalOnMissingBean
  public InvalidTokenAuthenticationEntryPoint invalidTokenAuthenticationEntryPoint() {
    return new InvalidTokenAuthenticationEntryPoint();
  }

  @Bean
  @Override
  public AuthenticationManager authenticationManagerBean() throws Exception {
    return super.authenticationManagerBean();
  }

  @Bean
  @ConditionalOnMissingBean
  public SecurityServices securityServices() {
    return new SecurityServices();
  }

  @Bean
  @ConditionalOnMissingBean
  public SM4Helper sm4Helper() {
    SM4Properties sm4 = webSecurityProperties.getSm4();
    return new SM4Helper(sm4.getMode(), sm4.getPadding(), sm4.getKey());
  }

  @Bean
  @ConditionalOnMissingBean
  public JwtHelper jwtTokenHelper() {
    return new JwtHelper();
  }
}
