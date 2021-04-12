create table if not exists gift_certificates
(
    id int auto_increment
    primary key,
    name varchar(45) not null,
    description varchar(100) not null,
    price decimal(5,2) not null,
    duration int not null,
    create_date datetime(3) not null,
    last_update_date datetime(3) null
    );

create table if not exists tags
(
    id int auto_increment
    primary key,
    name varchar(45) not null
    );

create table if not exists gift_certificate_tag
(
    gift_certificate int not null,
    tag int not null,
    primary key (gift_certificate, tag),
    constraint gift_certificate_fk
    foreign key (gift_certificate) references gift_certificates (id),
    constraint tag_fk
    foreign key (gift_certificate) references tags (id)
    );