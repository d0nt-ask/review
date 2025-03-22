drop table if exists `order`;
create table `order`
(
    id               bigint auto_increment,
    status     varchar(255),
    primary key (id)
) engine=InnoDB;

alter table `order` auto_increment = 10000000;

drop table if exists order_product;
create table order_product
(
    id               bigint auto_increment,
    order_id         bigint,
    product_Id        varchar(255),
    quantity         int,
    total_price       bigint,
    primary key (id)
) engine=InnoDB;

alter table order_product auto_increment = 10000000;
