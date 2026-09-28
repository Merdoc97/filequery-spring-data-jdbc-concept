package com.examples.prototype.data.jdbc.prototypespringdatajdbc.factory;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.data.jdbc.core.JdbcAggregateOperations;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;

public class BeanFactoryAwareRowMapperFactory extends DefaultRowMapperFactory {

    private final BeanFactory beanFactory;

    /**
     * Create a {@code BeanFactoryAwareRowMapperFactory} instance using the given {@link BeanFactory},
     * {@link JdbcAggregateOperations} and {@link QueryMappingConfiguration}.
     *
     * @param beanFactory
     * @param operations
     * @param queryMappingConfiguration
     */
    public BeanFactoryAwareRowMapperFactory(BeanFactory beanFactory, JdbcAggregateOperations operations,
                                            QueryMappingConfiguration queryMappingConfiguration) {

        super(operations, queryMappingConfiguration);

        this.beanFactory = beanFactory;
    }

    @Override
    public RowMapper<Object> getRowMapper(String reference) {
        return beanFactory.getBean(reference, RowMapper.class);
    }

    @Override
    public ResultSetExtractor<Object> getResultSetExtractor(String reference) {
        return beanFactory.getBean(reference, ResultSetExtractor.class);
    }

}
