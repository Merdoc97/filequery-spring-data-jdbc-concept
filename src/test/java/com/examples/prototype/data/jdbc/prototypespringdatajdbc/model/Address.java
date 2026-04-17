package com.examples.prototype.data.jdbc.prototypespringdatajdbc.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "addresses")
@Data
public class Address {

    @Id
    private Long id;
    private String street;
    private String city;
    private String state;
    private String zip;
    private String country;
    @Column("user_id")
    private Long userId;

}