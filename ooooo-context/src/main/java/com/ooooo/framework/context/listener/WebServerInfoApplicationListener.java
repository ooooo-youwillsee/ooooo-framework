package com.ooooo.framework.context.listener;

import com.ooooo.framework.context.WebServerInfo;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.web.context.WebServerApplicationContext;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationListener;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
public class WebServerInfoApplicationListener implements ApplicationListener<WebServerInitializedEvent> {

  @Override
  public void onApplicationEvent(WebServerInitializedEvent event) {
    WebServerApplicationContext applicationContext = event.getApplicationContext();
    ConfigurableListableBeanFactory beanFactory = null;
    if (applicationContext.getAutowireCapableBeanFactory() instanceof ConfigurableListableBeanFactory) {
      beanFactory = (ConfigurableListableBeanFactory) applicationContext.getAutowireCapableBeanFactory();
    }
    if (beanFactory == null) {
      return;
    }

    String beanDefinitionName = WebServerInfo.class.getName();
    if (!beanFactory.containsBean(beanDefinitionName)) {
      WebServerInfo webServerInfo = new WebServerInfo();
      webServerInfo.setApplicationName(applicationContext.getApplicationName());
      webServerInfo.setPort(event.getWebServer().getPort());
      beanFactory.registerSingleton(beanDefinitionName, webServerInfo);
    }
  }

}
