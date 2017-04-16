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

insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 1,'Nike T-Shirt','Mens Black Basketball Top',50,40.00,13.00, 1 ,0,4.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 2,'Nike T-Shirt','Mens Bring Your Game Basketball Top',50,35.00,10.00, 1 ,0,4.5);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 3,'Nike T-Shirt','Mens Black & White Basketball Top',50,35.00, 9.00,1 ,0,4.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 4,'Nike T-Shirt','Good Things Come In Threes Top',50,40.00, 10.00,1 ,0,5.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 5,'Nike T-Shirt','Kobe Bryant Black Top',50,26.00, 7.00,1 ,0,4.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 6,'Nike T-Shirt','Kobe Bryant Grey Top',50,30.00 , 15.00,1,0,4.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 7,'Nike T-Shirt','Lebron James Grey Top',50,35.00,14.00, 1 ,0,4.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 8,'Jordan T-Shirt','Air Jordan Gret Top',50,26.00,12.00, 1 ,0,3.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 9,'Jordan T-Shirt','Air Jordan Black Top',50,30.00,11.00, 1 ,0,5.0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 10,'Jordan T-Shirt','Air Jordan Engineered For Flight Top',50,35.00 ,10.00, 1,0,4.5);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 11,'Jordan T-Shirt','Air Jordan Long Sleeve Top',50,45.00, 18.00,1 ,0, 3.5);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 12,'Jordan T-Shirt','Air Jordan No Excuses Top',50,30.00,16.00,1 ,0 ,5);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 13,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00,15.00, 1 ,0,0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 14,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00,8.00, 1 ,0,0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 15,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00,12.00, 1 ,0,0);
insert into product (id,name,description,stock,price,supplier_price,supplier_id, amount_sold, rating) values ( 16,'Jordan T-Shirt','Air Jordan No Excuses Top',5,30.00,10.00, 1 ,0,0);

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


insert into user (email,title,name,surname,password,role) values ( 'Aidan@products.com', 'Mr','Aidan', 'Dunne', 'password', 'admin' );
insert into user (email,title,name,surname,password,role) values ( 'Stephen@products.com','Mr','Stephen','Kelly', 'password', 'admin' );
insert into user (email,title,name,surname,password,role) values ( 'Jordan@products.com','Mr','Jordan','Gardner', 'password', 'admin' );
insert into user (email,title,name,surname,password,role,number, street1, town,county,post_code,country, num_of_orders,loyalty_points_earned ) values ( 'customer@products.com','Mr','Charlie','Customer','password', 'customer','0845678543', '32 Blue Street','West Tower','Dublin','27','Ireland', 0, 0);
insert into user (email,title,name,surname,password,role,number, street1, town,county,post_code,country, num_of_orders,loyalty_points_earned ) values ( 'customer2@products.com','Mr', 'John', 'Smith', 'password', 'customer','0863857395','49 Red Lane','Wayborn','Wicklow','21','Ireland',0, 0 );
insert into user (email,title,name,surname,password,role,number, street1, town,county,post_code,country, num_of_orders,loyalty_points_earned ) values ( 'customer3@products.com', 'Mr', 'Alice', 'Ryan', 'password', 'customer', '0834726863','97 Oakwood','Easthill','Cork','14','Ireland', 0, 0);


insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (1, 'customer@products.com', 'Delivery', 'How long does delivery normally take?', 1, 0, GETDATE());
insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (2,  'Stephen@products.com', 'Delivery', 'Your Order should be delivered within 5 working days.', 3,1, GETDATE());
insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (3,  'customer@products.com','Delivery', 'Thank you',0,0, GETDATE());
insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (4, 'customer2@products.com', 'Account details', 'How do i edit my address?', 1, 0, GETDATE());
insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (5,  'Jordan@products.com', 'Account details', 'Editing of your acount can be done on the account details page.', 2,0, GETDATE());
insert into forum_message (id, user_email,subject,message_content, likes, dislikes,message_date) values (6,  'customer3@products.com','Cavs vs Warriors', 'Anyone see the game?',0,0, GETDATE());

insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 1,1,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 2,1,'customer2@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 3,1,'customer3@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 4,2,'customer2@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 5,2,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 6,3,'customer3@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 7,3,'customer@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 8,3,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 9,4,'customer3@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 10,5,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 11,6,'customer@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 12,6,'customer2@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 13,7,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 14,7,'customer3@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 15,8,'customer2@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 16,9,'customer@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 17,10,'customer@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 18,10,'customer3@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 19,10,'customer@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 20,11,'customer3@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 21,11,'customer@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 22,12,'customer2@products.com','Great buy', 5, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 27,11,'customer@products.com','Great buy', 3, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 28,11,'customer2@products.com','Great buy', 4, GETDATE());
insert into Review(id,product_id,customer_email,description,stars,review_date) values
( 29,12,'customer3@products.com','Great buy', 5, GETDATE());


