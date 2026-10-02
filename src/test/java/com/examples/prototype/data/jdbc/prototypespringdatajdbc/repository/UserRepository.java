package com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation.QueryFrom;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.User;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.UserProjection;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends CrudRepository<User, Long> {

    List<User> findAllByEmail(String email);

    @QueryFrom(value = "sql/find-all.sql")
    List<User> getAll();

    @QueryFrom(value = "sql/find-all_only_emails.sql")
    List<UserProjection> projectionTest();

    @QueryFrom(value = "sql/find-by-email.sql")
    List<User> findByTest(@Param("email") String email);


    @Query("select * from users where email=:email")
    List<User> findAllByEmailQuery(@Param("email") String email);

    Optional<User>findByEmail(String email);

}
