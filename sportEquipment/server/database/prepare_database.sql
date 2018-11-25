-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema sport_equipment
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema sport_equipment
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `sport_equipment` DEFAULT CHARACTER SET utf8 ;
USE `sport_equipment` ;

-- -----------------------------------------------------
-- Table `sport_equipment`.`account`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sport_equipment`.`account` (
  `contact_id` INT NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `role` VARCHAR(45) NULL DEFAULT 'GUEST',
  PRIMARY KEY (`contact_id`),
  UNIQUE INDEX `contact_id_UNIQUE` (`contact_id` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sport_equipment`.`skates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sport_equipment`.`skates` (
  `skates_id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL,
  `size` INT NULL,
  `cost_per_hour` DECIMAL(10,2) NULL,
  `booked_from` DATETIME NULL,
  `booked_to` DATETIME NULL,
  PRIMARY KEY (`skates_id`),
  UNIQUE INDEX `skates_id_UNIQUE` (`skates_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sport_equipment`.`stick`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sport_equipment`.`stick` (
  `stick_id` INT NOT NULL AUTO_INCREMENT,
  `type` VARCHAR(45) NULL,
  `cost_per_hour` DECIMAL(10,2) NULL,
  `booked_from` DATETIME NULL,
  `booked_to` DATETIME NULL,
  PRIMARY KEY (`stick_id`),
  UNIQUE INDEX `stick_id_UNIQUE` (`stick_id` ASC))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sport_equipment`.`account_has_stick`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sport_equipment`.`account_has_stick` (
  `account_contact_id` INT NOT NULL,
  `stick_stick_id` INT NOT NULL,
  PRIMARY KEY (`account_contact_id`, `stick_stick_id`),
  INDEX `fk_account_has_Stick_Stick1_idx` (`stick_stick_id` ASC),
  INDEX `fk_account_has_Stick_account_idx` (`account_contact_id` ASC),
  CONSTRAINT `fk_account_has_Stick_account`
    FOREIGN KEY (`account_contact_id`)
    REFERENCES `sport_equipment`.`account` (`contact_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_account_has_Stick_Stick1`
    FOREIGN KEY (`stick_stick_id`)
    REFERENCES `sport_equipment`.`stick` (`stick_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `sport_equipment`.`account_has_skates`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `sport_equipment`.`account_has_skates` (
  `account_contact_id` INT NOT NULL,
  `skates_skates_id` INT NOT NULL,
  PRIMARY KEY (`account_contact_id`, `skates_skates_id`),
  INDEX `fk_account_has_skates_skates1_idx` (`skates_skates_id` ASC),
  INDEX `fk_account_has_skates_account1_idx` (`account_contact_id` ASC),
  CONSTRAINT `fk_account_has_skates_account1`
    FOREIGN KEY (`account_contact_id`)
    REFERENCES `sport_equipment`.`account` (`contact_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE,
  CONSTRAINT `fk_account_has_skates_skates1`
    FOREIGN KEY (`skates_skates_id`)
    REFERENCES `sport_equipment`.`skates` (`skates_id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

-- -----------------------------------------------------
-- Data for table `sport_equipment`.`account`
-- -----------------------------------------------------
START TRANSACTION;
USE `sport_equipment`;
INSERT INTO `sport_equipment`.`account` (`contact_id`, `first_name`, `last_name`, `email`, `password`, `role`) VALUES (1, 'Ivan', 'Ivanov', 'ivan@mail.ru', '1111', 'ADMIN');
INSERT INTO `sport_equipment`.`account` (`contact_id`, `first_name`, `last_name`, `email`, `password`, `role`) VALUES (2, 'Petya', 'Petrov', 'petya@mail.ru', '1111', 'USER');

COMMIT;


-- -----------------------------------------------------
-- Data for table `sport_equipment`.`skates`
-- -----------------------------------------------------
START TRANSACTION;
USE `sport_equipment`;
INSERT INTO `sport_equipment`.`skates` (`skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (1, 'FIGURE', 38, 10, NULL, NULL);
INSERT INTO `sport_equipment`.`skates` (`skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (2, 'ICE_HOCKEY', 40, 8, NULL, NULL);
INSERT INTO `sport_equipment`.`skates` (`skates_id`, `type`, `size`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (3, 'ICE_HOCKEY', 43, 8, NULL, NULL);

COMMIT;


-- -----------------------------------------------------
-- Data for table `sport_equipment`.`stick`
-- -----------------------------------------------------
START TRANSACTION;
USE `sport_equipment`;
INSERT INTO `sport_equipment`.`stick` (`stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (1, 'ICE_HOCKEY', 2, NULL, NULL);
INSERT INTO `sport_equipment`.`stick` (`stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (2, 'ICE_HOCKEY', 5, NULL, NULL);
INSERT INTO `sport_equipment`.`stick` (`stick_id`, `type`, `cost_per_hour`, `booked_from`, `booked_to`) VALUES (3, 'ROLLER_HOCKEY', 3, NULL, NULL);

COMMIT;

