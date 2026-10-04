package com.examples.prototype.data.jdbc.prototypespringdatajdbc;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation.QueryFrom;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.query.FileBasedJdbcQuery;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.io.FileSystemResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.data.jdbc.core.convert.JdbcConverter;
import org.springframework.data.jdbc.repository.query.AbstractJdbcQuery;
import org.springframework.data.mapping.context.MappingContext;
import org.springframework.data.projection.ProjectionFactory;
import org.springframework.data.relational.core.mapping.RelationalPersistentEntity;
import org.springframework.data.relational.core.mapping.RelationalPersistentProperty;
import org.springframework.data.repository.core.NamedQueries;
import org.springframework.data.repository.core.RepositoryMetadata;
import org.springframework.data.repository.query.QueryLookupStrategy;
import org.springframework.data.repository.query.RepositoryQuery;
import org.springframework.data.repository.query.ValueExpressionDelegate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcOperations;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class QueryFromLookupStrategy implements QueryLookupStrategy {

    private final Optional<QueryLookupStrategy> delegate;
    private final ResourceLoader loader = new FileSystemResourceLoader();

    private final MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> mappingContext;
    private final NamedParameterJdbcOperations operations;
    private final AbstractJdbcQuery.RowMapperFactory rowMapperFactory;
    private final JdbcConverter converter;
    private final ValueExpressionDelegate delegateExpr;
    private final Map<String, String> sqlCacheMap = new ConcurrentHashMap<>();


    public QueryFromLookupStrategy(
            Optional<QueryLookupStrategy> delegate,
            MappingContext<? extends RelationalPersistentEntity<?>, ? extends RelationalPersistentProperty> mappingContext,
            NamedParameterJdbcOperations operations,
            AbstractJdbcQuery.RowMapperFactory rowMapperFactory,
            JdbcConverter converter,
            ValueExpressionDelegate delegateExpr
    ) {
        this.delegate = delegate;
        this.mappingContext = mappingContext;
        this.operations = operations;
        this.rowMapperFactory = rowMapperFactory;
        this.converter = converter;
        this.delegateExpr = delegateExpr;
    }

    @Override
    public RepositoryQuery resolveQuery(
            Method method,
            RepositoryMetadata metadata,
            ProjectionFactory factory,
            NamedQueries namedQueries
    ) {

        QueryFrom queryFrom = AnnotatedElementUtils.findMergedAnnotation(method, QueryFrom.class);

        if (queryFrom != null) {
            String filePath = queryFrom.value();
            if (filePath.isEmpty()) {
                throw new IllegalStateException("@QueryFrom must define 'value' attribute");
            }

            String sql = sqlCacheMap.computeIfAbsent(filePath, this::loadSql);
            return new FileBasedJdbcQuery(
                    sql,
                    method,
                    metadata,
                    factory,
                    mappingContext,
                    operations,
                    rowMapperFactory,
                    converter,
                    delegateExpr,
                    namedQueries
            );
        }

        return delegate.orElseThrow(() -> new IllegalStateException("No QueryLookupStrategy available for method " + method.getName()))
                .resolveQuery(method, metadata, factory, namedQueries);
    }

    private String loadSql(String path) {
        try {
            Resource resource = loader.getResource(path.startsWith("classpath:")
                    ? path
                    : "classpath:" + path);

            return new String(resource.getInputStream().readAllBytes());
        } catch (IOException e) {
            throw new IllegalStateException("Cannot read SQL file: " + path, e);
        }
    }
}