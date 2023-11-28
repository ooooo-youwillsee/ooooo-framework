package com.ooooo.framework.db.datasource;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class MultiDataSourceContextHolder {
	
	private static final ThreadLocal<String> routingKeyThreadLocal = new ThreadLocal<>();
	
	public static void setRoutingKey(String routingKey) {
		routingKeyThreadLocal.set(routingKey);
	}
	
	public static String getRoutingKey() {
		return routingKeyThreadLocal.get();
	}
	
}
