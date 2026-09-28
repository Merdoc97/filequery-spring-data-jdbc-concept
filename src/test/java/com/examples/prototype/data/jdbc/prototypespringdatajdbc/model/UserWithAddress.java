package com.examples.prototype.data.jdbc.prototypespringdatajdbc.model;

import lombok.Builder;
import lombok.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Table(name = "users")
@Value
@Builder
public class UserWithAddress {
    @Id
    private Long id;

    @Column("user_name")
    private String userName;

    private String email;

    @MappedCollection(idColumn = "user_id",keyColumn = "user_id")
    private List<Address> addresses;
}
