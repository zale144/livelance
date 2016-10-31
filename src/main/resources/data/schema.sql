--DROP DATABASE IF EXISTS _db_livelance;
--CREATE DATABASE _db_livelance DEFAULT CHARACTER SET utf8;
--USE _db_livelance;
--GRANT ALL ON _db_livelance.* TO 'li_db_user'@'%' IDENTIFIED BY '5BXnPwzao8kjVfCt';
--FLUSH PRIVILEGES;

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
  PRIMARY KEY (id),
  CONSTRAINT uq_role_code UNIQUE (code)
);

CREATE TABLE account (
	id BIGINT AUTO_INCREMENT NOT NULL,
	username VARCHAR(100) NOT NULL,
	password VARCHAR(200) NOT NULL,
	enabled BOOLEAN DEFAULT false NOT NULL,
	credentialsexpired BOOLEAN DEFAULT false NOT NULL,
	expired BOOLEAN DEFAULT false NOT NULL,
	locked BOOLEAN DEFAULT false NOT NULL,
	PRIMARY KEY (id),
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


