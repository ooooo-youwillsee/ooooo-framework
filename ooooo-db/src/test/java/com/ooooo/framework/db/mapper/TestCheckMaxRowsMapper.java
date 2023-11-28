package com.ooooo.framework.db.mapper;

import com.ooooo.framework.db.annotation.DisableCheckMaxRows;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Mapper
public interface TestCheckMaxRowsMapper {

  @Select("select '1' from dual")
  List<String> selectList1();

  @Select("select '1' from dual")
  @DisableCheckMaxRows
  List<String> selectList2();
}
