CREATE TABLE IF NOT EXISTS payment_type (
                                            id      BIGSERIAL       PRIMARY KEY,
                                            type    VARCHAR(255),
                                            amount  DECIMAL(19, 2)
);

CREATE TABLE IF NOT EXISTS oktmo (
                                     id      BIGSERIAL       PRIMARY KEY,
                                     code    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS payer (
                                     id      BIGSERIAL       PRIMARY KEY,
                                     name    VARCHAR(255),
                                     inn     BIGINT
);

CREATE TABLE IF NOT EXISTS payee (
                                     id      BIGSERIAL       PRIMARY KEY,
                                     name    VARCHAR(255),
                                     inn     BIGINT
);

CREATE TABLE IF NOT EXISTS users (
                                     id          BIGSERIAL     PRIMARY KEY,
                                     email       VARCHAR(255),
                                     username    VARCHAR(255),
                                     password    VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS user_roles (
                                          user_id     BIGINT         NOT NULL REFERENCES users(id) ON DELETE CASCADE,
                                          role        VARCHAR(255)   NOT NULL
);

CREATE TABLE IF NOT EXISTS payment_document (
                                                id                  BIGSERIAL     PRIMARY KEY,
                                                payer_inn           BIGINT,
                                                payee_inn           BIGINT,
                                                organization_oktmo  VARCHAR(255),
                                                amount              DECIMAL(19, 2),
                                                date_of_payment     DATE,
                                                payment_type        VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS payment (
                                       id                  BIGSERIAL      PRIMARY KEY,
                                       payment_type_id     BIGINT         REFERENCES payment_type,
                                       oktmo_id            BIGINT         REFERENCES oktmo,
                                       payer_id            BIGINT         REFERENCES payer,
                                       payee_id            BIGINT         REFERENCES payee,
                                       for_self            BOOLEAN,
                                       status              VARCHAR(255),
                                       payment_document    BIGINT         REFERENCES payment_document,
                                       user_id             BIGINT         NOT NULL REFERENCES users
);

CREATE TABLE IF NOT EXISTS inn_repository (
    id      BIGSERIAL     PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS shedlock (
                                        name        VARCHAR(64)   NOT NULL,
                                        lock_until  TIMESTAMP     NOT NULL,
                                        locked_at   TIMESTAMP     NOT NULL,
                                        locked_by   VARCHAR(255)  NOT NULL,
                                        PRIMARY KEY (name)
);