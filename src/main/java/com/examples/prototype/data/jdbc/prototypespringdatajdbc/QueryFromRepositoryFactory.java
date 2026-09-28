package com.examples.prototype.data.jdbc.prototypespringdatajdbc;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jdbc.core.convert.DataAccessStrategy;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.query.AbstractJdbcQuery;
import org.springframework.data.jdbc.repository.support.JdbcRepositoryFactory;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.dialect.Dialect;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.ValueExpressionDelegate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.util.Optional;

public class QueryFromRepositoryFactory extends JdbcRepositoryFactory {

    private final ApplicationContext context;
    private final NamedParameterJdbcOperations operations;
    private final JdbcConverter converter;
    private final RelationalMappingContext relationalMappingContext;
    private BeanFactory beanFactory;
    private QueryMappingConfiguration queryMappingConfiguration;
    private EntityCallbacks callbacks;


    public QueryFromRepositoryFactory(DataAccessStrategy dataAccessStrategy, RelationalMappingContext relationalMappingContext,
                                      JdbcConverter converter, Dialect dialect,
                                      NamedParameterJdbcOperations operations,
                                      ApplicationContext context) {
        super(dataAccessStrategy, relationalMappingContext, converter, dialect, context, operations);
        this.context = context;
        this.operations = operations;
        this.queryMappingConfiguration = QueryMappingConfiguration.EMPTY;
        this.converter = converter;
        this.relationalMappingContext = relationalMappingContext;
    }

    @Override
    protected Optional<QueryLookupStrategy> getQueryLookupStrategy(QueryLookupStrategy.Key key, ValueExpressionDelegate valueExpressionDelegate) {
        AbstractJdbcQuery.RowMapperFactory rowMapperFactory = new BeanFactoryRowMapperFactory(this.beanFactory, this.converter,
                this.context, this.callbacks,
                this.relationalMappingContext, this.queryMappingConfiguration);
        Optional<QueryLookupStrategy> original = super.getQueryLookupStrategy(key, valueExpressionDelegate);

        return Optional.of(new QueryFromLookupStrategy(
                original,
                context.getBean(org.springframework.data.mapping.context.MappingContext.class),
                context.getBean(org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations.class),
                rowMapperFactory,
                context.getBean(org.springframework.data.jdbc.core.convert.JdbcConverter.class),
                valueExpressionDelegate));

    }


}