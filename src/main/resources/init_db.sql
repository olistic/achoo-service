CREATE DATABASE IF NOT EXISTS `achoo_dev`;

USE `achoo_dev`;

CREATE TABLE IF NOT EXISTS `user` (
  `id`         INT AUTO_INCREMENT NOT NULL,
  `first_name` VARCHAR(30)        NOT NULL,
  `last_name`  VARCHAR(30)        NOT NULL,
  `email`      VARCHAR(80)        NOT NULL,
  `password`   CHAR(64)           NOT NULL,
  `salt`       CHAR(20)           NOT NULL,
  UNIQUE INDEX (`email`),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `drugstore` (
  `id`           INT         NOT NULL AUTO_INCREMENT,
  `name`         VARCHAR(45) NOT NULL,
  `phone_number` VARCHAR(15) NULL,
  `address`      VARCHAR(45) NOT NULL,
  `image_url`    VARCHAR(200)  NULL,
  INDEX (`name` ASC),
  PRIMARY KEY (`id`)
);

CREATE TABLE IF NOT EXISTS `product` (
  `product_id`            INT          NOT NULL AUTO_INCREMENT,
  `drugstore_id`          INT          NOT NULL,
  `product_name`          VARCHAR(45)  NOT NULL,
  `product_description`   VARCHAR(500) NOT NULL,
  `product_unitary_price` DOUBLE       NOT NULL,
  `product_image_url`          VARCHAR(200)  NULL,
  INDEX `fk_products_drugstore_idx` (`drugstore_id` ASC),
  INDEX (`product_name` ASC),
  PRIMARY KEY (`product_id`),
  CONSTRAINT `fk_products_drugstore`
  FOREIGN KEY (`drugstore_id`)
  REFERENCES `drugstore` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `order` (
  `id`           INT       NOT NULL AUTO_INCREMENT,
  `user_id`      INT       NOT NULL,
  `drugstore_id` INT       NOT NULL,
  `date`         TIMESTAMP NOT NULL,
  `score`        INT       NULL,
  INDEX `fk_orders_drugstore_idx` (`drugstore_id` ASC),
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_orders_drugstore`
  FOREIGN KEY (`drugstore_id`)
  REFERENCES `drugstore` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_user`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);

CREATE TABLE IF NOT EXISTS `order_line` (
  `order_id`   INT NOT NULL,
  `product_id` INT NOT NULL,
  `amount`     INT NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  INDEX `fk_order_lines_products_idx` (`product_id` ASC),
  CONSTRAINT `fk_order_lines_products`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`product_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_lines_orders`
  FOREIGN KEY (`order_id`)
  REFERENCES `order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION
);
