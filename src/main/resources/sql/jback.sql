CREATE TABLE `user` (
                            `user_id` BIGINT AUTO_INCREMENT PRIMARY KEY,
                            `user_name` DATE NOT NULL UNIQUE,
                            `data` JSON NOT NULL
) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
