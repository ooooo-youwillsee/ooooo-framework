package com.ooooo.framework.cache;

import com.ooooo.framework.cache.annotation.Cache;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
public class DemoService {

  @Getter
  @Setter
  public int count = 0;

  @Cache(name = "test1")
  public String test1(int i) {
    count++;
    return "value" + i;
  }

  @Cache(name = "test2", condition = "#i == 0")
  public String test2(int i) {
    count++;
    return "value" + i;
  }

}
