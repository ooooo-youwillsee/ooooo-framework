package com.ooooo.infra.config.support;

import com.ooooo.infra.config.ConfigDataUrlListener;
import com.ooooo.infra.config.nacos.NacosConfigDataUrlListenRegister;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

import java.util.Map;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class ConfigDataUrlListenerScanner implements ApplicationListener<ContextRefreshedEvent> {

  @Autowired(required = false)
  @Qualifier(ConfigDataAutoConfiguration.NACOS)
  private NacosConfigDataUrlListenRegister nacosConfigDataUrlListenRegister;

  @Override
  public void onApplicationEvent(ContextRefreshedEvent event) {
    ApplicationContext applicationContext = event.getApplicationContext();
    Map<String, ConfigDataUrlListener> beans = applicationContext.getBeansOfType(ConfigDataUrlListener.class);
    // register all listener
    for (Map.Entry<String, ConfigDataUrlListener> entry : beans.entrySet()) {
      ConfigDataUrlListener listener = entry.getValue();
      log.info("found ConfigDataUrlListener, name: '{}', url: '{}'", listener.getClass(), listener.getUrl());
      ConfigDataUrlType type = getType(listener.getUrl());
      //noinspection SwitchStatementWithTooFewBranches
      switch (type) {
        case NACOS:
          registerNacos(listener);
          break;
      }
    }
  }

  protected void registerNacos(ConfigDataUrlListener listener) {
    if (nacosConfigDataUrlListenRegister != null) {
      nacosConfigDataUrlListenRegister.register(listener);
    }
  }

  protected ConfigDataUrlType getType(String url) {
    for (ConfigDataUrlType value : ConfigDataUrlType.values()) {
      if (value.match(url)) {
        return value;
      }
    }
    throw new IllegalArgumentException("url '" + url + "' is error");
  }

}
