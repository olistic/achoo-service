DROP DATABASE IF EXISTS `achoo_dev`;

CREATE DATABASE `achoo_dev`;

USE `achoo_dev`;

CREATE TABLE `user` (
  `id`          INT         NOT NULL AUTO_INCREMENT,
  `first_name`  VARCHAR(30) NOT NULL,
  `last_name`   VARCHAR(30) NOT NULL,
  `address`     VARCHAR(45) NOT NULL,
  `email`       VARCHAR(80) NOT NULL,
  `password`    CHAR(64)    NOT NULL,
  `salt`        CHAR(20)    NOT NULL,
  UNIQUE INDEX (`email`),
  PRIMARY KEY (`id`)
);

CREATE TABLE `pharmacy` (
  `id`            INT           NOT NULL AUTO_INCREMENT,
  `name`          VARCHAR(45)   NOT NULL,
  `phone_number`  VARCHAR(15)   NULL,
  `address`       VARCHAR(45)   NOT NULL,
  `image_url`     VARCHAR(200)  NULL,
  INDEX (`name` ASC),
  PRIMARY KEY (`id`)
);

CREATE TABLE `product` (
  `id`            INT           NOT NULL AUTO_INCREMENT,
  `pharmacy_id`   INT           NOT NULL,
  `name`          VARCHAR(45)   NOT NULL,
  `description`   VARCHAR(500)  NOT NULL,
  `unitary_price` DOUBLE        NOT NULL,
  `image_url`     VARCHAR(200)  NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_products_pharmacy`
  FOREIGN KEY (`pharmacy_id`)
  REFERENCES `pharmacy` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  INDEX `fk_products_pharmacy_idx` (`pharmacy_id` ASC),
  INDEX (`name` ASC)
);

CREATE TABLE `order` (
  `id`          INT       NOT NULL AUTO_INCREMENT,
  `user_id`     INT       NOT NULL,
  `pharmacy_id` INT       NOT NULL,
  `date`        TIMESTAMP NOT NULL,
  `score`       INT       NULL,
  PRIMARY KEY (`id`),
  CONSTRAINT `fk_orders_pharmacy`
  FOREIGN KEY (`pharmacy_id`)
  REFERENCES `pharmacy` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_orders_user`
  FOREIGN KEY (`user_id`)
  REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  INDEX `fk_orders_pharmacy_idx` (`pharmacy_id` ASC)
);

CREATE TABLE `order_line` (
  `order_id`    INT NOT NULL,
  `product_id`  INT NOT NULL,
  `quantity`    INT NOT NULL,
  PRIMARY KEY (`order_id`, `product_id`),
  CONSTRAINT `fk_order_lines_products`
  FOREIGN KEY (`product_id`)
  REFERENCES `product` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_order_lines_orders`
  FOREIGN KEY (`order_id`)
  REFERENCES `order` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  INDEX `fk_order_lines_products_idx` (`product_id` ASC)
);
