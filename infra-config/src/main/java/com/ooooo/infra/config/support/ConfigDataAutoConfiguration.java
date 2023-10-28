package com.ooooo.infra.config.support;

import com.alibaba.nacos.client.config.NacosConfigService;
import com.ooooo.infra.config.nacos.NacosConfigDataUrlListenRegister;
import com.ooooo.infra.config.nacos.NacosServerProperties;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.ooooo.infra.config.nacos.NacosServerProperties.OOOOO_CONFIG;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
public class ConfigDataAutoConfiguration {

  public static final String NACOS = "nacos://";

  @Bean
  public ConfigDataUrlListenerScanner configDataUrlListenerRegister() {
    return new ConfigDataUrlListenerScanner();
  }


  @ConditionalOnClass(NacosConfigService.class)
  static class Nacos {

    @Bean
    @ConditionalOnClass
    @ConfigurationProperties(prefix = OOOOO_CONFIG)
    public NacosServerProperties nacosServerProperties() {
      return new NacosServerProperties();
    }

    @Bean(name = NACOS)
    @ConditionalOnMissingBean(name = NACOS)
    public NacosConfigDataUrlListenRegister nacosConfigDataUrlListenRegister() {
      return new NacosConfigDataUrlListenRegister();
    }

  }
}
