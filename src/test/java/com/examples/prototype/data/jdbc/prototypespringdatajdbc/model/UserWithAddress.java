package com.examples.prototype.data.jdbc.prototypespringdatajdbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

import java.util.ArrayList;
import java.util.List;

@Table(name = "users")
@Data
public class UserWithAddress {
    @Id
    private Long id;

    @Column("user_name")
    private String userName;

    private String email;

    @MappedCollection(idColumn = "user_id",keyColumn = "user_id")
    private List<Address> addresses = new ArrayList<>();
}
