INSERT INTO user (id, name, email, password)
VALUES (1, 'ada', null, '$2a$10$fjhn5UsqI.0Mh6ckYhsbyeM8i2rPquiwOxOM3UzHP0sGzPZIBGtX6'),
(2, 'michu', null, '$2a$10$Pn.EJc1JRQ64N5MxQsiu4uYbsSsj9evMwLKbgWW.nkt7Mfa2XknXe');

INSERT INTO task (id, name, comment, is_finished, is_started, is_hidden, creation_date, expiration_date, finish_date, creator_id)
VALUES (1, 'Ścieranie kurzów w całym domu', 'TYLKO DOKŁADNIE!!!', false, false, false, '2022-11-09 15:15:01', null, null, 1),
(2, 'Używanie cały dzień lewej ręki', null, false, false, false, '2022-11-09 15:17:01', null, null, 1),
(3, 'Śniadanie do łóżka', null, false, false, false, '2022-11-10 11:04:01', null, null, 1),
(4, 'Spacer z Pulserem', null, true, true, false, '2022-11-08 21:42:55', '2022-11-16 21:42:55', '2022-11-13 12:42:14', 1),
(5, 'Wybierasz film do oglądania', 'Tylko nie Wladce Pierscieni', true, true, false, '2022-11-10 22:31:55', '2022-11-24 22:31:55', '2022-11-10 23:48:55', 1),
(6, 'Wspólne robienie rogalików', 'Wspólne ale bardziej TY', false, true, false, '2022-11-09 11:11:55', '2022-11-23 11:11:55', null, 1),
(7, 'Trzy godziny bez IQOSA', 'W obecnosci partnera i ciągiem', false, true, false, '2022-11-10 10:01:40', '2022-11-17 10:01:40', null, 1),
(8, '10 minut masażu', null, false, false, true, '2022-11-09 06:47:25', null, null, 1);
