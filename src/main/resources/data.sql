INSERT INTO user (id, name, email, password)
VALUES (1, 'ada', null, '$2a$10$fjhn5UsqI.0Mh6ckYhsbyeM8i2rPquiwOxOM3UzHP0sGzPZIBGtX6'),
(2, 'michu', null, '$2a$10$Pn.EJc1JRQ64N5MxQsiu4uYbsSsj9evMwLKbgWW.nkt7Mfa2XknXe');

INSERT INTO task (id, name, comment, is_done, is_hidden, creation_date, expiration_date, done_date, creator_id)
VALUES (1, 'Ścieranie kurzów w całym domu', 'TYLKO DOKŁADNIE!!!', false, false, '2022-11-09 15:15:01', null, null, 1),
(2, 'Używanie cały dzień lewej ręki', null, false, false, '2022-11-09 15:17:01', null, null, 1);
