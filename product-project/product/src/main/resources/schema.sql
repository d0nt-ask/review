drop table if exists product;
create table product
(
    id               bigint auto_increment,
    description      varchar(255),
    image_url        varchar(255),
    name             varchar(255),
    price            varchar(255),
    current_quantity bigint,
    created_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    created_id       varchar(255),
    modified_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    modified_id      varchar(255),
    primary key (id)
) engine=InnoDB;

alter table product auto_increment = 20000000;

drop table if exists product_image;
create table product_image
(
    id            BINARY(16) not null,
    product_id    bigint,
    file_id       binary(16),
    file_name     varchar(255),
    sequence      int,
    origin_url    varchar(255),
    thumbnail_url varchar(255),
    created_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    created_id       varchar(255),
    modified_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    modified_id      varchar(255),
    primary key (id)
) engine=InnoDB;

drop table if exists inventory;
create table inventory
(
    id               bigint auto_increment,
    product_id       bigint,
    current_quantity bigint,
    total_quantity   bigint,
    created_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    created_id       varchar(255),
    modified_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    modified_id      varchar(255),
    primary key (id)
) engine=InnoDB;

alter table inventory auto_increment = 20000000;
