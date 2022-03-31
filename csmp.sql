create database csmp;
use csmp;

create table part
(
    skuId  int         not null,
    amount int         not null,
    price  double      not null,
    name   varchar(50) not null,
    constraint part_pk
        primary key (skuId)
);

create table computer
(
    skuId  int         not null,
    amount int         not null,
    price  double      not null,
    name   varchar(50) not null,
    constraint part_pk
        primary key (skuId)
);

create table sellingRecords
(
    id       int         not null,
    price    double      not null,
    amount   int         not null,
    skuId    int         not null,
    dateTime varchar(50) not null,
    type     varchar(20) not null,
    constraint sellRecord_pk
        primary key (id)
);

create table refillRecords
(
    id       int         not null,
    price    double      not null,
    amount   int         not null,
    skuId    int         not null,
    dateTime varchar(50) not null,
    type     varchar(20) not null,
    constraint sellRecord_pk
        primary key (id)
);

create table settings
(
    sid int not null,
    value int not null,
    constraint settings_pk
        primary key (sid)
);

insert into settings (sid, value)
values (1, 0);

create table password
(
    pass varchar(200) not null
);

insert into password (pass)
values ('');









