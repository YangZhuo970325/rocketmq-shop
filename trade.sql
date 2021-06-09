/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50731
 Source Host           : localhost:3306
 Source Schema         : trade

 Target Server Type    : MySQL
 Target Server Version : 50731
 File Encoding         : 65001

 Date: 09/06/2021 16:15:51
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for trade_coupon
-- ----------------------------
DROP TABLE IF EXISTS `trade_coupon`;
CREATE TABLE `trade_coupon`  (
  `coupon_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '优惠券id',
  `coupon_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券金额',
  `user_id` bigint(50) NULL DEFAULT NULL COMMENT '用户id',
  `order_id` bigint(50) NULL DEFAULT NULL COMMENT '订单id',
  `is_used` tinyint(1) NULL DEFAULT NULL COMMENT '是否使用',
  `used_time` timestamp(0) NULL DEFAULT NULL COMMENT '使用时间',
  PRIMARY KEY (`coupon_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 13211124 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_goods
-- ----------------------------
DROP TABLE IF EXISTS `trade_goods`;
CREATE TABLE `trade_goods`  (
  `goods_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '商品id',
  `goods_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品名称',
  `goods_number` int(11) NULL DEFAULT NULL COMMENT '商品库存',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `goods_desc` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '商品描述',
  `add_time` timestamp(0) NULL DEFAULT NULL COMMENT '添加时间',
  PRIMARY KEY (`goods_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 975315805 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_goods_number_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_goods_number_log`;
CREATE TABLE `trade_goods_number_log`  (
  `goods_id` bigint(11) NOT NULL COMMENT '商品id',
  `order_id` varchar(32) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '订单id',
  `goods_number` int(11) NULL DEFAULT NULL COMMENT '库存数量',
  `log_time` datetime(0) NULL DEFAULT NULL COMMENT '记录时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_mq_consumer_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_mq_consumer_log`;
CREATE TABLE `trade_mq_consumer_log`  (
  `msg_id` varchar(50) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息id',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '消费者组名',
  `msg_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'tag',
  `msg_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT 'key',
  `msg_body` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息体',
  `consumer_status` tinyint(1) NULL DEFAULT NULL COMMENT '0：正在处理\r\n1：处理成功\r\n2：处理失败',
  `consumer_times` tinyint(1) NULL DEFAULT NULL COMMENT '消费次数',
  `consumer_timestamp` timestamp(0) NULL DEFAULT NULL COMMENT '消费时间',
  `remark` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '备注'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_mq_producer_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_mq_producer_log`;
CREATE TABLE `trade_mq_producer_log`  (
  `id` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL COMMENT '主键',
  `group_name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '生产者组名',
  `msg_topic` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息主题',
  `msg_tag` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'tag',
  `msg_key` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT 'key',
  `msg_body` varchar(500) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '消息内容',
  `msg_status` tinyint(1) NULL DEFAULT NULL COMMENT '0：未处理\r\n1：已处理',
  `create_time` timestamp(0) NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP(0) COMMENT '记录时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_order
-- ----------------------------
DROP TABLE IF EXISTS `trade_order`;
CREATE TABLE `trade_order`  (
  `order_id` bigint(50) NOT NULL COMMENT '订单id',
  `user_id` bigint(50) NULL DEFAULT NULL COMMENT '用户id',
  `order_status` tinyint(1) NULL DEFAULT NULL COMMENT '订单状态\r\n0：未确认\r\n1：已确认\r\n2：已取消\r\n3：无效\r\n4：退款',
  `pay_status` tinyint(1) NULL DEFAULT NULL COMMENT '支付状态\r\n0：未支付\r\n1：支付中\r\n2：已支付',
  `shipping_status` tinyint(1) NULL DEFAULT NULL COMMENT '发货状态：\r\n0：未发货\r\n1：已发货\r\n2：已退货',
  `address` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '地址',
  `consignee` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '收货人',
  `goods_id` bigint(50) NULL DEFAULT NULL COMMENT '商品id',
  `goods_number` int(11) NULL DEFAULT NULL COMMENT '商品数量',
  `goods_price` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品价格',
  `goods_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '商品总价',
  `shipping_free` decimal(10, 2) NULL DEFAULT NULL COMMENT '运费',
  `order_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '订单价格',
  `coupon_id` bigint(50) NULL DEFAULT NULL COMMENT '优惠券id',
  `coupon_paid` decimal(10, 2) NULL DEFAULT NULL COMMENT '优惠券',
  `money_paid` decimal(10, 2) NULL DEFAULT NULL COMMENT '已付金额',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `add_time` timestamp(0) NULL DEFAULT NULL COMMENT '创建时间',
  `confirm_time` timestamp(0) NULL DEFAULT NULL COMMENT '订单确认时间',
  `pay_time` timestamp(0) NULL DEFAULT NULL COMMENT '支付时间',
  PRIMARY KEY (`order_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_pay
-- ----------------------------
DROP TABLE IF EXISTS `trade_pay`;
CREATE TABLE `trade_pay`  (
  `pay_id` bigint(50) NOT NULL COMMENT '支付编号',
  `order_id` bigint(50) NULL DEFAULT NULL COMMENT '支付编号',
  `pay_amount` decimal(10, 2) NULL DEFAULT NULL COMMENT '支付金额',
  `is_paid` tinyint(1) NULL DEFAULT NULL COMMENT '是否已支付\r\n1：否\r\n2：是',
  PRIMARY KEY (`pay_id`) USING BTREE
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_user
-- ----------------------------
DROP TABLE IF EXISTS `trade_user`;
CREATE TABLE `trade_user`  (
  `user_id` bigint(50) NOT NULL AUTO_INCREMENT COMMENT '用户id',
  `user_name` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户姓名',
  `user_password` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '用户密码',
  `user_mobile` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NULL DEFAULT NULL COMMENT '手机号',
  `user_score` int(11) NULL DEFAULT NULL COMMENT '积分',
  `user_reg_time` timestamp(0) NULL DEFAULT NULL COMMENT '注册时间',
  `user_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '用户余额',
  PRIMARY KEY (`user_id`) USING BTREE
) ENGINE = InnoDB AUTO_INCREMENT = 3131231244 CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

-- ----------------------------
-- Table structure for trade_user_money_log
-- ----------------------------
DROP TABLE IF EXISTS `trade_user_money_log`;
CREATE TABLE `trade_user_money_log`  (
  `user_id` bigint(50) NOT NULL COMMENT '用户id',
  `order_id` bigint(50) NOT NULL COMMENT '订单id',
  `money_log_type` tinyint(1) NOT NULL COMMENT '日志操作\r\n1：订单付款\r\n2：订单退款',
  `use_money` decimal(10, 2) NULL DEFAULT NULL COMMENT '操作金额',
  `create_time` timestamp(0) NULL DEFAULT NULL COMMENT '日志时间'
) ENGINE = InnoDB CHARACTER SET = utf8mb4 COLLATE = utf8mb4_bin ROW_FORMAT = Dynamic;

SET FOREIGN_KEY_CHECKS = 1;
