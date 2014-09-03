package com.free.misc;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

public class SpringBeanPostProcessor implements BeanPostProcessor {

  @Override
  public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
    return bean;
  }

  @Override
  public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
    if (bean instanceof MappingJacksonHttpMessageConverter) {
      ObjectMapper om = new ObjectMapper();
      om.getSerializationConfig().disable(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS);
      om.getSerializationConfig().disable(SerializationConfig.Feature.WRITE_NULL_MAP_VALUES);
      om.getSerializationConfig().setSerializationInclusion(Inclusion.NON_NULL);
      om.getDeserializationConfig().set(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
      ((MappingJacksonHttpMessageConverter) bean).setObjectMapper(om);
    }
    return bean;
  }

}
