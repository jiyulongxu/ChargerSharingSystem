/*
Navicat MySQL Data Transfer

Source Server         : 本地
Source Server Version : 50720
Source Host           : 127.0.0.1:3306
Source Database       : powerbank

Target Server Type    : MYSQL
Target Server Version : 50720
File Encoding         : 65001

Date: 2018-07-16 15:44:31
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `admin`
-- ----------------------------
DROP TABLE IF EXISTS `admin`;
CREATE TABLE `admin` (
  `admin_id` int(11) NOT NULL AUTO_INCREMENT,
  `admin_account` varchar(16) NOT NULL,
  `admin_password` varchar(32) NOT NULL,
  PRIMARY KEY (`admin_id`),
  UNIQUE KEY `adminAccount` (`admin_account`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of admin
-- ----------------------------
INSERT INTO `admin` VALUES ('1', 'admin', '123456');
INSERT INTO `admin` VALUES ('2', 'admin2', '123456');
INSERT INTO `admin` VALUES ('3', 'admin3', '123456');
INSERT INTO `admin` VALUES ('4', 'admin4', '123654');
INSERT INTO `admin` VALUES ('5', 'asdfawe', 'asdfawefasdf');
INSERT INTO `admin` VALUES ('6', 'awefasdf6', '546456');
INSERT INTO `admin` VALUES ('7', 'asdfawefwa', 'asdfawefsdf');
INSERT INTO `admin` VALUES ('8', 'adfsdafawe', 'asdfawefasef');
INSERT INTO `admin` VALUES ('9', 'awefasdf', 'we23rwet');
INSERT INTO `admin` VALUES ('10', 'daf2345d', 'q432asdf');
INSERT INTO `admin` VALUES ('11', 'f2q35weddfasd', '254wefsdf');
INSERT INTO `admin` VALUES ('12', 'q25qw45', '456wrddfg');
INSERT INTO `admin` VALUES ('13', 'ikghj564d', 'dsf45');
INSERT INTO `admin` VALUES ('14', 'dchw457dfg', 'q34dsxdgr');
INSERT INTO `admin` VALUES ('15', '456dzfg3465', 'dxcv3456e');
INSERT INTO `admin` VALUES ('16', 'xa23', 'dsh67');
INSERT INTO `admin` VALUES ('17', '4523fd', 'fcgv47');
INSERT INTO `admin` VALUES ('18', 'x76', 'sa346r6yt');
INSERT INTO `admin` VALUES ('19', 'xcv746', 'a2ewa45dg5');
INSERT INTO `admin` VALUES ('20', 'as54q2345', '346cs5sc34');
INSERT INTO `admin` VALUES ('25', 'admin6', '12345678');

-- ----------------------------
-- Table structure for `location`
-- ----------------------------
DROP TABLE IF EXISTS `location`;
CREATE TABLE `location` (
  `location_id` int(11) NOT NULL AUTO_INCREMENT,
  `location_city` varchar(16) NOT NULL,
  `location_district` varchar(16) NOT NULL,
  `location_address` varchar(64) NOT NULL,
  `location_alias` varchar(32) NOT NULL,
  `location_yun_id` int(11) DEFAULT NULL,
  `location_amount` int(11) unsigned zerofill DEFAULT NULL,
  `location_available` int(11) unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`location_id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of location
-- ----------------------------
INSERT INTO `location` VALUES ('10', '广州市', '番禺区', '小谷围街道广东药科大学图书馆一楼淅水咖啡厅', '淅水咖啡厅', '16', '00000000016', '00000000009');
INSERT INTO `location` VALUES ('11', '广州市', '番禺区', '小谷围街道南国会国际会议中心', '南国会国际会议中心', '17', '00000000020', '00000000014');
INSERT INTO `location` VALUES ('13', '广州市', '番禺区', '华南理工大学南校区华南理工大学大学城校区广东省高等学校新能源技术重点实验室', '新能源技术实验室', '19', '00000000012', '00000000007');

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `order_id` int(11) NOT NULL AUTO_INCREMENT,
  `order_user_id` int(11) NOT NULL,
  `order_lent_location_id` int(11) NOT NULL,
  `order_pobk_id` int(11) NOT NULL,
  `order_create_time` datetime NOT NULL,
  `order_has_finished` int(2) unsigned zerofill DEFAULT NULL,
  `order_revert_location_id` int(11) DEFAULT NULL,
  `order_finish_time` datetime DEFAULT NULL,
  `order_cost` float DEFAULT NULL,
  PRIMARY KEY (`order_id`),
  KEY `order_user_id` (`order_user_id`) USING BTREE,
  KEY `order_lent_location_id` (`order_lent_location_id`) USING BTREE,
  KEY `order_pobk_id` (`order_pobk_id`) USING BTREE,
  KEY `order_revert_location_id` (`order_revert_location_id`) USING BTREE,
  CONSTRAINT `order_lent_location_id` FOREIGN KEY (`order_lent_location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_pobk_id` FOREIGN KEY (`order_pobk_id`) REFERENCES `pobk` (`pobk_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_revert_location_id` FOREIGN KEY (`order_revert_location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `order_user_id` FOREIGN KEY (`order_user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('3', '10', '11', '29', '2018-07-04 19:42:10', '01', '11', '2018-07-04 20:10:00', '0.5');
INSERT INTO `order` VALUES ('4', '10', '13', '44', '2018-07-05 01:53:37', '01', '10', '2018-07-05 03:41:10', '2.4');
INSERT INTO `order` VALUES ('5', '3', '11', '29', '2018-07-05 03:30:56', '01', '11', '2018-07-05 03:41:15', '0.6');
INSERT INTO `order` VALUES ('6', '3', '11', '29', '2018-07-05 03:33:21', '01', '13', '2018-07-05 03:41:20', '0.6');
INSERT INTO `order` VALUES ('7', '10', '13', '44', '2018-07-05 03:41:53', '01', '10', '2018-07-05 03:42:02', '0.6');
INSERT INTO `order` VALUES ('8', '3', '11', '29', '2018-07-05 11:09:44', '01', '11', '2018-07-05 11:10:02', '0.6');
INSERT INTO `order` VALUES ('9', '3', '13', '44', '2018-07-05 11:10:44', '01', '13', '2018-07-05 11:11:38', '0.6');
INSERT INTO `order` VALUES ('10', '3', '13', '44', '2018-07-05 11:33:01', '01', '10', '2018-07-05 11:35:22', '0.6');
INSERT INTO `order` VALUES ('11', '3', '11', '29', '2018-07-05 11:45:01', '01', '13', '2018-07-05 11:45:50', '0.6');
INSERT INTO `order` VALUES ('12', '3', '13', '44', '2018-07-11 08:38:26', '01', '13', '2018-07-11 08:38:53', '0.6');
INSERT INTO `order` VALUES ('13', '3', '10', '23', '2018-07-11 08:39:10', '01', '13', '2018-07-11 08:39:28', '0.6');
INSERT INTO `order` VALUES ('14', '3', '13', '44', '2018-07-11 09:21:18', '01', '10', '2018-07-11 09:21:38', '0.6');

-- ----------------------------
-- Table structure for `pobk`
-- ----------------------------
DROP TABLE IF EXISTS `pobk`;
CREATE TABLE `pobk` (
  `pobk_id` int(11) NOT NULL AUTO_INCREMENT,
  `pobk_location_id` int(11) NOT NULL,
  `pobk_status` enum('lent','available') NOT NULL DEFAULT 'available',
  PRIMARY KEY (`pobk_id`),
  KEY `pobk_location_key` (`pobk_location_id`),
  CONSTRAINT `pobk_location_key` FOREIGN KEY (`pobk_location_id`) REFERENCES `location` (`location_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=52 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of pobk
-- ----------------------------
INSERT INTO `pobk` VALUES ('23', '10', 'available');
INSERT INTO `pobk` VALUES ('24', '10', 'available');
INSERT INTO `pobk` VALUES ('25', '10', 'available');
INSERT INTO `pobk` VALUES ('26', '10', 'available');
INSERT INTO `pobk` VALUES ('27', '10', 'available');
INSERT INTO `pobk` VALUES ('28', '10', 'available');
INSERT INTO `pobk` VALUES ('29', '11', 'available');
INSERT INTO `pobk` VALUES ('30', '11', 'available');
INSERT INTO `pobk` VALUES ('31', '11', 'available');
INSERT INTO `pobk` VALUES ('32', '11', 'available');
INSERT INTO `pobk` VALUES ('33', '11', 'available');
INSERT INTO `pobk` VALUES ('34', '11', 'available');
INSERT INTO `pobk` VALUES ('35', '11', 'available');
INSERT INTO `pobk` VALUES ('36', '11', 'available');
INSERT INTO `pobk` VALUES ('37', '11', 'available');
INSERT INTO `pobk` VALUES ('38', '11', 'available');
INSERT INTO `pobk` VALUES ('39', '11', 'available');
INSERT INTO `pobk` VALUES ('40', '11', 'available');
INSERT INTO `pobk` VALUES ('41', '11', 'available');
INSERT INTO `pobk` VALUES ('42', '11', 'available');
INSERT INTO `pobk` VALUES ('43', '11', 'available');
INSERT INTO `pobk` VALUES ('44', '13', 'available');
INSERT INTO `pobk` VALUES ('45', '13', 'available');
INSERT INTO `pobk` VALUES ('46', '13', 'available');
INSERT INTO `pobk` VALUES ('47', '13', 'available');
INSERT INTO `pobk` VALUES ('48', '13', 'available');
INSERT INTO `pobk` VALUES ('49', '13', 'available');
INSERT INTO `pobk` VALUES ('50', '13', 'available');
INSERT INTO `pobk` VALUES ('51', '13', 'available');

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_phone` varchar(12) NOT NULL,
  `user_alias` varchar(20) DEFAULT NULL,
  `user_password` varchar(32) NOT NULL,
  `user_balance` float unsigned zerofill DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `userPhone` (`user_phone`) USING HASH
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('3', '15602209156', '阿周咯', '123456', '0000000121.8');
INSERT INTO `user` VALUES ('10', '13533388335', '测试昵称', '123456', '000000000057');
