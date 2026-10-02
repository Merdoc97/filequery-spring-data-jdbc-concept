create table users
(
    id        bigint primary key not null,
    user_name varchar(50)         not null,
    email     varchar(50)         not null

);
CREATE SEQUENCE user_seq
    INCREMENT 1
    START 1;

insert into users(id, user_name, email)
VALUES (nextval('user_seq'), 'user1', 'user1@email.com');
insert into users(id, user_name, email)
VALUES (nextval('user_seq'), 'user2', 'user2@email.com');

create table addresses
(
    id      bigint primary key not null,
    street  varchar(255)       not null,
    city    varchar(255)       not null,
    state   varchar(255)       not null,
    zip     varchar(255)       not null,
    country varchar(255)       not null,
    user_id bigint references users (id)
);
CREATE SEQUENCE addresses_seq
    INCREMENT 1
    START 10;
insert into addresses(id,street, city, state, zip, country, user_id)
values (1,'test_street', 'test_city', 'state', 'zip', 'country', 2)
