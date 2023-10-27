package com.ooooo.infra.cache.aspect;

import com.ooooo.infra.cache.CacheService;
import com.ooooo.infra.cache.CacheTestConfiguration;
import com.ooooo.infra.cache.DemoService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = CacheTestConfiguration.class)
class CacheAspectTest {

  @Autowired
  private DemoService demoService;

  @Autowired
  private CacheService cacheService;

  @BeforeEach
  void beforeEach() {
    cacheService.delete("test1");
    cacheService.delete("test2");
  }

  @Test
  void test1() {
    demoService.setCount(0);
    String value = demoService.test1();
    assertThat(value).isEqualTo("value1");
    value = demoService.test1();
    assertThat(value).isEqualTo("value1");
  }

  @Test
  void test2() {
    // condition is true, so it will cache value
    demoService.setCount(0);
    String value = demoService.test2(0);
    assertThat(value).isEqualTo("value1");
    value = demoService.test2(0);
    assertThat(value).isEqualTo("value1");

    // condition is false, so it will not cache value
    demoService.setCount(0);
    value = demoService.test2(1);
    assertThat(value).isEqualTo("value1");
    value = demoService.test2(2);
    assertThat(value).isEqualTo("value2");
  }


}