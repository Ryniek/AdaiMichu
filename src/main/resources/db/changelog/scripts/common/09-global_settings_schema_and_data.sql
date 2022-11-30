--liquibase formatted sql
--changeset mrynski:globalSettings

create table global_settings (
    id bigint not null AUTO_INCREMENT,
    minutes_between_drawing bigint not null,
    reset_password_token_validity bigint not null,
	primary key (id)
);
INSERT INTO global_settings (id, minutes_between_drawing, reset_password_token_validity)
VALUES (1, 2880, 720);