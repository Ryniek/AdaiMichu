--liquibase formatted sql
--changeset mrynski:createUsers

create table users (
    id bigint not null,
    name varchar(255) not null unique,
    email varchar(255) unique,
    password varchar(255) not null,
    last_date_of_drawing_task timestamp,
    notification_send boolean not null,
    assigned_user_id bigint not null,
    primary key (id)
);
