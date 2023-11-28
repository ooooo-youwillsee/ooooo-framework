package com.ooooo.framework.db.mybatis;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ooooo.framework.db.annotation.DisableCheckMaxRows;
import com.ooooo.framework.db.config.DBProperties;
import org.apache.ibatis.cache.CacheKey;
import org.apache.ibatis.exceptions.TooManyResultsException;
import org.apache.ibatis.executor.Executor;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.plugin.Intercepts;
import org.apache.ibatis.plugin.Invocation;
import org.apache.ibatis.plugin.Signature;
import org.apache.ibatis.session.ResultHandler;
import org.apache.ibatis.session.RowBounds;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.Collection;

@Intercepts({
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class, CacheKey.class, BoundSql.class}),
    @Signature(type = Executor.class, method = "query", args = {MappedStatement.class, Object.class, RowBounds.class, ResultHandler.class}
    )
})
public class CheckMaxRowsInterceptor implements Interceptor {

  private final DBProperties dbProperties;

  public CheckMaxRowsInterceptor(DBProperties dbProperties) {
    this.dbProperties = dbProperties;
  }

  @Override
  public Object intercept(Invocation invocation) throws Throwable {
    Object result = invocation.proceed();
    int cnt = 1;
    if (result instanceof IPage) {
      IPage<?> page = (IPage<?>) result;
      cnt = page.getRecords().size();
    } else if (result instanceof Collection) {
      Collection<?> collection = (Collection<?>) result;
      cnt = collection.size();
    }

    if (cnt > getMaxRows() && checkMaxRows(invocation)) {
      throw new TooManyResultsException(String.format("查询结果%d超过%d条，请添加查询条件", cnt, getMaxRows()));
    }
    return result;
  }

  private int getMaxRows() {
    return dbProperties.getMaxRows();
  }

  private boolean checkMaxRows(Invocation invocation) {
    String id = ((MappedStatement) invocation.getArgs()[0]).getId();
    String className = id.substring(0, id.lastIndexOf("."));
    String methodName = id.substring(id.lastIndexOf(".") + 1);
    try {
      Class<?> clazz = Class.forName(className);
      if (clazz.isAnnotationPresent(DisableCheckMaxRows.class)) {
        return false;
      }
      for (Method method : ReflectionUtils.getDeclaredMethods(clazz)) {
        if (method.getName().equals(methodName) && method.isAnnotationPresent(DisableCheckMaxRows.class)) {
          return false;
        }
      }
    } catch (ClassNotFoundException e) {
      return false;
    }

    return true;
  }

}

