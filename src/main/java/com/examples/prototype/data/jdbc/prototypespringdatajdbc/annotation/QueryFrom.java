package com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation;

import jakarta.annotation.Resource;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;

import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface QueryFrom {

    @Resource
    String value() default "";

    Class<? extends RowMapper> rowMapperClass() default RowMapper.class;

    String rowMapperRef() default "";

    Class<? extends ResultSetExtractor> resultSetExtractorClass() default ResultSetExtractor.class;

    String resultSetExtractorRef() default "";
}

