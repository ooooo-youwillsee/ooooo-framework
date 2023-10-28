package com.ooooo.infra.config.nacos;

import cn.hutool.core.util.StrUtil;
import com.alibaba.nacos.api.NacosFactory;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.ooooo.infra.config.ConfigData;
import com.ooooo.infra.config.ConfigDataUrlListener;
import com.ooooo.infra.config.DefaultConfigData;
import com.ooooo.infra.config.support.ConfigDataUrlListenerRegister;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Slf4j
public class NacosConfigDataUrlListenRegister implements ConfigDataUrlListenerRegister {

  private final Map<Properties, ConfigService> nacosConfigServices = new ConcurrentHashMap<>();

  @Autowired(required = false)
  private NacosServerProperties nacosServerProperties;

  public ConfigService getConfigService(Properties properties) {
    return nacosConfigServices.computeIfAbsent(properties, __ -> {
      try {
        return NacosFactory.createConfigService(properties);
      } catch (NacosException e) {
        throw new RuntimeException(e);
      }
    });
  }

  public void register(ConfigDataUrlListener listener) {
    if (nacosServerProperties == null || !nacosServerProperties.isEnabled()) {
      log.info("nacos config data is disabled, so not register listener, name: '{}', url: '{}'", listener.getClass(), listener.getUrl());
      return;
    }

    ConfigService configService = getConfigService(nacosServerProperties.getProperties());
    String dataId = parseDataId(listener.getUrl());
    String group = parseGroup(listener.getUrl());
    try {
      ConfigAdapter configAdapter = new ConfigAdapter(listener);
      configService.addListener(dataId, group, configAdapter);
      // at lease notify once
      String config = configService.getConfig(dataId, group, 3000);
      configAdapter.receiveConfigInfo(config);
    } catch (NacosException e) {
      throw new RuntimeException(e);
    }
  }

  /**
   * input:
   * &nbsp;nacos://xxx.yaml?group=demo
   * <br/>
   * output:
   * &nbsp;xxx.yaml
   */
  private String parseDataId(String url) {
    String dataId = StrUtil.subAfter(url, "nacos://", false);
    return StrUtil.subBefore(dataId, "?", false);
  }

  /**
   * input:
   * &nbsp;nacos://xxx.yaml?group=demo
   * <br/>
   * output:
   * &nbsp;demo
   */
  private String parseGroup(String url) {
    String group = StrUtil.subAfter(url, "group=", false);
    return group;
  }

  @AllArgsConstructor
  private static class ConfigAdapter implements Listener {

    private ConfigDataUrlListener listener;

    @Override
    public Executor getExecutor() {
      return null;
    }

    @Override
    public void receiveConfigInfo(String configInfo) {
      DefaultConfigData configData = new DefaultConfigData();
      configData.setNewValue(configInfo);
      if (log.isTraceEnabled()) {
        log.trace("configChanged, url: '{}'\n newValue: \n{}", listener.getUrl(), configInfo);
      }
      listener.configChanged(configData);
    }
  }


}
