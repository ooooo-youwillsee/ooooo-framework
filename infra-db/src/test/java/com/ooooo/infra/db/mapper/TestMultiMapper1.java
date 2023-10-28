package com.ooooo.infra.db.mapper;

import com.ooooo.infra.db.annotation.MultiDataSource;
import com.ooooo.infra.db.datasource.MultiDataSourceContextHolder;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Mapper
@MultiDataSource(name = "test")
public interface TestMultiMapper1 {

  @Select("select 'test' from dual")
  default String test() {
    return MultiDataSourceContextHolder.getRoutingKey();
  }

  @Select("select 'test1' from dual")
  @MultiDataSource(name = "test2")
  default String test2() {
    return MultiDataSourceContextHolder.getRoutingKey();
  }


}
