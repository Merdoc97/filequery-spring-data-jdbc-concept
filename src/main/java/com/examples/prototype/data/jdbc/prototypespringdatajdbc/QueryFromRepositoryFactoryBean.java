package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.convert.*;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactoryBean;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.core.support.RepositoryFactorySupport;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;
import org.springframework.util.Assert;

import java.io.Serializable;

public class QueryFromRepositoryFactoryBean<T extends Repository<S, ID>, S, ID extends Serializable>
        extends JdbcRepositoryFactoryBean<T, S, ID>
        implements ApplicationContextAware {
    private ApplicationContext applicationContext;
    private QueryMappingConfiguration queryMappingConfiguration;
    private ApplicationEventPublisher publisher;
    private BeanFactory beanFactory;
    private RelationalMappingContext mappingContext;
    private JdbcConverter converter;
    private DataAccessStrategy dataAccessStrategy;
    private NamedParameterJdbcOperations operations;
    private EntityCallbacks entityCallbacks;
    private Dialect dialect;

    public QueryFromRepositoryFactoryBean(Class<? extends T> repositoryInterface) {
        super(repositoryInterface);
    }

    @Override
    protected RepositoryFactorySupport doCreateRepositoryFactory() {
        RelationalMappingContext relationalMappingContext = applicationContext.getBean(RelationalMappingContext.class);
        QueryFromRepositoryFactory factory = new QueryFromRepositoryFactory(dataAccessStrategy, relationalMappingContext, converter,
                dialect, operations, applicationContext, beanFactory, entityCallbacks);
        factory.setQueryMappingConfiguration(queryMappingConfiguration);
        factory.setBeanFactory(beanFactory);
        factory.setEntityCallbacks(entityCallbacks);
        return factory;
    }

    public void setMappingContext(RelationalMappingContext mappingContext) {
        Assert.notNull(mappingContext, "MappingContext must not be null");
        super.setMappingContext(mappingContext);
        this.mappingContext = mappingContext;
    }

    public void setDialect(Dialect dialect) {
        Assert.notNull(dialect, "Dialect must not be null");
        super.setDialect(dialect);
        this.dialect = dialect;
    }


    public void setDataAccessStrategy(DataAccessStrategy dataAccessStrategy) {
        Assert.notNull(dataAccessStrategy, "DataAccessStrategy must not be null");
        super.setDataAccessStrategy(dataAccessStrategy);
        this.dataAccessStrategy = dataAccessStrategy;
    }

    @Autowired(required = false)
    public void setQueryMappingConfiguration(QueryMappingConfiguration queryMappingConfiguration) {
        Assert.notNull(queryMappingConfiguration, "QueryMappingConfiguration must not be null");
        super.setQueryMappingConfiguration(queryMappingConfiguration);
        this.queryMappingConfiguration = queryMappingConfiguration;
    }

    public void setJdbcOperations(NamedParameterJdbcOperations operations) {
        Assert.notNull(operations, "NamedParameterJdbcOperations must not be null");
        super.setJdbcOperations(operations);
        this.operations = operations;
    }

    public void setConverter(JdbcConverter converter) {
        Assert.notNull(converter, "JdbcConverter must not be null");
        super.setConverter(converter);
        this.converter = converter;
    }

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        super.setBeanFactory(beanFactory);
        this.beanFactory = beanFactory;
    }

    @Override
    public void afterPropertiesSet() {
        Assert.state(this.mappingContext != null, "MappingContext is required and must not be null");
        Assert.state(this.converter != null, "RelationalConverter is required and must not be null");
        this.operations = applicationContext.getBean(NamedParameterJdbcOperations.class);
        this.converter = applicationContext.getBean(JdbcConverter.class);
        this.dialect = applicationContext.getBean(Dialect.class);
        this.dataAccessStrategy = applicationContext.getBean(DataAccessStrategy.class);

        if (this.operations == null) {

            Assert.state(beanFactory != null, "If no JdbcOperations are set a BeanFactory must be available");

            this.operations = beanFactory.getBean(NamedParameterJdbcOperations.class);
        }

        if (this.dataAccessStrategy == null) {

            Assert.state(beanFactory != null, "If no DataAccessStrategy is set a BeanFactory must be available");

            this.dataAccessStrategy = this.beanFactory.getBeanProvider(DataAccessStrategy.class) //
                    .getIfAvailable(() -> {

                        Assert.state(this.dialect != null, "Dialect is required and must not be null");

                        SqlGeneratorSource sqlGeneratorSource = new SqlGeneratorSource(this.mappingContext, this.converter,
                                this.dialect);
                        SqlParametersFactory sqlParametersFactory = new SqlParametersFactory(this.mappingContext, this.converter);
                        InsertStrategyFactory insertStrategyFactory = new InsertStrategyFactory(this.operations, this.dialect);

                        DataAccessStrategyFactory factory = new DataAccessStrategyFactory(sqlGeneratorSource, this.converter,
                                this.operations, sqlParametersFactory, insertStrategyFactory);

                        return factory.create();
                    });
        }

        if (this.queryMappingConfiguration == null) {
            this.queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
        }

        if (beanFactory != null) {
            entityCallbacks = EntityCallbacks.create(beanFactory);
        }
        super.setConverter(this.converter);
        super.setDialect(this.dialect);
        super.afterPropertiesSet();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }


}