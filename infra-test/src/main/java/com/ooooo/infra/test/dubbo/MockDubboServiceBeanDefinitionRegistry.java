package com.ooooo.infra.test.dubbo;

import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.ClassPathBeanDefinitionScanner;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Component;

/**
 * @author <a href="https://github.com/ooooo-youwillsee">ooooo</a>
 * @since 1.0.0
 */
@Component
@ConditionalOnClass(DubboService.class)
public class MockDubboServiceBeanDefinitionRegistry implements BeanDefinitionRegistryPostProcessor {

  public static String SCAN_PACKAGES = "com";

  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    DubboServiceClassPathBeanDefinitionScanner scanner = new DubboServiceClassPathBeanDefinitionScanner(registry);
    scanner.scan(SCAN_PACKAGES);
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

  }

  private static class DubboServiceClassPathBeanDefinitionScanner extends ClassPathBeanDefinitionScanner {

    public DubboServiceClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
      super(registry, false);
      addIncludeFilter(new AnnotationTypeFilter(DubboService.class));
    }

  }
}
