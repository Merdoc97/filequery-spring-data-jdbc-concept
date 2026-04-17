create table users
(
    id        serial primary key not null,
    user_name varchar(50)        not null,
    email     varchar(50)        not null

);
insert into users(user_name, email)
VALUES ('user1', 'user1@email.com');
insert into users(id, user_name, email)
VALUES (2, 'user2', 'user2@email.com');

create table addresses
(
    id      serial primary key not null,
    street  varchar(255)       not null,
    city    varchar(255)       not null,
    state   varchar(255)       not null,
    zip     varchar(255)       not null,
    country varchar(255)       not null,
    user_id bigint references users (id)
);

insert into addresses(street, city, state, zip, country, user_id)
values ('test_street', 'test_city', 'state', 'zip', 'country', 2)