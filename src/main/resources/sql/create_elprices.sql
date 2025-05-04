CREATE DATABASE IF NOT EXISTS `elprices`
       /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `elprices`;

-- dump
CREATE TABLE IF NOT EXISTS `price_data` (
                                            `id` bigint NOT NULL AUTO_INCREMENT,
                                            `date` date NOT NULL,
                                            `json_data` json NOT NULL,
                                            PRIMARY KEY (`id`),
    UNIQUE KEY `date` (`date`)
    ) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


CREATE USER 'eluser'@'%' IDENTIFIED BY 'xxx';
GRANT USAGE ON *.* TO 'xxx'@'%';
GRANT SELECT, DELETE, UPDATE, INSERT, LOCK TABLES, EXECUTE, SHOW VIEW, CREATE TEMPORARY TABLES, TRIGGER  ON `elprices`.* TO 'eluser'@'%';
FLUSH PRIVILEGES;
SHOW GRANTS FOR 'eluser'@'%';
SHOW CREATE USER 'eluser'@'%';
