package com.free.misc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class SpringBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

  @Override
  public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
    // ApplicationContextAwareProcessor a;
    // ServletContextAwareProcessor b;
    // CommonAnnotationBeanPostProcessor c;
    // AutowiredAnnotationBeanPostProcessor d;
    // factory.registerCustomEditor(requiredType, propertyEditorClass)
    String[] bns = factory.getBeanDefinitionNames();
    for (String bn : bns)
      System.out.println(bn);
  }

}
