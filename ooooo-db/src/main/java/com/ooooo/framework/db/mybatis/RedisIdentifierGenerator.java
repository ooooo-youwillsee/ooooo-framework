package com.ooooo.framework.db.mybatis;

import com.baomidou.mybatisplus.core.incrementer.IdentifierGenerator;
import org.redisson.Redisson;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class RedisIdentifierGenerator implements IdentifierGenerator {

  @Override
  public Number nextId(Object entity) {
    return null;
  }

}
