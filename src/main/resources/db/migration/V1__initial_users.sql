CREATE TABLE USERS (
                       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
                       first_name VARCHAR(50) NOT NULL,
                       last_name VARCHAR(50) NOT NULL,
                       email VARCHAR(100) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL
);
