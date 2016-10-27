--DROP DATABASE IF EXISTS hire_me;
--CREATE DATABASE hire_me DEFAULT CHARACTER SET utf8;
--USE hire_me;
--GRANT ALL ON hire_me.* TO 'zale144'@'%' IDENTIFIED BY 'pastazazube';
--FLUSH PRIVILEGES;

DROP TABLE IF EXISTS Rating;
DROP TABLE IF EXISTS Location;
DROP TABLE IF EXISTS Deal;
DROP TABLE IF EXISTS Service;
DROP TABLE IF EXISTS Category;
DROP TABLE IF EXISTS account_role;
DROP TABLE IF EXISTS Role;
DROP TABLE IF EXISTS Profile;
DROP TABLE IF EXISTS Account;

CREATE TABLE Role (
  id BIGINT NOT NULL,
  code VARCHAR(50) NOT NULL,
  label VARCHAR(100) NOT NULL,
--  ordinal INT NOT NULL,
--  effectiveAt DATETIME NOT NULL,
--  expiresAt DATETIME DEFAULT NULL,
--  createdAt DATETIME NOT NULL,
  PRIMARY KEY (id),
  CONSTRAINT UQ_Role_Code UNIQUE (code)
);

CREATE TABLE Account (
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
	CONSTRAINT UQ_Account_Username UNIQUE (username)
);

CREATE TABLE Profile (
	id BIGINT AUTO_INCREMENT NOT NULL,
	account_id BIGINT,
	firstname VARCHAR(100) NOT NULL,
	lastname VARCHAR(100) NOT NULL,
	email VARCHAR(200),
	phone_number VARCHAR(100), 
	picture_link VARCHAR(500),
	date_of_registration DATETIME,
	PRIMARY KEY (id),
	CONSTRAINT FK_account FOREIGN KEY (account_id) REFERENCES Account (id)
);

CREATE TABLE account_role (
  account_id BIGINT NOT NULL,
  role_id BIGINT NOT NULL,
  PRIMARY KEY (account_id, role_id),
  CONSTRAINT FK_account_role_Account FOREIGN KEY (account_id) REFERENCES Account (id),
  CONSTRAINT FK_account_role_Role FOREIGN KEY (role_id) REFERENCES Role (id)
);

CREATE TABLE Category (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(100) NOT NULL,
	PRIMARY KEY (id)
);

CREATE TABLE Service (
	id BIGINT AUTO_INCREMENT NOT NULL,
	name VARCHAR(100) NOT NULL,
	category_id BIGINT NOT NULL,
	CONSTRAINT FK_category FOREIGN KEY (category_id) REFERENCES Category (id),
	PRIMARY KEY (id)
);

CREATE TABLE Deal (
	id BIGINT AUTO_INCREMENT NOT NULL,
	description VARCHAR(1000) NOT NULL,
	profile_id BIGINT NOT NULL,
	service_id BIGINT NOT NULL,
	service_cost DOUBLE(15,2) NOT NULL,
	CONSTRAINT FK_profile FOREIGN KEY (profile_id) REFERENCES Profile (id),
	CONSTRAINT FK_service FOREIGN KEY (service_id) REFERENCES Service (id),
	PRIMARY KEY (id)
);

CREATE TABLE Rating (
	id BIGINT AUTO_INCREMENT NOT NULL,
	comment VARCHAR(1000),
	customer_name VARCHAR(200) NOT NULL,
	time_of_rating DATE,
	rating INTEGER NOT NULL,
	deal_id BIGINT NOT NULL,
	CONSTRAINT FK_rating_deal FOREIGN KEY (deal_id) REFERENCES Deal (id),
	PRIMARY KEY (id)
);

CREATE TABLE Location (
	id BIGINT AUTO_INCREMENT NOT NULL,
	country VARCHAR(200) NOT NULL,
	city VARCHAR(200) NOT NULL,
	street VARCHAR(200) NOT NULL,
	number VARCHAR(20) NOT NULL,
	latitude DOUBLE(10, 6) NOT NULL,
	longitude DOUBLE(10, 6) NOT NULL,
	deal_id BIGINT NOT NULL,
	CONSTRAINT FK_location_deal FOREIGN KEY (deal_id) REFERENCES Deal (id),
	PRIMARY KEY (id)
);