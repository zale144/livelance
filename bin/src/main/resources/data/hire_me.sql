DROP DATABASE IF EXISTS hire_me;
CREATE DATABASE hire_me DEFAULT CHARACTER SET utf8;

USE hire_me;

GRANT ALL ON hire_me.* TO 'zale144'@'%' IDENTIFIED BY 'pastazazube';

FLUSH PRIVILEGES;

select * from Account;

select * from Location;

select * from Deal;

select * from Service;

select * from Category;

select * from Rating;

select * from Account_Role;

select * from Profile;

select * from Role;

INSERT INTO Role (id, code, label)
VALUES (1, 'ROLE_USER', 'User');

INSERT INTO account_role (account_id, role_id) VALUES (1, 1);

INSERT INTO Service (name, category_id) VALUES ('Akupunktura', 1);

INSERT INTO Deal (profile, service, serviceCost, description) 
VALUES (3, 1, 1200.0, 'Profi masaza, povoljno!');

INSERT INTO Rating (comment, customerName, timeOfRating, rating, deal) 
VALUES ('bezveze', 'rasa', now(), 2, 1);

INSERT INTO Location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Knez Mihajlova', '18',-323.345474, 44.543442, 3);

INSERT INTO Rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('U sustini je dobro, nemam primedbe', 'Veselin', now(), 5, 3);

delete from Rating where id = 6;

select * from Profile;

delete from Profile where id = 6;
delete from Account_Role where account_id = 6;
delete from Account where id = 6;

insert into account (username, password, enabled) values ('asdf', '$2a$10$UZCAmpm2gKwDrngRkm7aGO0FmW9sHM/U4nHXNFO066nDJANUYiZ0.', true);
insert into account_role (account_id, role_id) values (8, 1);