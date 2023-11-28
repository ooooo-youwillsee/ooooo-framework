package com.ooooo.framework.db.config;

import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.core.handlers.MetaObjectHandler;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.InnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.ooooo.framework.db.datasource.MultiDataSourceAdvisor;
import com.ooooo.framework.db.datasource.MultiDataSourceProperties;
import com.ooooo.framework.db.datasource.MultiRoutingDataSource;
import com.ooooo.framework.db.mybatis.CheckMaxRowsInterceptor;
import com.ooooo.framework.db.mybatis.TimeMetaObjectHandler;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Role;
import org.springframework.data.redis.connection.RedisConnectionFactory;

import javax.sql.DataSource;
import java.util.List;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Configuration
@EnableConfigurationProperties(DBProperties.class)
@ConditionalOnClass(MybatisPlusAutoConfiguration.class)
public class DBAutoConfiguration {

  @Autowired
  private DBProperties dbProperties;

  @Bean
  @ConditionalOnMissingBean
  public MetaObjectHandler timeMetaObjectHandler() {
    return new TimeMetaObjectHandler();
  }

  @Bean
  public MybatisPlusInterceptor mybatisPlusInterceptor(List<InnerInterceptor> innerInterceptors) {
    MybatisPlusInterceptor mybatisPlusInterceptor = new MybatisPlusInterceptor();
    mybatisPlusInterceptor.setInterceptors(innerInterceptors);
    return mybatisPlusInterceptor;
  }

  @Bean
  public PaginationInnerInterceptor paginationInterceptor() {
    return new PaginationInnerInterceptor();
  }

  @Bean
  public CheckMaxRowsInterceptor checkMaxRowsInterceptor() {
    return new CheckMaxRowsInterceptor(dbProperties);
  }


  @Configuration
  @ConditionalOnClass({RedisConnectionFactory.class, Redisson.class})
  @ConditionalOnBean(RedissonClient.class)
  static class Redis {

//    @Bean
//    @ConditionalOnMissingBean
//    public IdentifierGenerator idGenerator() {
//      return new RedisIdentifierGenerator();
//    }

  }

  @Configuration
  @EnableConfigurationProperties(MultiDataSourceProperties.class)
  @ConditionalOnProperty(prefix = DBProperties.OOOOO_DB, name = DBProperties.ENABLE_MULTI_DATA_SOURCE,
      havingValue = "true", matchIfMissing = true)
  static class Multi {

    @Autowired
    private MultiDataSourceProperties dataSourceProperties;

    @Bean
    @ConditionalOnMissingBean
    public DataSource dataSource() {
      return new MultiRoutingDataSource(dataSourceProperties);
    }

    @Bean
    @Role(RootBeanDefinition.ROLE_INFRASTRUCTURE)
    @ConditionalOnMissingBean
    public MultiDataSourceAdvisor multiDataSourceAdvisor() {
      return new MultiDataSourceAdvisor();
    }
  }
}
