package com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation.QueryFrom;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.UserWithAddress;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserWithAddressRepository extends CrudRepository<UserWithAddress, Long> {

    @QueryFrom("sql/findById.sql")
    Optional<UserWithAddress> getById(@Param("userId") Long id);
}
