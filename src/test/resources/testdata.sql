INSERT INTO users (name, email, password, last_date_of_drawing_task, notification_send, assigned_user_id)
VALUES ('ada', 'michalrynski96@gmail.com', '$2a$10$fjhn5UsqI.0Mh6ckYhsbyeM8i2rPquiwOxOM3UzHP0sGzPZIBGtX6', '2022-11-08 10:17:01', false, 2),
('michu', 'test', '$2a$10$Pn.EJc1JRQ64N5MxQsiu4uYbsSsj9evMwLKbgWW.nkt7Mfa2XknXe', '2022-11-09 21:12:14', false, 1);

INSERT INTO tasks (id, name, comment, is_finished, is_started, is_hidden, creation_date, days_to_use, expiration_date, finish_date, creator_id, drawn_user_id)
VALUES (1, 'Ścieranie kurzów w całym domu', 'TYLKO DOKŁADNIE!!!', false, false, false, '2022-11-09 15:15:01', 14, null, null, 1, null),
(2, 'Używanie cały dzień lewej ręki', null, false, false, false, '2022-11-09 15:17:01', 12, null, null, 1, null),
(3, 'Śniadanie do łóżka', null, false, false, false, '2022-11-10 11:04:01', 10, null, null, 1, null),
(4, 'Spacer z Pulserem', null, true, false, true, '2022-11-08 21:42:55', 8, '2022-11-16 21:42:55', '2022-11-13 12:42:14', 1, 1),
(5, 'Wybierasz film do oglądania', 'Tylko nie Wladce Pierscieni', false, true, false, '2022-11-10 22:31:55', 14, '2022-11-24 22:31:55', null, 1, 2),
(6, 'Wspólne robienie rogalików', 'Wspólne ale bardziej TY', false, true, false, '2022-11-09 11:11:55', 14, '2022-11-23 11:11:55', null, 1, 1),
(7, 'Trzy godziny bez IQOSA', 'W obecnosci partnera i ciągiem', true, false, false, '2022-11-10 10:01:40', 7, '2022-11-17 10:01:40', '2022-11-14 10:01:40', 1, 2);

INSERT INTO password_reset_token(id, token, user_id, expiration_date)
VALUES (1, 'token', '1', '2022-11-16 21:42:55');