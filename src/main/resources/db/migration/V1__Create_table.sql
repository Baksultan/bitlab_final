CREATE TABLE t_courses (
                           id bigserial NOT NULL,
                           description TEXT,
                           name varchar(255),
                           price integer,
                           PRIMARY KEY (id)
);

CREATE TABLE t_permission (
                              id bigserial NOT NULL,
                              role varchar(255),
                              PRIMARY KEY (id)
);

CREATE TABLE t_users (
                         id bigserial NOT NULL,
                         email varchar(255),
                         full_name varchar(255),
                         password varchar(255),
                         PRIMARY KEY (id)
);

CREATE TABLE t_users_permissions (
                                     user_id bigint NOT NULL,
                                     permissions_id bigint NOT NULL,
                                     FOREIGN KEY (permissions_id) REFERENCES t_permission(id),
                                     FOREIGN KEY (user_id) REFERENCES t_users(id)
);

CREATE TABLE t_users_user_courses (
                                      user_id bigint NOT NULL,
                                      user_courses_id bigint NOT NULL,
                                      FOREIGN KEY (user_courses_id) REFERENCES t_courses(id),
                                      FOREIGN KEY (user_id) REFERENCES t_users(id)
);


INSERT INTO t_permission (role)
VALUES
    ('ROLE_ADMIN'),
    ('ROLE_TEACHER'),
    ('ROLE_MANAGER');

INSERT INTO t_users (email, full_name, password)
VALUES
    ('baksultan@gmail.com', 'Baksultan Amangeldi', '$2a$12$IsgPr4QFwaL5aPyOBeaXC.F823CINInC4e5RkG8opM.eTulBe1cMe');

INSERT INTO t_users_permissions (user_id, permissions_id)
VALUES (1, 1);

INSERT INTO t_courses (id, description, name, price)
VALUES
    (1, 'Курс для обучения студентов не имеющих опыта в программировании до уровня junior java-разработчик.', 'Java Developer', 100000);

INSERT INTO t_courses (id, description, name, price)
VALUES
    (2, 'Курсы программирования на JAVA для новичков. Необходимые базовые понятия и термины, актуальные технологии.', 'Java Start', 85000);

INSERT INTO t_courses (id, description, name, price)
VALUES
    (3, 'Научим проектировать удобный и привлекательный дизайн для WEB и мобильных приложении.', 'UI/UX Design', 70000);

INSERT INTO t_courses (id, description, name, price)
VALUES
    (4, 'Мы научим Вас правильно выбирать тактику, подобрать нужную структуру данных для решения сложных алгоритмов.', 'Algorithms', 90000);