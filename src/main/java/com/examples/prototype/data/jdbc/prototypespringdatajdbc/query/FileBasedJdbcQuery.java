package com.examples.prototype.data.jdbc.prototypespringdatajdbc.query;

import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.query.JdbcQueryMethod;

import org.springframework.data.jdbc.repository.query.StringBasedJdbcQuery;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.ValueExpressionDelegate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.lang.reflect.Method;

public class FileBasedJdbcQuery extends StringBasedJdbcQuery {

    public FileBasedJdbcQuery(
            String sql,
            Method method,
            RepositoryMetadata metadata,
            ProjectionFactory factory,
            MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> mappingContext,
            NamedParameterJdbcOperations operations,
            RowMapperFactory rowMapperFactory,
            JdbcConverter converter,
            ValueExpressionDelegate delegate,
            NamedQueries namedQueries
    ) {
        super(sql,
                new JdbcQueryMethod(method, metadata, factory, namedQueries, mappingContext),
                operations,
                rowMapperFactory,
                converter,
                delegate
        );

    }


}
