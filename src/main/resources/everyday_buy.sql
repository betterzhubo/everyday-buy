/*
Navicat MySQL Data Transfer

Source Server         : localhost
Source Server Version : 50726
Source Host           : localhost:3306
Source Database       : everyday_buy

Target Server Type    : MYSQL
Target Server Version : 50726
File Encoding         : 65001

Date: 2019-12-23 18:59:22
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for `goods`
-- ----------------------------
DROP TABLE IF EXISTS `goods`;
CREATE TABLE `goods` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_name` varchar(128) NOT NULL DEFAULT '',
  `purpose` tinyint(3) NOT NULL DEFAULT '0' COMMENT '0:餐饮,1:自用',
  `price` decimal(6,2) NOT NULL,
  `goods_type_id` int(11) NOT NULL DEFAULT '0',
  `show_image` varchar(1023) NOT NULL DEFAULT '' COMMENT '展示图片',
  `detail_image` varchar(1023) NOT NULL DEFAULT '' COMMENT '详情内图片',
  `selling` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否销售中',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of goods
-- ----------------------------
INSERT INTO `goods` VALUES ('1', '酱油', '0', '6.50', '1', 'mockup6.jpg', '', '');
INSERT INTO `goods` VALUES ('2', '醋', '1', '3.00', '1', 'map-marker-violet.png', '', '');

-- ----------------------------
-- Table structure for `goods_images`
-- ----------------------------
DROP TABLE IF EXISTS `goods_images`;
CREATE TABLE `goods_images` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_id` int(11) NOT NULL DEFAULT '0',
  `images_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '图片类型1:轮播图片,2:详情图片',
  `images_url` varchar(127) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of goods_images
-- ----------------------------
INSERT INTO `goods_images` VALUES ('1', '1', '2', 'image/avatar-4.jpg');
INSERT INTO `goods_images` VALUES ('2', '1', '2', 'image/avatar-8.jpg');
INSERT INTO `goods_images` VALUES ('3', '1', '1', 'image/mockup1.jpg');

-- ----------------------------
-- Table structure for `goods_type`
-- ----------------------------
DROP TABLE IF EXISTS `goods_type`;
CREATE TABLE `goods_type` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `goods_type` varchar(127) NOT NULL DEFAULT '',
  `goods_sub_type` varchar(127) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of goods_type
-- ----------------------------
INSERT INTO `goods_type` VALUES ('1', '调味品', '调味品');
INSERT INTO `goods_type` VALUES ('2', '干货', '干货');
INSERT INTO `goods_type` VALUES ('5', '干货122', '');

-- ----------------------------
-- Table structure for `order`
-- ----------------------------
DROP TABLE IF EXISTS `order`;
CREATE TABLE `order` (
  `id` int(11) NOT NULL,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `consignee_name` varchar(64) NOT NULL DEFAULT '',
  `consignee_mobile` varchar(16) NOT NULL DEFAULT '',
  `order_status` int(11) NOT NULL DEFAULT '0',
  `consignee_address` varchar(255) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of order
-- ----------------------------
INSERT INTO `order` VALUES ('2147483647', '1', '张三', '18640004000', '1', '辽宁省沈阳市和平区', '2019-09-16 11:02:09', '2019-09-12 15:57:09');

-- ----------------------------
-- Table structure for `order_goods`
-- ----------------------------
DROP TABLE IF EXISTS `order_goods`;
CREATE TABLE `order_goods` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `order_id` int(11) NOT NULL DEFAULT '0',
  `goods_id` int(11) NOT NULL DEFAULT '0',
  `goods_name` varchar(128) NOT NULL DEFAULT '',
  `goods_count` int(11) NOT NULL DEFAULT '0',
  `goods_price` decimal(6,2) NOT NULL,
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of order_goods
-- ----------------------------
INSERT INTO `order_goods` VALUES ('1', '2147483647', '2', '醋', '2', '6.00', '2019-09-12 16:03:29', '2019-09-12 16:03:29');
INSERT INTO `order_goods` VALUES ('2', '2147483647', '1', '酱油', '4', '12.00', '2019-09-12 17:25:11', '2019-09-12 17:25:11');

-- ----------------------------
-- Table structure for `shipping_address`
-- ----------------------------
DROP TABLE IF EXISTS `shipping_address`;
CREATE TABLE `shipping_address` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `consignee_area` varchar(64) NOT NULL DEFAULT '' COMMENT '收货人地区',
  `consignee_address` varchar(255) NOT NULL DEFAULT '' COMMENT '收货人地址',
  `consignee_name` varchar(20) NOT NULL DEFAULT '' COMMENT '收货人',
  `consignee_mobile` varchar(20) NOT NULL DEFAULT '' COMMENT '收货人电话',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- ----------------------------
-- Records of shipping_address
-- ----------------------------

-- ----------------------------
-- Table structure for `shopping_cart`
-- ----------------------------
DROP TABLE IF EXISTS `shopping_cart`;
CREATE TABLE `shopping_cart` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL DEFAULT '0',
  `goods_id` int(11) NOT NULL DEFAULT '0',
  `goods_count` int(11) NOT NULL DEFAULT '0',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of shopping_cart
-- ----------------------------

-- ----------------------------
-- Table structure for `user`
-- ----------------------------
DROP TABLE IF EXISTS `user`;
CREATE TABLE `user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL DEFAULT '',
  `phone` varchar(16) NOT NULL DEFAULT '',
  `openid` varchar(128) NOT NULL DEFAULT '',
  `password` varchar(128) NOT NULL DEFAULT '',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8 ROW_FORMAT=COMPACT;

-- ----------------------------
-- Records of user
-- ----------------------------
INSERT INTO `user` VALUES ('1', 'aaa', '', '', 'bbb', '2019-08-21 10:34:53', '2019-08-21 10:34:53');
