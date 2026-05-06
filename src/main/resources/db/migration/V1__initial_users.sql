CREATE TABLE USERS (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       user_name VARCHAR(255) NOT NULL
);

INSERT INTO USERS (user_name)
VALUES ('Ivanov Ivan');
INSERT INTO USERS (user_name)
VALUES ('Petrov Petr');
INSERT INTO USERS (user_name)
VALUES ('Semenov Semen');
INSERT INTO USERS (user_name)
VALUES ('Sidorov Ivan');
INSERT INTO USERS (user_name)
VALUES ('Petrov Semen');