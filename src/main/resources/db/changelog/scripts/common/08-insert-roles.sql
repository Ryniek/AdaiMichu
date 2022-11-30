--liquibase formatted sql
--changeset mrynski:insertRoles

INSERT INTO user_role (id, name)
VALUES (1, 'ROLE_USER'),
(2, 'ROLE_ADMIN');

ALTER TABLE users MODIFY assigned_user_id BIGINT NULL;

INSERT INTO users (id, name, email, password, last_date_of_drawing_task, notification_send, assigned_user_id)
VALUES (3, 'admin', null, '$2a$10$fjhn5UsqI.0Mh6ckYhsbyeM8i2rPquiwOxOM3UzHP0sGzPZIBGtX6', null, false, null);

INSERT INTO users_roles (user_id, role_id)
VALUES (1, 1),
(2, 1),
(3, 2);