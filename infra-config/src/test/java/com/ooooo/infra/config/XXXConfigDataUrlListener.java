package com.ooooo.infra.config;

import lombok.Data;
import lombok.Getter;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Getter
@Component
public class XXXConfigDataUrlListener implements ConfigDataUrlListener {

  public static final String DATA_ID = "xxx.yaml";

  public static final String GROUP = "demo";

  private String newValue;

  @Override
  public String getUrl() {
    return String.format("nacos://%s?group=%s", DATA_ID, GROUP);
  }

  @Override
  public void configChanged(ConfigData data) {
    newValue = data.getNewValue();
  }
}
