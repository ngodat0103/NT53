create table address
(
    id               uuid default uuid_generate_v4() not null
        primary key,
    created_at       timestamp(6)                    not null,
    created_by       varchar(255)                    not null,
    last_modified_at timestamp(6),
    last_modified_by varchar(255),
    address_line1    varchar(255),
    address_line2    varchar(255),
    city             varchar(100),
    postal_code      varchar(20),
    region           varchar(100),
    street_number    varchar(10),
    unit_number      varchar(10),
    country_id       uuid
        constraint fke54x81nmccsk5569hsjg1a6ka
            references country
);

alter table address
    owner to "user-svc-user";

create table country
(
    id               uuid default uuid_generate_v4() not null
        primary key,
    created_at       timestamp(6)                    not null,
    created_by       varchar(255)                    not null,
    last_modified_at timestamp(6),
    last_modified_by varchar(255),
    country_name     varchar(100)
);

alter table country
    owner to "user-svc-user";

create table site_user
(
    id               uuid default uuid_generate_v4() not null
        primary key,
    created_at       timestamp(6)                    not null,
    created_by       varchar(255)                    not null,
    last_modified_at timestamp(6),
    last_modified_by varchar(255),
    email_address    varchar(255),
    first_name       varchar(100),
    last_name        varchar(100),
    phone_number     varchar(20)
);

alter table site_user
    owner to "user-svc-user";

create table user_address
(
    created_at       timestamp(6) not null,
    created_by       varchar(255) not null,
    last_modified_at timestamp(6),
    last_modified_by varchar(255),
    is_default       boolean,
    address_id       uuid         not null
        constraint fkdaaxogn1ss81gkcsdn05wi6jp
            references address,
    user_id          uuid         not null
        constraint fkb94vp5jhhr5d7xc93emqv2gko
            references site_user,
    primary key (address_id, user_id)
);

alter table user_address
    owner to "user-svc-user";

