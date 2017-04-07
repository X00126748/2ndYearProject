# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table basket (
  id                            bigint not null,
  customer_email                varchar(255),
  constraint uq_basket_customer_email unique (customer_email),
  constraint pk_basket primary key (id)
);
create sequence basket_seq;

create table category (
  id                            bigint not null,
  name                          varchar(255),
  constraint pk_category primary key (id)
);
create sequence category_seq;

create table category_product (
  category_id                   bigint not null,
  product_id                    bigint not null,
  constraint pk_category_product primary key (category_id,product_id)
);

create table forum_message (
  id                            bigint not null,
  subject                       varchar(255),
  message_content               varchar(255),
  user_email                    varchar(255),
  likes                         integer,
  dislikes                      integer,
  message_date                  timestamp,
  constraint pk_forum_message primary key (id)
);
create sequence forum_message_seq;

create table order_item (
  id                            bigint not null,
  order_id                      bigint,
  basket_id                     bigint,
  product_id                    bigint,
  quantity                      integer,
  price                         double,
  size                          varchar(255),
  selected_card                 varchar(255),
  constraint pk_order_item primary key (id)
);
create sequence order_item_seq;

create table payment_card (
  card_number                   varchar(255) not null,
  expiration_month              integer,
  expiration_year               integer,
  security_code                 integer,
  type                          varchar(255),
  customer_email                varchar(255),
  constraint pk_payment_card primary key (card_number)
);

create table product (
  id                            bigint not null,
  supplier_id                   bigint,
  name                          varchar(255),
  description                   varchar(255),
  stock                         integer,
  price                         double,
  amount_sold                   integer,
  rating                        double,
  constraint pk_product primary key (id)
);
create sequence product_seq;

create table review (
  id                            bigint not null,
  name                          varchar(255),
  product_id                    bigint,
  description                   varchar(255),
  review_date                   timestamp,
  stars                         double,
  review_count                  integer,
  constraint pk_review primary key (id)
);
create sequence review_seq;

create table shop_order (
  id                            bigint not null,
  loyalty_points_earned         integer,
  order_date                    timestamp,
  order_status                  varchar(255),
  customer_email                varchar(255),
  constraint pk_shop_order primary key (id)
);
create sequence shop_order_seq;

create table stock_basket (
  id                            bigint not null,
  admin_email                   varchar(255),
  constraint uq_stock_basket_admin_email unique (admin_email),
  constraint pk_stock_basket primary key (id)
);
create sequence stock_basket_seq;

create table stock_order (
  id                            bigint not null,
  order_date                    timestamp,
  order_status                  varchar(255),
  admin_email                   varchar(255),
  constraint pk_stock_order primary key (id)
);
create sequence stock_order_seq;

create table stock_order_item (
  id                            bigint not null,
  order_id                      bigint,
  basket_id                     bigint,
  product_id                    bigint,
  quantity                      integer,
  price                         double,
  size                          varchar(255),
  constraint pk_stock_order_item primary key (id)
);
create sequence stock_order_item_seq;

create table supplier (
  id                            bigint not null,
  name                          varchar(255),
  email                         varchar(255),
  number                        varchar(255),
  constraint pk_supplier primary key (id)
);
create sequence supplier_seq;

create table user (
  role                          varchar(255),
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  department                    varchar(255),
  street1                       varchar(255),
  street2                       varchar(255),
  town                          varchar(255),
  post_code                     varchar(255),
  country                       varchar(255),
  loyalty_points_earned         integer,
  num_of_orders                 integer,
  constraint pk_user primary key (email)
);

alter table basket add constraint fk_basket_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;

alter table category_product add constraint fk_category_product_category foreign key (category_id) references category (id) on delete restrict on update restrict;
create index ix_category_product_category on category_product (category_id);

alter table category_product add constraint fk_category_product_product foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_category_product_product on category_product (product_id);

alter table forum_message add constraint fk_forum_message_user_email foreign key (user_email) references user (email) on delete restrict on update restrict;
create index ix_forum_message_user_email on forum_message (user_email);

alter table order_item add constraint fk_order_item_order_id foreign key (order_id) references shop_order (id) on delete restrict on update restrict;
create index ix_order_item_order_id on order_item (order_id);

alter table order_item add constraint fk_order_item_basket_id foreign key (basket_id) references basket (id) on delete restrict on update restrict;
create index ix_order_item_basket_id on order_item (basket_id);

alter table order_item add constraint fk_order_item_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_order_item_product_id on order_item (product_id);

alter table payment_card add constraint fk_payment_card_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_payment_card_customer_email on payment_card (customer_email);

alter table product add constraint fk_product_supplier_id foreign key (supplier_id) references supplier (id) on delete restrict on update restrict;
create index ix_product_supplier_id on product (supplier_id);

alter table review add constraint fk_review_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_review_product_id on review (product_id);

alter table shop_order add constraint fk_shop_order_customer_email foreign key (customer_email) references user (email) on delete restrict on update restrict;
create index ix_shop_order_customer_email on shop_order (customer_email);

alter table stock_basket add constraint fk_stock_basket_admin_email foreign key (admin_email) references user (email) on delete restrict on update restrict;

alter table stock_order add constraint fk_stock_order_admin_email foreign key (admin_email) references user (email) on delete restrict on update restrict;
create index ix_stock_order_admin_email on stock_order (admin_email);

alter table stock_order_item add constraint fk_stock_order_item_order_id foreign key (order_id) references stock_order (id) on delete restrict on update restrict;
create index ix_stock_order_item_order_id on stock_order_item (order_id);

alter table stock_order_item add constraint fk_stock_order_item_basket_id foreign key (basket_id) references stock_basket (id) on delete restrict on update restrict;
create index ix_stock_order_item_basket_id on stock_order_item (basket_id);

alter table stock_order_item add constraint fk_stock_order_item_product_id foreign key (product_id) references product (id) on delete restrict on update restrict;
create index ix_stock_order_item_product_id on stock_order_item (product_id);


# --- !Downs

alter table basket drop constraint if exists fk_basket_customer_email;

alter table category_product drop constraint if exists fk_category_product_category;
drop index if exists ix_category_product_category;

alter table category_product drop constraint if exists fk_category_product_product;
drop index if exists ix_category_product_product;

alter table forum_message drop constraint if exists fk_forum_message_user_email;
drop index if exists ix_forum_message_user_email;

alter table order_item drop constraint if exists fk_order_item_order_id;
drop index if exists ix_order_item_order_id;

alter table order_item drop constraint if exists fk_order_item_basket_id;
drop index if exists ix_order_item_basket_id;

alter table order_item drop constraint if exists fk_order_item_product_id;
drop index if exists ix_order_item_product_id;

alter table payment_card drop constraint if exists fk_payment_card_customer_email;
drop index if exists ix_payment_card_customer_email;

alter table product drop constraint if exists fk_product_supplier_id;
drop index if exists ix_product_supplier_id;

alter table review drop constraint if exists fk_review_product_id;
drop index if exists ix_review_product_id;

alter table shop_order drop constraint if exists fk_shop_order_customer_email;
drop index if exists ix_shop_order_customer_email;

alter table stock_basket drop constraint if exists fk_stock_basket_admin_email;

alter table stock_order drop constraint if exists fk_stock_order_admin_email;
drop index if exists ix_stock_order_admin_email;

alter table stock_order_item drop constraint if exists fk_stock_order_item_order_id;
drop index if exists ix_stock_order_item_order_id;

alter table stock_order_item drop constraint if exists fk_stock_order_item_basket_id;
drop index if exists ix_stock_order_item_basket_id;

alter table stock_order_item drop constraint if exists fk_stock_order_item_product_id;
drop index if exists ix_stock_order_item_product_id;

drop table if exists basket;
drop sequence if exists basket_seq;

drop table if exists category;
drop sequence if exists category_seq;

drop table if exists category_product;

drop table if exists forum_message;
drop sequence if exists forum_message_seq;

drop table if exists order_item;
drop sequence if exists order_item_seq;

drop table if exists payment_card;

drop table if exists product;
drop sequence if exists product_seq;

drop table if exists review;
drop sequence if exists review_seq;

drop table if exists shop_order;
drop sequence if exists shop_order_seq;

drop table if exists stock_basket;
drop sequence if exists stock_basket_seq;

drop table if exists stock_order;
drop sequence if exists stock_order_seq;

drop table if exists stock_order_item;
drop sequence if exists stock_order_item_seq;

drop table if exists supplier;
drop sequence if exists supplier_seq;

drop table if exists user;

