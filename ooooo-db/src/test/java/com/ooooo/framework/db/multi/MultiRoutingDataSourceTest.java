package com.ooooo.framework.db.multi;

import com.ooooo.framework.db.mapper.TestMultiMapper2;
import com.ooooo.framework.db.DBTestConfiguration;
import com.ooooo.framework.db.mapper.TestMultiMapper1;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@SpringBootTest(classes = DBTestConfiguration.class)
class MultiRoutingDataSourceTest {

  @Autowired
  private TestService testService;

  @Autowired
  @Qualifier("testMultiMapper1")
  private TestMultiMapper1 mapper1;

  @Autowired
  @Qualifier("testMultiMapper2")
  private TestMultiMapper2 mapper2;

  @Test
  void testService() {
    String result = testService.test();
    assertEquals("test", result);

    result = testService.test2();
    assertEquals("test2", result);
  }


  @Test
  public void testMapper1() {
    String result = mapper1.test();
    assertEquals("test", result);

    result = mapper1.test2();
    assertEquals("test2", result);
  }


  @Test
  public void testMapper2() {
    String result = mapper2.test();
    assertEquals("test-test", result);

    result = mapper2.test2();
    assertEquals("test2", result);
  }

}