ALTER TABLE `user`CHANGE `pass` `pin` varchar(255) DEFAULT NULL;

CREATE TABLE IF NOT EXISTS `install` (
    `id` bigint NOT NULL AUTO_INCREMENT,
    `user_id` varchar(255) NOT NULL,
    `install_id` varchar(255) DEFAULT NULL,
    `request_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`),
    KEY `user_id` (`user_id`),
    KEY `request_time` (`request_time`)
    ) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

