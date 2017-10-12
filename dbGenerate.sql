-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema TP_AM_DB
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema TP_AM_DB
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `TP_AM_DB` DEFAULT CHARACTER SET utf8 ;
USE `TP_AM_DB` ;

-- -----------------------------------------------------
-- Table `TP_AM_DB`.`admin`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TP_AM_DB`.`admin` (
  `id_admin` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(64) NOT NULL,
  `active` TINYINT(1) NOT NULL DEFAULT '1',
  PRIMARY KEY (`id_admin`))
ENGINE = InnoDB
AUTO_INCREMENT = 3
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `TP_AM_DB`.`androidUsers`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TP_AM_DB`.`androidUsers` (
  `idandroidUsers` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NULL DEFAULT NULL,
  `password` VARCHAR(64) NULL DEFAULT NULL,
  `active` TINYINT(1) NULL DEFAULT '1',
  PRIMARY KEY (`idandroidUsers`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `TP_AM_DB`.`softwareAgents`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TP_AM_DB`.`softwareAgents` (
  `id_sa` INT(11) NOT NULL AUTO_INCREMENT,
  `device_name` VARCHAR(100) NOT NULL,
  `device_ip` VARCHAR(45) NOT NULL,
  `device_mac_address` VARCHAR(90) NOT NULL,
  `os_version` VARCHAR(90) NOT NULL,
  `nmap_version` VARCHAR(45) NOT NULL,
  `hash` INT(11) NOT NULL,
  `active` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id_sa`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `TP_AM_DB`.`jobs`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TP_AM_DB`.`jobs` (
  `id_jobs` INT(11) NOT NULL AUTO_INCREMENT,
  `parameters` VARCHAR(255) NULL DEFAULT NULL,
  `periodic` TINYINT(1) NULL DEFAULT NULL,
  `time` INT(11) NULL DEFAULT NULL,
  `status` VARCHAR(45) NULL DEFAULT NULL,
  `softwareAgents_id_sa` INT(11) NOT NULL,
  `id` INT(11) NOT NULL,
  PRIMARY KEY (`id_jobs`),
  INDEX `fk_jobs_softwareAgents_idx` (`softwareAgents_id_sa` ASC),
  CONSTRAINT `fk_jobs_softwareAgents`
    FOREIGN KEY (`softwareAgents_id_sa`)
    REFERENCES `TP_AM_DB`.`softwareAgents` (`id_sa`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `TP_AM_DB`.`results`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `TP_AM_DB`.`results` (
  `id_result` INT(11) NOT NULL AUTO_INCREMENT,
  `xml` LONGTEXT NOT NULL,
  `jobs_id_jobs` INT(11) NOT NULL,
  `date_created` TIMESTAMP NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id_result`, `jobs_id_jobs`),
  INDEX `fk_results_jobs1_idx` (`jobs_id_jobs` ASC),
  CONSTRAINT `fk_results_jobs1`
    FOREIGN KEY (`jobs_id_jobs`)
    REFERENCES `TP_AM_DB`.`jobs` (`id_jobs`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;

INSERT INTO admin (username,password,active)
VALUES ('admin','8c6976e5b5410415bde908bd4dee15dfb167a9c873fc4bb8a81f6f2ab448a918','1');

