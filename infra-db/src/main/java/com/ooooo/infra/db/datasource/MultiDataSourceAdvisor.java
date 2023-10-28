package com.ooooo.infra.db.datasource;

import com.ooooo.infra.db.annotation.MultiDataSource;
import lombok.extern.slf4j.Slf4j;
import org.aopalliance.aop.Advice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.springframework.aop.ClassFilter;
import org.springframework.aop.MethodMatcher;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.context.annotation.Role;
import org.springframework.core.annotation.AnnotatedElementUtils;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class MultiDataSourceAdvisor extends AbstractPointcutAdvisor {

	private final MultiDataSourcePointcut pointcut = new MultiDataSourcePointcut();

	private final MultiDataSourceMethodInterceptor interceptor = new MultiDataSourceMethodInterceptor();

	@Override
	public Pointcut getPointcut() {
		return pointcut;
	}
	
	@Override
	public Advice getAdvice() {
		return interceptor;
	}
	
	private static class MultiDataSourcePointcut extends StaticMethodMatcherPointcut {
		
		@Override
		public boolean matches(Method method, Class<?> targetClass) {
			return AnnotatedElementUtils.isAnnotated(method, MultiDataSource.class)
					|| AnnotatedElementUtils.isAnnotated(targetClass, MultiDataSource.class)
					|| Arrays.stream(targetClass.getInterfaces()).anyMatch(c -> c.isAnnotationPresent(MultiDataSource.class));
		}
	}
	
	private static class MultiDataSourceMethodInterceptor implements MethodInterceptor {
		
		@Override
		public Object invoke(MethodInvocation invocation) throws Throwable {
			String oldRoutingKey = MultiDataSourceContextHolder.getRoutingKey();
			String routingKey = getRoutingKey(invocation.getMethod(), invocation.getThis());
			if (log.isTraceEnabled()) {
				log.trace("@MultiDataSource oldRoutingKey: '{}', routingKey is '{}' on method '{}'", oldRoutingKey, routingKey, invocation.getMethod().getName());
			}

			MultiDataSourceContextHolder.setRoutingKey(routingKey);
			try {
				Object proceed = invocation.proceed();
				return proceed;
			} finally {
				MultiDataSourceContextHolder.setRoutingKey(oldRoutingKey);
			}
		}
		
		private String getRoutingKey(Method method, Object obj) {
			MultiDataSource dataSource = AnnotatedElementUtils.getMergedAnnotation(method, MultiDataSource.class);
			if (dataSource != null) {
				return dataSource.name();
			}

			dataSource = AnnotatedElementUtils.getMergedAnnotation(obj.getClass(), MultiDataSource.class);
			if (dataSource != null) {
				return dataSource.name();
			}
			
			for (Class<?> clazz : obj.getClass().getInterfaces()) {
				dataSource = AnnotatedElementUtils.getMergedAnnotation(clazz, MultiDataSource.class);
				if (dataSource != null) {
					return dataSource.name();
				}
			}

			return null;
		}
	}
	
	
}
