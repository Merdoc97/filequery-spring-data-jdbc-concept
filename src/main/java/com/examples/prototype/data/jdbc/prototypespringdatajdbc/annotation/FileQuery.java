package com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation;

import jakarta.annotation.Resource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface FileQuery {

    @Resource
    String file() default "";
}

