--liquibase formatted sql
--changeset mrynski:createRole

create table user_role (
    id bigint not null AUTO_INCREMENT,
    name varchar(255) not null UNIQUE,
	primary key (id)
);

create table users_roles (
	user_id bigint not null,
	role_id bigint not null,
	primary key (user_id, role_id),
	foreign key(user_id) references users(id),
    foreign key(role_id) references user_role(id)
);