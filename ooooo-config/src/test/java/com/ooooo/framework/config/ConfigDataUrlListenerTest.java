package com.ooooo.framework.config;

import com.alibaba.nacos.api.config.ConfigService;
import com.ooooo.framework.config.nacos.NacosConfigDataUrlListenRegister;
import com.ooooo.framework.config.nacos.NacosServerProperties;
import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = ConfigTestConfiguration.class)
class ConfigDataUrlListenerTest {

  @Autowired
  private NacosConfigDataUrlListenRegister nacosConfigDataUrlListenRegister;

  @Autowired
  private NacosServerProperties nacosServerProperties;

  @Autowired
  private XXXConfigDataUrlListener xxxConfigDataUrlListener;

  @Test
  @SneakyThrows
  void testNacos() {
    ConfigService configService = nacosConfigDataUrlListenRegister.getConfigService(nacosServerProperties.getProperties());
    // publish new value
    configService.publishConfig(XXXConfigDataUrlListener.DATA_ID, XXXConfigDataUrlListener.GROUP, "123");
    TimeUnit.SECONDS.sleep(3);
    assertThat(xxxConfigDataUrlListener.getNewValue()).isEqualTo("123");

    // publish new value
    configService.publishConfig(XXXConfigDataUrlListener.DATA_ID, XXXConfigDataUrlListener.GROUP, "456");
    TimeUnit.SECONDS.sleep(3);
    assertThat(xxxConfigDataUrlListener.getNewValue()).isEqualTo("456");
  }


}