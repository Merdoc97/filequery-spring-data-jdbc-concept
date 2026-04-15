package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import org.jspecify.annotations.Nullable;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.core.convert.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.query.RowMapperFactory;
import org.springframework.data.jdbc.repository.support.BeanFactoryAwareRowMapperFactory;
import org.springframework.data.jdbc.repository.support.DefaultRowMapperFactory;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ValueExpressionDelegate;

import java.util.Optional;

public class FileQueryRepositoryFactory extends JdbcRepositoryFactory {

    private final ApplicationContext context;
    private final JdbcAggregateOperations operations;
    private @Nullable BeanFactory beanFactory;
    private QueryMappingConfiguration queryMappingConfiguration;

    public FileQueryRepositoryFactory(JdbcAggregateOperations operations, ApplicationContext context) {
        super(operations);
        this.context = context;
        this.operations = operations;
        this.queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.@Nullable Key key, ValueExpressionDelegate valueExpressionDelegate) {
        RowMapperFactory rowMapperFactory = (RowMapperFactory)(this.beanFactory != null ? new BeanFactoryAwareRowMapperFactory(this.beanFactory, this.operations, this.queryMappingConfiguration) : new DefaultRowMapperFactory(this.operations, this.queryMappingConfiguration));
        Optional<QueryLookupStrategy> original = super.getQueryLookupStrategy(key,valueExpressionDelegate);
        return Optional.of(new FileQueryLookupStrategy(
                original,
                context.getBean(org.springframework.data.mapping.context.MappingContext.class),
                context.getBean(org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations.class),
                rowMapperFactory,
                context.getBean(org.springframework.data.jdbc.core.convert.JdbcConverter.class),
                valueExpressionDelegate,
                context));

    }
}