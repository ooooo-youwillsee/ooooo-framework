package com.ooooo.framework.context.listener;

import com.ooooo.framework.context.ContextTestConfiguration;
import com.ooooo.framework.context.WebServerInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = ContextTestConfiguration.class,
    webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebServerInfoApplicationListenerTest {

  @Autowired
  private WebServerInfo webServerInfo;

  @Test
  void test() {
    assertThat(webServerInfo.getPort()).isGreaterThan(0);
  }

}