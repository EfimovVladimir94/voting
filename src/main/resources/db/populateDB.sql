DELETE FROM votes;
DELETE FROM dishes;
DELETE FROM restaurants;
DELETE FROM menu;
DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100000;

INSERT INTO users (name, email, password) VALUES
('User', 'user@yandex.ru', '{noop}password'),
('Admin', 'admin@gmail.com', '{noop}admin');

INSERT INTO user_roles (role, user_id) VALUES
('ROLE_USER', 100000),
('ROLE_ADMIN', 100001),
('ROLE_USER', 100001);

INSERT INTO restaurants (name) VALUES
('Нихром'),
('Makiaveli'),
('Кампай'),
('Хан Сарай');

INSERT INTO menu (date, restaurant_id) VALUES
('2019-07-11', 100002),
('2019-07-11', 100003),
(TODAY(), 100002),
(TODAY(), 100003);

INSERT INTO dishes (name, price, menu_id) VALUES
('Блины', 6000, 100006),
('Салат', 5000, 100007),
('Суп', 6000, 100008),
('Пицца', 4200, 100009);

INSERT INTO votes (date, menu_id, user_id) VALUES
('2019-07-11', 100006, 100000)