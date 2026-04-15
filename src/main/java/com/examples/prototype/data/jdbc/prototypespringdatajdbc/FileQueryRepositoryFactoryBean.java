package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;

import java.io.Serializable;

public class FileQueryRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends JdbcRepositoryFactoryBean<T, S, ID>
        implements ApplicationContextAware {
    private ApplicationContext applicationContext;

    public FileQueryRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        JdbcAggregateOperations operations = applicationContext.getBean(JdbcAggregateOperations.class);
        var factory = new FileQueryRepositoryFactory(operations, applicationContext);
        return factory;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}