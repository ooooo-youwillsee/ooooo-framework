package com.ooooo.framework.cache.aspect;

import com.ooooo.framework.cache.CacheService;
import com.ooooo.framework.cache.CacheTestConfiguration;
import com.ooooo.framework.cache.DemoService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.assertj.core.api.Assertions.assertThat;

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
    cacheService.delete("test1:0");
    cacheService.delete("test1:1");
    cacheService.delete("test2:0");
    cacheService.delete("test2:1");
  }

  @Test
  void test1() {
    // i = 0
    demoService.setCount(0);
    String value = demoService.test1(0);
    assertThat(value).isEqualTo("value0");
    assertThat(demoService.getCount()).isEqualTo(1);
    value = demoService.test1(0);
    assertThat(value).isEqualTo("value0");
    assertThat(demoService.getCount()).isEqualTo(1);

    // i = 1
    value = demoService.test1(1);
    assertThat(demoService.getCount()).isEqualTo(2);
    assertThat(value).isEqualTo("value1");
    value = demoService.test1(1);
    assertThat(value).isEqualTo("value1");
    assertThat(demoService.getCount()).isEqualTo(2);
  }

  @Test
  void test2() {
    // condition is true, so it will cache value
    demoService.setCount(0);
    String value = demoService.test2(0);
    assertThat(value).isEqualTo("value0");
    assertThat(demoService.getCount()).isEqualTo(1);
    value = demoService.test2(0);
    assertThat(value).isEqualTo("value0");
    assertThat(demoService.getCount()).isEqualTo(1);

    // condition is false, so it will not cache value
    value = demoService.test2(1);
    assertThat(value).isEqualTo("value1");
    assertThat(demoService.getCount()).isEqualTo(2);
    value = demoService.test2(2);
    assertThat(value).isEqualTo("value2");
    assertThat(demoService.getCount()).isEqualTo(3);
  }


}