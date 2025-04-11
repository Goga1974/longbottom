CREATE TABLE `user` (
    `user_id` VARCHAR(255) PRIMARY KEY,
    `user_name` VARCHAR(255) NOT NULL,
    `pass` VARCHAR(255),
    `data` JSON NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `item` (
    `item_id` VARCHAR(255) PRIMARY KEY,
    `item_name` VARCHAR(255) NOT NULL,
    `data` JSON NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

/* version 2*/

CREATE TABLE `user` (
                        `user_id` VARCHAR(255) PRIMARY KEY,
                        `user_name` VARCHAR(255),
                        `pass` VARCHAR(255),
                        `data` JSON
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `item` (
                        `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                        `item_id` VARCHAR(255),
                        `user_id` VARCHAR(255) NOT NULL,
                        `count` INT UNSIGNED DEFAULT 0,
                        INDEX (`item_id`),
                        INDEX (`user_id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `unlocked` (
                        `item_id` VARCHAR(255) PRIMARY KEY,
                        `user_id` VARCHAR(255) NOT NULL,
                        `count`  INT UNSIGNED DEFAULT 0
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


CREATE TABLE request_log (
                             id BIGINT AUTO_INCREMENT PRIMARY KEY,
                             user_id VARCHAR(255),
                             ip_address VARCHAR(50),
                             user_agent VARCHAR(255),
                             request_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

