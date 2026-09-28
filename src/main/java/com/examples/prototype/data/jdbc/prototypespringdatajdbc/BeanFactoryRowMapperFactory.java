package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.jdbc.core.convert.EntityRowMapper;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.QueryMappingConfiguration;
import org.springframework.data.jdbc.repository.query.AbstractJdbcQuery;
import org.springframework.data.mapping.callback.EntityCallbacks;
import org.springframework.data.relational.core.mapping.RelationalMappingContext;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.event.AfterConvertCallback;
import org.springframework.data.relational.core.mapping.event.AfterConvertEvent;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.lang.Nullable;

import java.sql.ResultSet;
import java.sql.SQLException;

public class BeanFactoryRowMapperFactory implements AbstractJdbcQuery.RowMapperFactory {

    private final @Nullable BeanFactory beanFactory;
    private final JdbcConverter converter;
    private final ApplicationEventPublisher publisher;
    private final @Nullable EntityCallbacks callbacks;
    private final RelationalMappingContext context;
    private final QueryMappingConfiguration queryMappingConfiguration;

    BeanFactoryRowMapperFactory(@Nullable BeanFactory beanFactory,
                                JdbcConverter converter,
                                ApplicationEventPublisher publisher,
                                @Nullable EntityCallbacks callbacks,
                                RelationalMappingContext context,
                                QueryMappingConfiguration queryMappingConfiguration) {
        this.beanFactory = beanFactory;
        this.converter = converter;
        this.publisher = publisher;
        this.callbacks = callbacks;
        this.context = context;
        this.queryMappingConfiguration = queryMappingConfiguration;
    }

    @Override
    public RowMapper<Object> create(Class<?> result) {
        return createMapper(result);
    }

    @Override
    public RowMapper<Object> getRowMapper(String reference) {

        if (beanFactory == null) {
            throw new IllegalStateException(
                    "Cannot resolve RowMapper bean reference '" + reference + "'; BeanFactory is not configured.");
        }

        return beanFactory.getBean(reference, RowMapper.class);
    }

    @Override
    public ResultSetExtractor<Object> getResultSetExtractor(String reference) {

        if (beanFactory == null) {
            throw new IllegalStateException(
                    "Cannot resolve ResultSetExtractor bean reference '" + reference + "'; BeanFactory is not configured.");
        }

        return beanFactory.getBean(reference, ResultSetExtractor.class);
    }

    RowMapper<Object> createMapper(Class<?> returnedObjectType) {

        RelationalPersistentEntity<?> persistentEntity = getMappingContext().getPersistentEntity(returnedObjectType);

        if (persistentEntity == null) {
            return (RowMapper<Object>) SingleColumnRowMapper.newInstance(returnedObjectType,
                    converter.getConversionService());
        }

        return (RowMapper<Object>) determineDefaultMapper(returnedObjectType);
    }

    public RelationalMappingContext getMappingContext() {
        return context;
    }

    private RowMapper<?> determineDefaultMapper(Class<?> returnedObjectType) {

        RowMapper<?> configuredQueryMapper = queryMappingConfiguration.getRowMapper(returnedObjectType);

        if (configuredQueryMapper != null)
            return configuredQueryMapper;

        EntityRowMapper<?> defaultEntityRowMapper = new EntityRowMapper<>( //
                getMappingContext().getRequiredPersistentEntity(returnedObjectType), //
                converter //
        );

        return new PostProcessingRowMapper<>(defaultEntityRowMapper);
    }

    class PostProcessingRowMapper<T> implements RowMapper<T> {

        private final RowMapper<T> delegate;

        PostProcessingRowMapper(RowMapper<T> delegate) {
            this.delegate = delegate;
        }

        @Override
        public T mapRow(ResultSet rs, int rowNum) throws SQLException {

            T entity = delegate.mapRow(rs, rowNum);

            if (entity != null) {

                publisher.publishEvent(new AfterConvertEvent<>(entity));

                if (callbacks != null) {
                    return callbacks.callback(AfterConvertCallback.class, entity);
                }
            }

            return entity;
        }
    }
}