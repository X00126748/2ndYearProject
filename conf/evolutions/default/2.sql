# --- Sample dataset

# --- !Ups

insert into category (id,name) values ( 1,'Shoes' );
insert into category (id,name) values ( 2,'Jerseys' );
insert into category (id,name) values ( 3,'Shorts' );
insert into category (id,name) values ( 4,'T-Shirts' );
insert into category (id,name) values ( 5,'Jumpers' );
insert into category (id,name) values ( 6,'Hats' );
insert into category (id,name) values ( 7,'Coaching Equipment' ); 
insert into category (id,name) values ( 8,'Miscellaneous' ); 
insert into category (id,name) values ( 9,'Mens'); 
insert into category (id,name) values ( 10,'Womens');

insert into supplier (id,name, email, number) values (1,'ABC Product Supplier','customer@products.com', '0855436782');

insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 1,'Nike T-Shirt','Mens Black Basketball Top',50,40.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 2,'Nike T-Shirt','Mens Bring Your Game Basketball Top',50,35.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 3,'Nike T-Shirt','Mens Black & White Basketball Top',50,35.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 4,'Nike T-Shirt','Good Things Come In Threes Top',50,40.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 5,'Nike T-Shirt','Kobe Bryant Black Top',50,26.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 6,'Nike T-Shirt','Kobe Bryant Grey Top',50,30.00 , 1,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 7,'Nike T-Shirt','Lebron James Grey Top',50,35.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 8,'Jordan T-Shirt','Air Jordan Gret Top',50,26.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 9,'Jordan T-Shirt','Air Jordan Black Top',50,30.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 10,'Jordan T-Shirt','Air Jordan Engineered For Flight Top',50,35.00 , 1,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 11,'Jordan T-Shirt','Air Jordan Long Sleeve Top',50,45.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 12,'Jordan T-Shirt','Air Jordan No Excuses Top',50,30.00,1 ,0 );
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 13,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 14,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 15,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00, 1 ,0);
insert into product (id,name,description,stock,price,supplier_id, amount_sold) values ( 16,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00, 1 ,0);

insert into category_product (category_id,product_id) values (4,1);
insert into category_product (category_id,product_id) values (4,2);
insert into category_product (category_id,product_id) values (4,3);
insert into category_product (category_id,product_id) values (4,4);
insert into category_product (category_id,product_id) values (4,5);
insert into category_product (category_id,product_id) values (4,6);
insert into category_product (category_id,product_id) values (4,7);
insert into category_product (category_id,product_id) values (4,8);  
insert into category_product (category_id,product_id) values (4,9);
insert into category_product (category_id,product_id) values (4,10);
insert into category_product (category_id,product_id) values (4,11);
insert into category_product (category_id,product_id) values (4,12);
insert into category_product (category_id,product_id) values (9,1);
insert into category_product (category_id,product_id) values (9,2);
insert into category_product (category_id,product_id) values (9,3);  
insert into category_product (category_id,product_id) values (9,4); 
insert into category_product (category_id,product_id) values (9,5); 
insert into category_product (category_id,product_id) values (9,6); 
insert into category_product (category_id,product_id) values (9,7); 
insert into category_product (category_id,product_id) values (9,8); 
insert into category_product (category_id,product_id) values (9,9); 
insert into category_product (category_id,product_id) values (9,10); 
insert into category_product (category_id,product_id) values (9,11); 
insert into category_product (category_id,product_id) values (9,12); 
insert into category_product (category_id,product_id) values (9,13); 
insert into category_product (category_id,product_id) values (9,14); 
insert into category_product (category_id,product_id) values (9,15); 
insert into category_product (category_id,product_id) values (9,16);
insert into category_product (category_id,product_id) values (1,16);

insert into Review(id,product_id,name,description,stars) values
( 1,1,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 2,1,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 3,1,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 4,2,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 5,2,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 6,3,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 7,3,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 8,3,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 9,4,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 10,5,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 11,6,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 12,6,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 13,7,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 14,7,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 15,8,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 16,9,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 17,10,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 18,10,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 19,10,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 20,11,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 21,11,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 22,12,'Joe Bloggs','Great buy', 5);
insert into Review(id,product_id,name,description,stars) values
( 27,11,'Joe Bloggs','Great buy', 3);
insert into Review(id,product_id,name,description,stars) values
( 28,11,'Joe Bloggs','Great buy', 4);
insert into Review(id,product_id,name,description,stars) values
( 29,12,'Joe Bloggs','Great buy', 5);


insert into user (email,name,password,role) values ( 'Aidan@products.com', 'Aidan', 'password', 'admin' );
insert into user (email,name,password,role) values ( 'Stephen@products.com', 'Stephen', 'password', 'admin' );
insert into user (email,name,password,role) values ( 'Jordan@products.com', 'Jordan', 'password', 'admin' );
insert into user (email,name,password,role) values ( 'customer@products.com', 'Charlie Customer', 'password', 'customer' );
insert into user (email,name,password,role) values ( 'customer2@products.com', 'John Smith', 'password', 'customer' );
insert into user (email,name,password,role) values ( 'customer3@products.com', 'Alice Ryan', 'password', 'customer' );

insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (1, 'customer@products.com', 'Delivery', 'How long does delivery normally take?', 1, 0);
insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (2,  'Stephen@products.com', 'Delivery', 'Your Order should be delivered within 5 working days.', 3,1);
insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (3,  'customer@products.com','Delivery', 'Thank you',0,0);
insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (4, 'customer2@products.com', 'Account details', 'How do i edit my address?', 1, 0);
insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (5,  'Jordan@products.com', 'Account details', 'Editing of your acount can be done on the account details page.', 2,0);
insert into forum_message (id, user_email,subject,message_content, likes, dislikes) values (6,  'customer3@products.com','Cavs vs Warriors', 'Anyone see the game?',0,0);



