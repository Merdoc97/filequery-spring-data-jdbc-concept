package com.examples.prototype.data.jdbc.prototypespringdatajdbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "users")
@Data
public class User {
    @Id
    private Long id;
    @Column("user_name")
    private String userName;
    private String email;
}
