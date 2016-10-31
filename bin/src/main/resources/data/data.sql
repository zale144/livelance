
INSERT INTO Role (id, code, label)
VALUES (1, 'ROLE_USER', 'User');
INSERT INTO Role (id, code, label) 
VALUES (2, 'ROLE_ADMIN', 'Admin');
INSERT INTO Role (id, code, label) 
VALUES (3, 'ROLE_SYSADMIN', 'System Admin');

INSERT INTO Account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('jova', '$2a$04$oCNqkdWPn77cQ7c4S6ylfe3WGYdf6QXH7AV.wkaDDUjqAFtjt.XyC', true, false, false, false);
INSERT INTO Account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('pera', '$2a$04$iaCt6D1GR2QhQw8wZYyi3Orrzw7m0JwGTLJiz7hGomCtU8R5faeEe', true, false, false, false);
INSERT INTO Account (username, password, enabled, credentialsexpired, expired, locked) 
VALUES ('boja', '$2a$04$3NNhArZIGWt5J0WREJAy7eTmUxRC3qVr9qxddJFuvXdegzEeohMQ.', true, false, false, false);

INSERT INTO Profile (account_id, firstname, lastname, email, phone_number, picture_link, date_of_registration) 
VALUES (1, 'Jovan', 'Jovanovic', 'jovan@example.com', '062/123456', 
'http://www.mysticmadness.com/wp-content/uploads/2010/04/How-To-Tell-If-A-Guy-Has-A-Feeling-For-You.jpg', '2016-01-19');
INSERT INTO Profile (account_id, firstname, lastname, email, phone_number, picture_link, date_of_registration) 
VALUES (2, 'Petar', 'Petrovic', 'pera@example.com', '062/223456', 
'http://www.istanbulexpats.com/wp-content/uploads/2014/11/odd-job-handyman-istanbul.jpg', '2015-08-22');
INSERT INTO Profile (account_id, firstname, lastname, email, phone_number, picture_link, date_of_registration) 
VALUES (3, 'Bojana', 'Bojanic', 'boja@example.com', '062/323456',
'http://www.businessnewsdaily.com/images/i/000/002/133/i02/teacher.jpg?1334882765', '2014-11-30');

INSERT INTO account_role (account_id, role_id) VALUES (1, 1);
INSERT INTO account_role (account_id, role_id) VALUES (2, 1);
INSERT INTO account_role (account_id, role_id) VALUES (2, 2);
INSERT INTO account_role (account_id, role_id) VALUES (3, 1);
INSERT INTO account_role (account_id, role_id) VALUES (3, 2);
INSERT INTO account_role (account_id, role_id) VALUES (3, 3);

INSERT INTO Category (name) VALUES ('Terapija');
INSERT INTO Category (name) VALUES ('Tehnika');
INSERT INTO Category (name) VALUES ('Edukacija');
INSERT INTO Category (name) VALUES ('Kozmetika');
INSERT INTO Category (name) VALUES ('Dizajn');

INSERT INTO Service (name, category_id) VALUES ('Masaža', 1);
INSERT INTO Service (name, category_id) VALUES ('Fizioterapija', 1);
INSERT INTO Service (name, category_id) VALUES ('Vodoinstalacije', 2);
INSERT INTO Service (name, category_id) VALUES ('Elektroinstalacije', 2);
INSERT INTO Service (name, category_id) VALUES ('Servisiranje računara', 2);
INSERT INTO Service (name, category_id) VALUES ('Servisiranje automobila', 2);
INSERT INTO Service (name, category_id) VALUES ('Časovi engleskog', 3);
INSERT INTO Service (name, category_id) VALUES ('Časovi gitare', 3);
INSERT INTO Service (name, category_id) VALUES ('Časovi plivanja', 3);
INSERT INTO Service (name, category_id) VALUES ('Manikir/pedikir', 4);
INSERT INTO Service (name, category_id) VALUES ('Frizeraj', 4);
INSERT INTO Service (name, category_id) VALUES ('Šminkanje', 4);
INSERT INTO Service (name, category_id) VALUES ('Kozmetika', 4);
INSERT INTO Service (name, category_id) VALUES ('Grafički dizajn', 5);
INSERT INTO Service (name, category_id) VALUES ('Web dizajn', 5);
INSERT INTO Service (name, category_id) VALUES ('Enterijer', 5);
INSERT INTO Service (name, category_id) VALUES ('Krojenje/šivenje', 5);

INSERT INTO Deal (profile_id, service_id, service_cost, description) 
VALUES (1, 1, 1200.0, 'Profi masaza, povoljno!');
INSERT INTO Deal (profile_id, service_id, service_cost, description) 
VALUES (2, 3, 950.0, 'Sve vrste vodoinstalaterskih popravki!');
INSERT INTO Deal (profile_id, service_id, service_cost, description) 
VALUES (3, 7, 500.0, 'Spremam za IELTS sertifikacije');
--INSERT INTO Deal (profile_id, service_id, service_cost, description) 
--VALUES (2, 12, 1000.0, 'Profi šminka za sve moguće prilike, venčanja, rođendani, izlasci...');

INSERT INTO Location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Novosadskog Sajma', '22a', 45.456933, 19.683760, 1);
INSERT INTO Location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Zmajevo', '1. Maja', '39', 45.447535, 19.691673, 2);
INSERT INTO Location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Vrbas', 'Narodnog Fronta', '44',45.463995, 19.671365, 3);
INSERT INTO Location (country, city, street, number, latitude, longitude, deal_id) 
VALUES ('Serbia', 'Novi Sad', 'Knez Mihajlova', '18',45.453745, 19.683396, 3);

INSERT INTO Rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('Prezadovoljna sam!!!!!', 'CicaMica', now(), 5, 1);
INSERT INTO Rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('nije jako lose', 'Anoniman', now(), 4, 2);
INSERT INTO Rating (comment, customer_name, time_of_rating, rating, deal_id) 
VALUES ('nije bas najbolje', 'Jablanko', now(), 2, 3);