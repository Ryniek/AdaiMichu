--liquibase formatted sql
--changeset mrynski:insertUsers

INSERT INTO users (id, name, email, password, last_date_of_drawing_task, notification_send, assigned_user_id)
VALUES (1, 'ada', 'michalrynski96@gmail.com', '$2a$10$fjhn5UsqI.0Mh6ckYhsbyeM8i2rPquiwOxOM3UzHP0sGzPZIBGtX6', '2022-11-08 10:17:01', false, 2),
(2, 'michson', null, '$2a$10$Pn.EJc1JRQ64N5MxQsiu4uYbsSsj9evMwLKbgWW.nkt7Mfa2XknXe', '2022-11-09 21:12:14', false, 1);