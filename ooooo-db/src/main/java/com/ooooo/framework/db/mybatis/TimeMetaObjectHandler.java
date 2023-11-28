package com.ooooo.framework.db.mybatis;

import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import org.apache.ibatis.reflection.MetaObject;

import java.util.Date;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class TimeMetaObjectHandler implements MetaObjectHandler {

  @Override
  public void insertFill(MetaObject metaObject) {
    Date currentTime = new Date();
    this.strictInsertFill(metaObject, "create_time", Date.class, currentTime);
    this.strictInsertFill(metaObject, "update_time", Date.class, currentTime);
  }

  @Override
  public void updateFill(MetaObject metaObject) {
    Date currentTime = new Date();
    this.strictInsertFill(metaObject, "update_time", Date.class, currentTime);
  }
}
