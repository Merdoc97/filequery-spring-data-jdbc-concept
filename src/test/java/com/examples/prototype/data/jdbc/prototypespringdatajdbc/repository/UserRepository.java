package com.examples.prototype.data.jdbc.prototypespringdatajdbc.repository;

import com.examples.prototype.data.jdbc.prototypespringdatajdbc.annotation.FileQuery;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.User;
import com.examples.prototype.data.jdbc.prototypespringdatajdbc.model.UserProjection;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional(readOnly = true)
public interface UserRepository extends CrudRepository<User, Long> {

    @FileQuery(file = "sql/find-all.sql")
    List<User> getAll();

    @FileQuery(file = "sql/find-all_only_emails.sql")
    List<UserProjection> projectionTest();

    @FileQuery(file = "sql/find-by-email.sql")
    List<User> findByTest(@Param("email")String email);

    List<User>findAllByEmail(String email);

    @Query("select * from users where email=:email")
    List<User>findAllByEmailQuery(@Param("email") String email);


}
