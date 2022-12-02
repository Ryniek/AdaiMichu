--liquibase formatted sql
--changeset mrynski:insertTestUsers

INSERT INTO users (id, name, email, password, last_date_of_drawing_task, notification_send, assigned_user_id)
VALUES (1, 'user1', null, '$2a$10$NNY/mikKCDTlZMjDvAVds.qEnCwpSlW.LX3PlXPmdg5GQ3xC9Tpju', null, false, 2),
(2, 'user2', null, '$2a$10$NNY/mikKCDTlZMjDvAVds.qEnCwpSlW.LX3PlXPmdg5GQ3xC9Tpju', null, false, 1);