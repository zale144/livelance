DROP DATABASE IF EXISTS hire_me;
CREATE DATABASE hire_me DEFAULT CHARACTER SET UTF8;

USE hire_me;

GRANT ALL ON hire_me.* TO 'zale144'@'%' IDENTIFIED BY 'pastazazube';

FLUSH PRIVILEGES;

DROP TABLE IF EXISTS rating;
DROP TABLE IF EXISTS location;
DROP TABLE IF EXISTS deal;
DROP TABLE IF EXISTS service;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS account_role;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS profile;
DROP TABLE IF EXISTS account;

CREATE TABLE role (
  id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  label VARCHAR(100) NOT NULL,
--  ordinal INT NOT NULL,
--  effectiveAt DATETIME NOT NULL,
--  expiresAt DATETIME DEFAULT NULL,
--  createdAt DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT uq_role_code UNIQUE (code)
);

CREATE TABLE account (
	id BIGINT AUTO_INCREMENT NOT NULL,
--	reference_id VARCHAR(255) NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(200) NOT NULL,
	enabled BOOLEAN DEFAULT false NOT NULL,
	credentialsexpired BOOLEAN DEFAULT false NOT NULL,
	expired BOOLEAN DEFAULT false NOT NULL,
	locked BOOLEAN DEFAULT false NOT NULL,
--	version INT NOT NULL,
-- 	createdBy VARCHAR(100) NOT NULL,
--  	createdAt DATETIME NOT NULL,
--  	updatedBy VARCHAR(100) DEFAULT NULL,
--  	updatedAt DATETIME DEFAULT NULL,
	PRIMARY KEY (id),
--	CONSTRAINT UQ_tbl_account_reference_id UNIQUE (reference_id),
	CONSTRAINT uq_account_username UNIQUE (username)
);

CREATE TABLE profile (
	id BIGINT AUTO_INCREMENT NOT NULL,
	account_id BIGINT,
	firstname VARCHAR(100) NOT NULL,
	lastname VARCHAR(100) NOT NULL,
	email VARCHAR(200),
	phone_number VARCHAR(100), 
	picture_link VARCHAR(500),
	website VARCHAR(500),
	facebook VARCHAR(500),
	about VARCHAR(2000),
	date_of_registration DATETIME,
	PRIMARY KEY (id),
	CONSTRAINT fk_account FOREIGN KEY (account_id) REFERENCES account (id)
);

CREATE TABLE account_role (
  account_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (account_id, role_id),
  CONSTRAINT fk_account_role_account FOREIGN KEY (account_id) REFERENCES account (id),
  CONSTRAINT fk_account_role_role FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE category (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(100) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE service (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(100) NOT NULL,
	category_id BIGINT NOT NULL,
	CONSTRAINT fk_category FOREIGN KEY (category_id) REFERENCES category (id),
	PRIMARY KEY (id)
);

CREATE TABLE deal (
	id BIGINT AUTO_INCREMENT NOT NULL,
	description VARCHAR(1000) NOT NULL,
	long_description VARCHAR (2000),
	profile_id BIGINT NOT NULL,
	service_id BIGINT NOT NULL,
	service_cost DOUBLE(15,2) NOT NULL,
	CONSTRAINT fk_profile FOREIGN KEY (profile_id) REFERENCES profile (id),
	CONSTRAINT fk_service FOREIGN KEY (service_id) REFERENCES service (id),
	PRIMARY KEY (id)
);

CREATE TABLE rating (
	id BIGINT AUTO_INCREMENT NOT NULL,
	comment VARCHAR(1000),
	customer_name VARCHAR(200) NOT NULL,
	time_of_rating DATE,
	rating INTEGER NOT NULL,
	deal_id BIGINT NOT NULL,
	CONSTRAINT fk_rating_deal FOREIGN KEY (deal_id) REFERENCES deal (id),
	PRIMARY KEY (id)
);

CREATE TABLE location (
	id BIGINT AUTO_INCREMENT NOT NULL,
	country VARCHAR(200) NOT NULL,
	city VARCHAR(200) NOT NULL,
	street VARCHAR(200),
	number VARCHAR(20),
	latitude DOUBLE(10, 6) NOT NULL,
	longitude DOUBLE(10, 6) NOT NULL,
	deal_id BIGINT NOT NULL,
	CONSTRAINT fk_location_deal FOREIGN KEY (deal_id) REFERENCES deal (id),
	PRIMARY KEY (id)
);

select * from account;

select * from location;

select * from deal;

select * from service;

select * from category;

select * from rating;

select * from account_role;

select * from profile;

select * from role;


INSERT INTO role (id, code, label)
VALUES (1, 'ROLE_USER', 'User');
INSERT INTO role (id, code, label) 
VALUES (2, 'ROLE_ADMIN', 'Admin');
INSERT INTO role (id, code, label) 
VALUES (3, 'ROLE_SYSADMIN', 'System Admin');

INSERT INTO account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('jane', '$2a$04$oCNqkdWPn77cQ7c4S6ylfe3WGYdf6QXH7AV.wkaDDUjqAFtjt.XyC', true, false, false, false);
INSERT INTO account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('pete', '$2a$04$iaCt6D1GR2QhQw8wZYyi3Orrzw7m0JwGTLJiz7hGomCtU8R5faeEe', true, false, false, false);
INSERT INTO account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('shogo', '$2a$04$3NNhArZIGWt5J0WREJAy7eTmUxRC3qVr9qxddJFuvXdegzEeohMQ.', true, false, false, false);
INSERT INTO account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('kris', '$2a$04$3NNhArZIGWt5J0WREJAy7eTmUxRC3qVr9qxddJFuvXdegzEeohMQ.', true, false, false, false);

INSERT INTO profile (account_id, firstname, lastname, email, phone_number, picture_link, website, facebook,
about, date_of_registration) 
VALUES (1, 'Janice', 'Jones', 'jane@example.com', '062/123456', 
'https://i.guim.co.uk/img/static/sys-images/Media/Pix/pictures/2011/7/20/1311186215351/Janice-Hadlow-007.
jpg?w=470&q=55&auto=format&usm=12&fit=max&s=dad4c5433205104558516bc23c823f7b',
'http://www.somelink1.rs', 'http://www.facebook/someprofile1',
'My name is Jane, and I enjoy meeting new people and finding ways to help them have an uplifting experience. 
I have had a variety of customer service opportunities, through which I was able to have fewer returned products and 
increased repeat customers, when compared with co-workers. I am dedicated, outgoing, and a team player.','2016-01-19');
INSERT INTO profile (account_id, firstname, lastname, email, phone_number, picture_link, website, facebook,
about, date_of_registration) 
VALUES (2, 'Peter', 'Peterson', 'pete@example.com', '062/223456', 
'http://www.istanbulexpats.com/wp-content/uploads/2014/11/odd-job-handyman-istanbul.jpg', 
'http://www.somelink2.rs', 'http://www.facebook/someprofile2',
'People find me to be an upbeat, self-motivated team player with excellent communication skills.
 For the past several years I have worked in lead qualification, telemarketing, and customer service in the technology industry.
 My experience includes successfully calling people in director-level positions of technology departments
 and developing viable leads. ', '2015-08-22');
INSERT INTO profile (account_id, firstname, lastname, email, phone_number, picture_link, website, facebook,
about, date_of_registration) 
VALUES (3, 'Makishima', 'Shogo', 'shogo@example.com', '062/323456',
'http://www.kicnano.cornell.edu/wp-content/uploads/2010/12/shogo-hamada-pic.jpg',
'http://www.somelink2.rs', 'http://www.facebook/someprofile2',
'I am a dedicated person with a family of four. I enjoy reading, and the knowledge and perspective that my reading gives
 me has strengthened my teaching skills and presentation abilities. I have been successful at raising a family, and 
I attribute this success to my ability to plan, schedule, and handle many different tasks at once. This flexibility 
will help me in the classroom, where there are many different personalities and learning styles.', '2014-11-30');
INSERT INTO profile (account_id, firstname, lastname, email, phone_number, picture_link, website, facebook,
about, date_of_registration) 
VALUES (4, 'Kristofer', 'Dahl', 'kris@example.com', '062/8053456',
'http://ll-media.tmz.com/2015/10/02/1002-kristofer-buckle-getty-4.jpg',
'http://www.somelink3.rs', 'http://www.facebook/someprofile3',
'I am an experienced professional hairdresser. I enjoy working on challenging cases, and learning new things that will  help
work on my skills and creative abilities. I have been highly successful at achieving world renowned results, and 
I attribute this success to my ability to organize, assess, and implement. This abliity 
will help me achieve the best results, while maintaining a professional demeanor.', '2014-11-30');

INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
INSERT INTO account_role (account_id, role_id) VALUES (2, 1);
INSERT INTO account_role (account_id, role_id) VALUES (2, 2);
INSERT INTO account_role (account_id, role_id) VALUES (3, 1);
INSERT INTO account_role (account_id, role_id) VALUES (3, 2);
INSERT INTO account_role (account_id, role_id) VALUES (3, 3);
INSERT INTO account_role (account_id, role_id) VALUES (4, 1);


INSERT INTO category (name) VALUES ('Therapy');
INSERT INTO category (name) VALUES ('Technology');
INSERT INTO category (name) VALUES ('Education');
INSERT INTO category (name) VALUES ('Cosmetics');
INSERT INTO category (name) VALUES ('Design');

INSERT INTO service (name, category_id) VALUES ('Massage', 1);
INSERT INTO service (name, category_id) VALUES ('Physiotherapy', 1);
INSERT INTO service (name, category_id) VALUES ('Plumbing', 2);
INSERT INTO service (name, category_id) VALUES ('Electrical maintenance', 2);
INSERT INTO service (name, category_id) VALUES ('Computer repair', 2);
INSERT INTO service (name, category_id) VALUES ('Car mechanics', 2);
INSERT INTO service (name, category_id) VALUES ('English lessons', 3);
INSERT INTO service (name, category_id) VALUES ('Guitar lessons', 3);
INSERT INTO service (name, category_id) VALUES ('Swimming lessons', 3);
INSERT INTO service (name, category_id) VALUES ('Manicure/pedicure', 4);
INSERT INTO service (name, category_id) VALUES ('Hairstyling', 4);
INSERT INTO service (name, category_id) VALUES ('Makeup', 4);
INSERT INTO service (name, category_id) VALUES ('Face treatment', 4);
INSERT INTO service (name, category_id) VALUES ('Graphic design', 5);
INSERT INTO service (name, category_id) VALUES ('Web design', 5);
INSERT INTO service (name, category_id) VALUES ('Interior design', 5);
INSERT INTO service (name, category_id) VALUES ('Tailoring/sewing', 5);

INSERT INTO deal (profile_id, service_id, service_cost, description, long_description) 
VALUES (1, 2, 1200.0, 'Shiatsu massage, now on discount!',
'Over four years of physical therapy experience, working with progressively more challenging 
cases and more diverse clients.');
INSERT INTO deal (profile_id, service_id, service_cost, description, long_description) 
VALUES (2, 3, 950.0, 'All types of plumbing maintenance!', 
'Nine years of experience developing, implementing, and managing complex projects 
within time and budgetary constraings.');
INSERT INTO deal (profile_id, service_id, service_cost, description, long_description) 
VALUES (3, 7, 500.0, 'Prepare for your IELTS certification!',
'More than six years of advanced English teaching experience, with strong emphasis on  
business skills and a broad range of teaching methods.');
INSERT INTO deal (profile_id, service_id, service_cost, description, long_description) 
VALUES (2, 12, 1000.0, 'Pro makeup for all occasions: weddings, birthdays, parties...', 
'A self-motivated and organized professional with over 10 years of experience providing thorough and skillful
support to aforementioned department.');
INSERT INTO deal (profile_id, service_id, service_cost, description, long_description) 
VALUES (4, 11, 2400.0, 'Professional hairdresser, for both men and women.', 
'I am a professional hairdresser with over 20 years of experience having worked with the most demanding
styles and customers.');

INSERT INTO location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Branka Bajica', '46', 45.258327, 19.821259, 1);
INSERT INTO location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Todora Toze Jovanovica', '13', 45.254718, 19.804984, 2);
INSERT INTO location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Isidore Sekulic', '5',45.249271, 19.799630, 3);
INSERT INTO location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Jovana Popovica', '18',45.245052, 19.811276, 4);
INSERT INTO location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Bulevar Oslobodjenja', '33',45.259742, 19.832404, 5);

INSERT INTO rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('Awesome! I highly recommend it!', 'CicaMica', now(), 5, 1);
INSERT INTO rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('Not too bad..', 'Anonymous', now(), 4, 2);
INSERT INTO rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('not really that great, tbh...', 'jblnk', now(), 2, 3);
INSERT INTO rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('its really as good as it says', 'Anonymous', now(), 5, 5);

-- 
insert into location (deal_id, country, city, street, number, latitude, longitude) 
values (3, 'Serbia', 'Novi Sad', 'Петра Драпшина', '21', 45.447535, 19.691673);
-- 
-- select d.id, d.description, d.service_cost, d.service_id, d.profile_id
-- from 
-- 	deal d
--     inner join service s on s.id = d.service_id
--     inner join category c on c.id = s.category_id
--     inner join location l on l.deal_id = d.id
-- where c.name = 'Tehnika' and
-- 	l.latitude > 45.447535 - 0.1 and l.latitude < 45.447535 + 0.1 
-- 	and l.longitude > 19.691673 - 0.1 and l.longitude < 19.691673 + 0.1
--     group by deal_id;
-- 
-- select d.id, d.description, d.service_cost, d.service_id, d.profile_id
-- from 
-- 	deal d
--     inner join service s on s.id = d.service_id
--     inner join location l on l.deal_id = d.id
-- where s.name = 'Vodo' and
-- 	l.latitude > 45.447535 - 0.1 and l.latitude < 45.447535 + 0.1 
-- 	and l.longitude > 19.691673 - 0.1 and l.longitude < 19.691673 + 0.1
--     group by deal_id;
-- 
-- select d.id, d.description, d.service_cost, d.service_id, d.profile_id
-- from 
-- 	deal d
--     inner join location l on l.deal_id = d.id
-- where d.description like '%Vodo%' and
-- 	l.latitude > 45.447535 - 0.1 and l.latitude < 45.447535 + 0.1 
-- 	and l.longitude > 19.691673 - 0.1 and l.longitude < 19.691673 + 0.1
--     group by deal_id;
-- 
-- select *
-- from 
-- 	deal  d
--     inner join location l on l.deal_id = d.id
-- where get_distance(l.latitude, 44.601933313624194, l.longitude, 22.861440019726615) < 3000
--     group by deal_id;
-- 
-- 
-- DELIMITER $$
-- 
-- DROP FUNCTION IF EXISTS `get_distance` $$
-- CREATE FUNCTION get_distance(geo1_latitude decimal(10,6), geo1_longitude decimal(10,6), geo2_latitude decimal(10,6), geo2_longitude decimal(10,6)) 
-- returns decimal(10,3) DETERMINISTIC
-- BEGIN
--   return (((ACOS(SIN(geo1_latitude * PI() / 180) * SIN(geo2_latitude * PI() / 180) + COS(geo1_latitude * PI() / 180) * COS(geo2_latitude * PI() / 180) * COS((geo1_longitude - geo2_longitude) * PI() / 180)) * 180 / PI()) * 60 * 1.1515 * 1.609344/1000));
-- END $$
-- 
-- DELIMITER ;


