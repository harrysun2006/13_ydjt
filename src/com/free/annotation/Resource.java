package com.free.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.free.ResourceType;

@Target({ ElementType.TYPE, ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface Resource {

  ResourceType type() default ResourceType.GENERAL;

  String id() default "";

  String code() default "";

  String name() default "";

  String description() default "";

}
