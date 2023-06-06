DROP TABLE IF EXISTS operations;
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS bonus_accounts;
DROP TABLE IF EXISTS service_users;

CREATE TABLE service_users
(
    id         BIGSERIAL PRIMARY KEY ,
    email      VARCHAR(255),
    enabled    BOOLEAN      NOT NULL,
    login      VARCHAR(255) NOT NULL
        CONSTRAINT login_idx UNIQUE,
    name       VARCHAR(255),
    password   VARCHAR(255) NOT NULL,
    registered DATE         NOT NULL
);

CREATE TABLE user_roles
(
    user_id BIGSERIAL NOT NULL
        CONSTRAINT foreign_key_user_id REFERENCES service_users ON DELETE CASCADE,
    role    VARCHAR(255)
        CONSTRAINT user_roles_role_check
            CHECK ((role)::text = ANY
                   ((ARRAY ['ADMIN'::character varying,
                       'OPERATOR'::character varying,
                       'USER'::character varying])::text[])),
    CONSTRAINT users_roles_idx UNIQUE (user_id, role)
);

CREATE TABLE bonus_accounts
(
    id          BIGSERIAL PRIMARY KEY,
    bonus       NUMERIC(38, 2) NOT NULL
        CONSTRAINT bonus_accounts_bonus_check
            CHECK ((bonus >= (0)::numeric) AND (bonus <= ('9223372036854775807'::bigint)::numeric)),
    last_update TIMESTAMPTZ(0) NOT NULL,
    version     INTEGER        NOT NULL,
    user_id     BIGINT
        CONSTRAINT foreign_key_user_id REFERENCES service_users ON DELETE CASCADE
);

CREATE TABLE operations
(
    id             BIGSERIAL PRIMARY KEY,
    change         NUMERIC(38, 2) NOT NULL,
    operation_time TIMESTAMPTZ(2) NOT NULL,
    description    VARCHAR(255),
    external_id    VARCHAR(255)   NOT NULL,
    bonus_accounts BIGINT         NOT NULL
        CONSTRAINT foreign_key_bonus_accounts
            REFERENCES bonus_accounts ON DELETE CASCADE
);

CREATE INDEX moment_idx ON operations (operation_time);
