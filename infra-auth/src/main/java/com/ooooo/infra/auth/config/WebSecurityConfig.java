package com.ooooo.infra.auth.config;

import com.ooooo.infra.auth.endpoint.AccessTokenFilter;
import com.ooooo.infra.auth.endpoint.AuthorizationEndpoint;
import com.ooooo.infra.auth.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
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
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private WebSecurityProperties webSecurityProperties;
	
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring()
				.mvcMatchers("/authless/**")
				.mvcMatchers(AuthorizationEndpoint.TOKEN_URL)
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
		    .addFilterBefore(accessTokenFilter(), UsernamePasswordAuthenticationFilter.class)
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
	public AccessTokenFilter accessTokenFilter() {
		return new AccessTokenFilter();
	}
	
	@Bean
	@ConditionalOnMissingBean
	public SecurityService defaultSecurityService() {
		return new DefaultSecurityService();
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
	
}
