create table if not exists s311685.payment_type
(
    id      bigserial       primary key,
    type    varchar(255),
    amount  decimal(19, 2)
);

alter table s311685.payment_type owner to "s311685";

create table if not exists s311685.oktmo
(
    id      bigserial       primary key,
    code    varchar(255)
);

alter table s311685.oktmo owner to "s311685";


create table if not exists s311685.payer
(
    id      bigserial       primary key,
    name    varchar(255),
    inn     bigint
);

alter table s311685.payer owner to "s311685";


create table if not exists s311685.payee
(
    id      bigserial       primary key,
    name    varchar(255),
    inn     bigint
    );

alter table s311685.payee owner to "s311685";


create table if not exists s311685.users
(
    id          bigserial       primary key,
    username    varchar(255),
    password    varchar(255),
    role        varchar(255)
);

alter table s311685.users owner to "s311685";


create table if not exists s311685.payment
(
    id                  bigserial       primary key,
    payment_type_id     bigint          references s311685.payment_type,
    oktmo_id            bigint          references s311685.oktmo,
    payer_id            bigint          references s311685.payer,
    payee_id            bigint          references s311685.payee,
    for_self            boolean,
    status              varchar(255),
    payment_document    bigint          references s311685.payment_document,
    user_id             bigint          not null    references s311685.users
);


alter table s311685.payment owner to "s311685";


create table if not exists s311685.payment_document
(
    id                  bigserial       primary key,
    payer_inn           varchar(255),
    payee_inn           varchar(255),
    organization_oktmo  varchar(255),
    amount              decimal(19, 2),
    date_of_payment     date,
    payment_type        varchar(255),
    payment_id          bigint          references s311685.payment
    );

alter table s311685.payment_document owner to "s311685";


create table if not exists s311685.inn_repository
(
    id bigserial primary key
);

alter table s311685.inn_repository owner to "s311685";

drop table inn_repository, oktmo, payee, payer, payment, payment_document, payment_type, users CASCADE ;

CREATE TABLE IF NOT EXISTS shedlock(
                                       name VARCHAR(64) NOT NULL,
                                       lock_until TIMESTAMP NOT NULL,
                                       locked_at TIMESTAMP NOT NULL,
                                       locked_by VARCHAR(255) NOT NULL,
                                       PRIMARY KEY (name)
);