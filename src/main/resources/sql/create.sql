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

