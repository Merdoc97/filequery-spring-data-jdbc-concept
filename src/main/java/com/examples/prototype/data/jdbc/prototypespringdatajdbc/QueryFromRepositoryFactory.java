package com.examples.prototype.data.jdbc.prototypespringdatajdbc;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.query.AbstractJdbcQuery;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ValueExpressionDelegate;

import java.util.Optional;

public class QueryFromRepositoryFactory extends JdbcRepositoryFactory {

    private final ApplicationContext context;
    private final JdbcAggregateOperations operations;
    private  BeanFactory beanFactory;
    private QueryMappingConfiguration queryMappingConfiguration;

    public QueryFromRepositoryFactory(JdbcAggregateOperations operations, ApplicationContext context) {
        super(operations);
        this.context = context;
        this.operations = operations;
        this.queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key, ValueExpressionDelegate valueExpressionDelegate) {
        AbstractJdbcQuery.RowMapperFactory rowMapperFactory = (AbstractJdbcQuery.RowMapperFactory)(this.beanFactory != null ? new BeanFactoryAwareRowMapperFactory(this.beanFactory, this.operations, this.queryMappingConfiguration) : new DefaultRowMapperFactory(this.operations, this.queryMappingConfiguration));
        Optional<QueryLookupStrategy> original = super.getQueryLookupStrategy(key,valueExpressionDelegate);
        return Optional.of(new QueryFromLookupStrategy(
                original,
                context.getBean(org.springframework.data.mapping.context.MappingContext.class),
                context.getBean(org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations.class),
                rowMapperFactory,
                context.getBean(org.springframework.data.jdbc.core.convert.JdbcConverter.class),
                valueExpressionDelegate));

    }
}