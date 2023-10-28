# infra

Infrastructure to set up spring boot application

## 模块说明

- infra-dev

1. 提供开发环境的统一配置，可以在**多个项目**之间使用**单一配置文件**。

- infra-auth

1. 提供统一的认证接口 `/authless/token`
2. 业务系统只需要实现 `AbstractSecurityService` 就可以完成**认证**和**授权**，示例类 `UsernamePasswordSecurityService`

- infra-cache

1. `spring cache` 存在的问题，`redis` 请求出错之后，**无法查询**数据库
2. 提供 `CacheService` 来**操作缓存**
3. 提供 `@Cache` 来**缓存数据**

- infra-context

1. 提供 `Result` 作为**响应结果体**
2. 提供 `WebServerInfo` 支持获取 `applicationName` 和 `port`
3. 提供 `ExpressionParser` 支持**表达式解析**

- infra-test

1. 提供 `mock dubbo` 接口, 包括 `provider` 和 `consumer`

- infra-config

1. 提供 `ConfigDataUrlListener` 支持**监听配置**

- infra-db

1. 提供 `@MultiDataSource` 支持**动态数据源**
2. 提供 `CheckMaxRowsInterceptor` 检查**最大条数**, 使用 `@DisableCheckMaxRows` 来禁止检查
3. 提供 `TimeMetaObjectHandler` 自动填充 `create_time` 和 `update_time` 字段