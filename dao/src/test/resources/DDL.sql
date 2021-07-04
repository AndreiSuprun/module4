set MODE MYSQL;

create table if not exists certificates
(
    id bigint auto_increment
    primary key,
    name varchar(200) not null,
    description varchar(500) not null,
    price decimal(5,2) not null,
    duration int not null,
    created_on timestamp null,
    created_by varchar(50) null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    constraint certificates_name_uindex
    unique (name)
    );

create table if not exists users
(
    id bigint auto_increment
    primary key,
    email varchar(50) not null,
    created_on timestamp null,
    created_by varchar(50) null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    pwd varchar(255) null,
    password varchar(255) null,
    user_name varchar(255) not null,
    constraint users_email_uindex
    unique (email)
);

create table if not exists orders
(
    id bigint auto_increment
        primary key,
    created_by varchar(255) null,
    created_on datetime(6) null,
    updated_by varchar(255) null,
    updated_on datetime(6) null,
    total_price decimal(19,2) null,
    user_id bigint null,
    constraint FK32ql8ubntj5uh44ph9659tiih
        foreign key (user_id) references users (id)
);

create table if not exists orders_certificates
(
    created_by varchar(255) null,
    created_on datetime(6) null,
    updated_by varchar(255) null,
    updated_on datetime(6) null,
    quantity int not null,
    order_id bigint not null,
    certificate_id bigint not null,
    primary key (certificate_id, order_id),
    constraint FKnnq2g7p5kaghab94fgo150nii
        foreign key (order_id) references orders (id),
    constraint FKt3dup59ssyqcb4hbee98ucm8a
        foreign key (certificate_id) references certificates (id)
);

create table if not exists tags
(
    id bigint auto_increment
        primary key,
    created_by varchar(255) null,
    created_on datetime(6) null,
    updated_by varchar(255) null,
    updated_on datetime(6) null,
    name varchar(255) not null,
    constraint UK_t48xdq560gs3gap9g7jg36kgc
        unique (name)
);

create table if not exists certificates_tags
(
    certificate_id bigint not null,
    tag_id bigint not null,
    constraint FKralfn6foqh2x7djgc6bna16wn
        foreign key (tag_id) references tags (id),
    constraint FKs8r2j1rxfqawf5oiphtc42mgs
        foreign key (certificate_id) references certificates (id)
);

create table if not exists roles
(
    id int auto_increment
        primary key,
    created_by varchar(255) null,
    created_on datetime(6) null,
    updated_by varchar(255) null,
    updated_on datetime(6) null,
    name varchar(20) null
);

create table if not exists users_roles
(
    user_id bigint not null,
    role_id int not null,
    primary key (user_id, role_id),
    constraint FK2o0jvgh89lemvvo17cbqvdxaa
        foreign key (user_id) references users (id),
    constraint FKj6m8fwv7oqv74fcehir1a9ffy
        foreign key (role_id) references roles (id)
);

insert into users (user_name, email, created_on, created_by) values ('userName', 'user@mail.com', CURRENT_TIMESTAMP, 'user');
insert into certificates (name, description, price, duration, created_on, created_by) values ('new Certificate', 'Description of new certificate', 2.00, 90, CURRENT_TIMESTAMP, 'user');
insert into tags (name, created_on, created_by) values ('new Tag', CURRENT_TIMESTAMP, 'user');
insert into certificates_tags (certificate_id, tag_id) values (1, 1);
insert into roles (name, created_on, created_by) values ('USER', CURRENT_TIMESTAMP, 'user');
insert into users_roles (user_id, role_id) values (1, 1);
