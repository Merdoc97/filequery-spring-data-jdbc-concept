create table users
(
    id        serial primary key not null,
    user_name varchar(50)        not null,
    email     varchar(50)        not null

);
insert into users(user_name, email) VALUES ('user1','user1@email.com');
insert into users(user_name, email) VALUES ('user2','user2@email.com');