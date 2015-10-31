USE `achoo_dev`;
INSERT INTO user(first_name, last_name, email, password, salt) values('Mathias','Cabano', 'cabanomathias@gmail.com', 'CZ4i3Yh+wuthP5yqZRRFKAMmMBY=', 'gL6bdcesQOQ=');
INSERT INTO user(first_name, last_name, email, password, salt) values('Alfredo','El Ters', 'alfredoelters@gmail.com', 'n07B0NvWJchGEKomzkPhrVjBBcQ=', 'eOBrddD0VU8=');


INSERT INTO pharmacy(name, phone_number, address, image_url) values('Farmashop', '1234567', 'Priamo 1579, Montevideo, Uruguay', 'https://pbs.twimg.com/profile_images/378800000563025154/432294d87567d7a4d1bf3416f626b904_400x400.png');
INSERT INTO pharmacy(name, phone_number, address, image_url) values('Farmacia Paris', '1234567', 'Belgrano 2874, Montevideo, Uruguay', 'https://i.vimeocdn.com/video/519867953_640.jpg');
INSERT INTO pharmacy(name, phone_number, address, image_url) values('San Roque', '1234567', 'Walcalde 2638, Montevideo, Uruguay', 'http://espectadornegocios.com/media/xcore//2554_1291335531_logo_san_roque.jpg');

INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (1,'Perifar 400', 'Calmante', 45, 'http://mlu-s2-p.mlstatic.com/perifar-400-espasmo-flex-15810-MLU20110477655_062014-O.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (1,'Cataflam 50', 'Calmante muy fuerte', 100, 'https://www.rxstars.net/wp-content/uploads/2015/04/Cataflam-50mg.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (2,'Domper MPS', 'Anti nauseas', 80, 'http://img.tradeindia.com/fp/1/001/005/765.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (2,'Aspirina 500', 'Calmante', 50 , 'http://www.blogdefarmacia.com/wp-content/uploads/2011/12/aspirinas1.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (2,'Perifar Flex', 'Calmante y relajante muscular', 50 , 'http://mlu-s2-p.mlstatic.com/15849-MLU20110476806_062014-O.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (3,'Perifar 400', 'Calmante', 48, 'http://mlu-s2-p.mlstatic.com/perifar-400-espasmo-flex-15810-MLU20110477655_062014-O.jpg');
