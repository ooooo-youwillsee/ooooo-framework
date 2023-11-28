package com.ooooo.framework.cache;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = CacheTestConfiguration.class)
class RedisCacheServiceTest {

  @Autowired
  private RedisCacheService cacheService;

  @Test
  void test() {
    cacheService.set("abc", "123");
    String value = cacheService.get("abc", String.class);
    assertThat(value).isEqualTo("123");
  }
}