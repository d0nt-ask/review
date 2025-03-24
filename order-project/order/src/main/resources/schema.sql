drop table if exists `order`;
create table `order`
(
    id                      bigint auto_increment,
    user_id                 varchar(255),
    status                  varchar(255),
    total_price             bigint,
    order_created_date_time   datetime(6),
    ordered_date_time   datetime(6),
    road_addr               varchar(1000),
    jibun_addr              varchar(1000),
    detail_addr            varchar(1000),
    created_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    created_id       varchar(255),
    modified_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    modified_id      varchar(255),
    primary key (id)
) engine=InnoDB;

alter table `order` auto_increment = 20000000;

drop table if exists order_product;
create table order_product
(
    id               bigint auto_increment,
    order_id         bigint,
    product_Id       varchar(255),
    product_name     varchar(255),
    quantity         int,
    price            bigint,
    total_price      bigint,
    created_date     TIMESTAMP DEFAULT CURRENT_TIMESTAMP not null,
    created_id       varchar(255),
    modified_date    TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP not null,
    modified_id      varchar(255),
    primary key (id)
) engine=InnoDB;

alter table order_product auto_increment = 20000000;
