package com.ooooo.infra.test.dubbo;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.common.utils.ArrayUtils;
import org.apache.dubbo.config.annotation.DubboReference;
import org.apache.dubbo.config.spring.beans.factory.annotation.ReferenceAnnotationBeanPostProcessor;
import org.springframework.beans.BeansException;
import org.springframework.beans.PropertyValues;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.InstantiationAwareBeanPostProcessor;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.core.Ordered;
import org.springframework.core.PriorityOrdered;
import org.springframework.stereotype.Component;
import org.springframework.util.ReflectionUtils;

@Slf4j
@Component
@ConditionalOnClass(DubboReference.class)
public class MockDubboReferenceAnnotationBeanDefinitionRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor, PriorityOrdered {

  /**
   * @param registry the bean definition registry used by the application context
   * @throws BeansException
   */
  @Override
  public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
    // use @DubboReference as @autowired
    RootBeanDefinition beanDefinition = new RootBeanDefinition(MockReferenceAnnotationBeanPostProcessor.class);
    beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    registry.registerBeanDefinition(ReferenceAnnotationBeanPostProcessor.BEAN_NAME, beanDefinition);
  }

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {

  }

  @Override
  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  private static class MockReferenceAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, ApplicationContextAware {

    @Setter
    private ApplicationContext applicationContext;

    @Override
    public PropertyValues postProcessProperties(PropertyValues pvs, Object bean, String beanName) throws BeansException {
      ReflectionUtils.doWithFields(bean.getClass(),
                                   f -> {
                                     ReflectionUtils.makeAccessible(f);
                                     f.set(bean, calcValue(f.getType()));
                                   },
                                   ff -> ff.getAnnotation(DubboReference.class) != null);
      return null;
    }

    private Object calcValue(Class<?> type) {
      String[] names = applicationContext.getBeanNamesForType(type);
      if (ArrayUtils.isEmpty(names)) {
        return null;
      }

      if (names.length > 1) {
        throw new IllegalStateException("There is more than one bean in @DubboReference.");
      }

      return applicationContext.getBean(names[0]);
    }
  }
}
