CREATE DATABASE `achoo_dev`;

USE `achoo_dev`;

CREATE TABLE `user` (
  `id` INT UNSIGNED AUTO_INCREMENT NOT NULL PRIMARY KEY,
  `first_name` VARCHAR(30) NOT NULL,
  `last_name` VARCHAR(30) NOT NULL,
  `email` VARCHAR(80) NOT NULL,
  `password` CHAR(64) NOT NULL,
  `salt` CHAR(20) NOT NULL,
    UNIQUE INDEX (`email`)
);
