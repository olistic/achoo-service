USE `achoo_dev`;
INSERT INTO user(first_name, last_name, email, password, salt) values('Mathias','Cabano', 'mathias@achoo.uy', 'CZ4i3Yh+wuthP5yqZRRFKAMmMBY=', 'gL6bdcesQOQ=');
INSERT INTO user(first_name, last_name, email, password, salt) values('Matias','Olivera', 'matias@achoo.uy', 'n07B0NvWJchGEKomzkPhrVjBBcQ=', 'eOBrddD0VU8=');
INSERT INTO user(first_name, last_name, email, password, salt) values('Diego','Muracciole', 'diego@achoo.uy', 'n07B0NvWJchGEKomzkPhrVjBBcQ=', 'eOBrddD0VU8=');
INSERT INTO user(first_name, last_name, email, password, salt) values('Alfredo','El Ters', 'alfredo@achoo.uy', 'n07B0NvWJchGEKomzkPhrVjBBcQ=', 'eOBrddD0VU8=');


INSERT INTO pharmacy(name, phone_number, address, image_url) values('Walgreens', '(415) 391-7222', '135 Powell St, San Francisco, CA', 'https://pbs.twimg.com/profile_images/467002768075395072/ya2By6bN.jpeg');
INSERT INTO pharmacy(name, phone_number, address, image_url) values('Northside Pharmacy', '(718) 387-6566', '559 Driggs Ave, Brooklyn, NY', 'http://www.wwjm.com/northside_pharmacies.jpg');
INSERT INTO pharmacy(name, phone_number, address, image_url) values('CVS Pharmacy', '(212) 982-3369', '158 Bleecker St, New York, NY', 'https://pbs.twimg.com/profile_images/514136773920710656/rssMCPXC_400x400.png');
INSERT INTO pharmacy(name, phone_number, address, image_url) values('Walgreens', '(773) 342-9161', '1601 N Milwaukee Ave, Chicago, IL', 'https://pbs.twimg.com/profile_images/467002768075395072/ya2By6bN.jpeg');

INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (1,'Xanax', 'Treats anxiety and panic disorder.', 50, 'http://www.enkivillage.com/s/upload/images/2015/05/23850247a55c59cb7540a3f8b8ccf699.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (1,'Hydrocodone', 'Hydrocodone is a semi-synthetic opioid synthesized from codeine, one of the opioid alkaloids found in the opium poppy.', 100, 'http://store.mcguff.com/Images/Images550/004591%20HydrocodoneApap%20%5BC-III%5D,%207.5%20per%20500,%20100%20Tablets%20per%20Bottle%20McGuffMedical.com.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (2,'Lisinopril', 'This medicine is an ACE inhibitor that treats high blood pressure and heart failure.', 100, 'http://www.drugsdb.com/images/2011/06/lisinopril-side-effects-e1331443452741.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (2,'Medrol', 'Treats asthma, allergic reactions, inflammation, flare-ups of ongoing illnesses, and many other medical problems. May also be used to lessen some symptoms of cancer. This medicine is a steroid.', 80, 'http://medicinesdelivery.com/image/cache/data/products/medrol30-500x500.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (3,'Xanax', 'Treats anxiety and panic disorder.', 50 , 'http://www.enkivillage.com/s/upload/images/2015/05/23850247a55c59cb7540a3f8b8ccf699.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (3,'Zoloft', 'This medicine is an SSRI that treats depression, anxiety, and other disorders.', 48, 'http://www.abclawcenters.com/wp-content/uploads/2014/11/Post_2013.08.02_zoloft-and-other-antidepressants-can-132366_1_zoloft.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (4,'Prozac', 'This medicine is an SSRI that treats depression, obsessive-compulsive disorder (OCD), and other disorders.', 50 , 'http://i.dailymail.co.uk/i/pix/2011/04/25/article-1380489-0057FE4500000258-812_468x341.jpg');
INSERT INTO product(pharmacy_id, name, description, unitary_price, image_url) values (4,'Zoloft', 'This medicine is an SSRI that treats depression, anxiety, and other disorders.', 48, 'http://www.abclawcenters.com/wp-content/uploads/2014/11/Post_2013.08.02_zoloft-and-other-antidepressants-can-132366_1_zoloft.jpg');
