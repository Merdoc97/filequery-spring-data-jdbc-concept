package com.examples.prototype.data.jdbc.prototypespringdatajdbc.configuration;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.QueryFromRepositoryFactoryBean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories;

@Configuration
@EnableJdbcRepositories(
        repositoryFactoryBeanClass = QueryFromRepositoryFactoryBean.class,
        basePackages = "com.examples.prototype.data.jdbc")
public class QueryFileConfiguration {
}
