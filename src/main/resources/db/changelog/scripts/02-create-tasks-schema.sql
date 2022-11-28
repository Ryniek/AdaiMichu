--liquibase formatted sql
--changeset mrynski:createTasks

create table tasks (
    id bigint not null,
    name varchar(255) not null,
    comment varchar(255),
    is_finished boolean not null,
    is_started boolean not null,
    is_hidden boolean not null,
    creation_date timestamp not null,
    days_to_use bigint not null,
    expiration_date timestamp,
    finish_date timestamp,
    creator_id bigint,
    drawn_user_id bigint,
    primary key (id),
    foreign key(creator_id) references users(id),
    foreign key(drawn_user_id) references users(id)
);