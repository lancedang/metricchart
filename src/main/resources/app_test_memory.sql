-- ----------------------------
-- Table structure for app_test_cpu
-- ----------------------------
DROP TABLE IF EXISTS `app_test_cpu`;
CREATE TABLE `app_test_cpu` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `package_name` varchar(60) NOT NULL,
  `process_id` varchar(10) DEFAULT '0',
  `cpu_percent` double(12,2) NOT NULL,
  `create_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=274 DEFAULT CHARSET=utf8mb4;