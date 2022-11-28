--liquibase formatted sql
--changeset mrynski:createPasswordResetToken

create table password_reset_token (
    id bigint not null AUTO_INCREMENT,
    token varchar(255) not null unique,
    expiration_date timestamp not null,
    user_id bigint not null,
    primary key (id),
    foreign key(user_id) references users(id)
);
