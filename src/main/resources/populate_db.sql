USE `achoo_dev`;
INSERT INTO user(first_name, last_name, email, password, salt) values('Mathias','Cabano', 'cabanomathias@gmail.com', 'CZ4i3Yh+wuthP5yqZRRFKAMmMBY=', 'gL6bdcesQOQ=');
INSERT INTO user(first_name, last_name, email, password, salt) values('Alfredo','El Ters', 'alfredoelters@gmail.com', 'n07B0NvWJchGEKomzkPhrVjBBcQ=', 'eOBrddD0VU8=');


INSERT INTO drugstore(name, phone_number, address) values('Farmashop', '1234567', 'Priamo 1579');
INSERT INTO drugstore(name, phone_number, address) values('Farmacia Paris', '1234567', 'Belgrano 2874');
INSERT INTO drugstore(name, phone_number, address) values('Bristol', '1234567', 'Walcalde 2638');

INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (1,'Perifar 400', 'Calmante', 45);
INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (1,'Kataflan', 'Calmante muy fuerte', 100);
INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (2,'Domper', 'Anti nauseas', 80);
INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (2,'Aspirina', 'Calmante', 50);
INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (2,'Perifar 600', 'Calmante fuerte', 50);
INSERT INTO product(drugstore_id, product_name, product_description, product_unitary_price) values (3,'Perifar 400', 'Calmante', 45);
