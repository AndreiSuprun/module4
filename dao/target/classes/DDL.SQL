create table if not exists gift_certificates
(
    id int auto_increment
    primary key,
    name varchar(100) not null,
    description varchar(500) not null,
    price decimal(5,2) not null,
    duration int not null,
    create_date timestamp not null,
    last_update_date timestamp null
    );

create table if not exists tags
(
    id int auto_increment
    primary key,
    name varchar(45) not null,
    constraint tags_name_uindex
    unique (name)
    );

create table if not exists gift_certificate_tags
(
    gift_certificate_id int not null,
    tag_id int not null,
    primary key (tag_id, gift_certificate_id),
    constraint gift_certificate_tags_tags_id_fk
    foreign key (tag_id) references tags (id),
    constraint gift_certificates_id_fk
    foreign key (gift_certificate_id) references gift_certificates (id)
    );