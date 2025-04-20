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
    `count` INT DEFAULT 0,
    INDEX (`item_id`),
    INDEX (`user_id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `unlocked` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `item_id` VARCHAR(255),
    `user_id` VARCHAR(255) NOT NULL,
    `count` INT DEFAULT 1,
    INDEX (`item_id`),
    INDEX (`user_id`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE `request_log` (
    `id` BIGINT AUTO_INCREMENT PRIMARY KEY,
    `user_id` VARCHAR(255),
    `ip_address` VARCHAR(50),
    `user_agent` VARCHAR(255),
    `request_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    INDEX (`user_id`),
    INDEX (`ip_address`),
    INDEX (`request_time`)
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;

CREATE TABLE token (
    user_id VARCHAR(255) PRIMARY KEY,
    token VARCHAR(512) NOT NULL,
    created_at DATETIME NOT NULL,
    expires_at DATETIME NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;


