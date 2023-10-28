package com.ooooo.infra.db.mapper;

import com.ooooo.infra.db.annotation.MultiDataSource;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Mapper
@MultiDataSource(name = "test-test")
public interface TestMultiMapper2 extends TestMultiMapper1 {


}
