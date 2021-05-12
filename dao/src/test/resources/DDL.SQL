set MODE MYSQL;

create table if not exists certificates
(
    id bigint auto_increment
    primary key,
    name varchar(200) not null,
    description varchar(500) not null,
    price decimal(5,2) not null,
    duration int not null,
    created_on timestamp not null,
    created_by varchar(50) not null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    constraint certificates_name_uindex
    unique (name)
    );

create table if not exists orders
(
    id bigint auto_increment
    primary key,
    user_id bigint not null,
    total_price decimal(5,2) not null,
    created_on timestamp not null,
    created_by varchar(50) not null,
    updated_on timestamp null,
    updated_by varchar(50) null
    );

create table if not exists orders_certificates
(
    order_id bigint not null,
    certificate_id bigint not null,
    quantity int not null,
    created_on timestamp not null,
    created_by varchar(50) not null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    primary key (order_id, certificate_id),
    constraint certificates_fk
    foreign key (certificate_id) references certificates (id),
    constraint orders_fk
    foreign key (order_id) references orders (id)
    );

create table if not exists tags
(
    id bigint auto_increment
    primary key,
    name varchar(50) not null,
    created_on timestamp not null,
    created_by varchar(50) not null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    constraint tags_name_uindex
    unique (name)
    );

create table if not exists certificates_tags
(
    certificate_id bigint not null,
    tag_id bigint not null,
    primary key (tag_id, certificate_id),
    constraint certificates_tags_certificates_fk
    foreign key (certificate_id) references certificates (id),
    constraint certificates_tags_tags_fk
    foreign key (tag_id) references tags (id)
    );

create table if not exists users
(
    id bigint auto_increment
    primary key,
    first_name varchar(50) not null,
    last_name varchar(50) not null,
    email varchar(50) not null,
    created_on timestamp not null,
    created_by varchar(50) not null,
    updated_on timestamp null,
    updated_by varchar(50) null,
    constraint users_email_uindex
    unique (email)
    );