# infra

Infrastructure to set up spring boot application

## 模块说明

- infra-dev

1. 提供开发环境的统一配置，可以在**多个项目**之间使用**单一配置文件**。

- infra-auth

1. 提供统一的认证接口 `/authless/token`
2. 业务系统只需要实现 `AbstractSecurityService` 就可以完成认证和授权，示例类 `UsernamePasswordSecurityService`

- infra-cache

1. `spring cache` 存在的问题，`redis` 请求出错之后，无法继续查询数据库
2. 提供 `CacheService` 操作缓存
3. 提供 `@Cache` 注解

- infra-context

1. 提供 `Result` 类作为响应结果体
2. 提供 `WebServerInfo` 可以获取 `applicationName` 和 `port`
3. 提供 `ExpressionParser` 表达解析器

- infra-test

1. 提供 mock `dubbo` 接口, 包括 `provider` 和 `consumer`。

- infra-config

1. 提供 `ConfigDataUrlListener` 监听配置