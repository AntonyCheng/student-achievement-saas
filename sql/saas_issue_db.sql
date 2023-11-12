/*
 Navicat Premium Data Transfer

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 12/11/2023 23:00:50
*/
CREATE database if NOT EXISTS `saas_issue_db` default character set utf8mb4 collate utf8mb4_general_ci;
use
    `saas_issue_db`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_tenant
-- ----------------------------
DROP TABLE IF EXISTS `t_tenant`;
CREATE TABLE `t_tenant`
(
    `tenant_id`         bigint UNSIGNED                                         NOT NULL COMMENT '租户ID',
    `tenant_account`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '租户账户，推荐手机号码',
    `like_account`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_password`   varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '租户密码',
    `tenant_name`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '租户负责人名字',
    `like_name`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_gender`     tinyint                                                 NOT NULL COMMENT '租户负责人性别（0表示女性，1表示男性）',
    `tenant_email`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户负责人邮箱',
    `like_email`        varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `tenant_picture`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户负责人照片URL',
    `like_picture`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `tenant_school`     varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '租户学校名称',
    `like_school`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_college`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NOT NULL COMMENT '租户学院名称',
    `like_college`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_address`    varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '租户学院地址',
    `like_address`      varchar(255) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `tenant_coordinate` varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL COMMENT '租户学院经纬度',
    `like_coordinate`   varchar(128) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `tenant_ip`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '租户容器IP地址',
    `like_ip`           varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_port`       varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '租户容器端口',
    `like_port`         varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_db_ip`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '租户数据库IP地址',
    `like_db_ip`        varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_db_port`    varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL COMMENT '租户数据库端口',
    `like_db_port`      varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci  NULL     DEFAULT NULL,
    `tenant_level`      tinyint                                                 NOT NULL DEFAULT 0 COMMENT '租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）',
    `tenant_status`     tinyint                                                 NOT NULL COMMENT '租户状态（0表示待审核，1表示启用，2表示审核未通过，3表示禁用）',
    `create_time`       timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`       timestamp                                               NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`        tinyint                                                 NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`tenant_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_token
-- ----------------------------
DROP TABLE IF EXISTS `t_token`;
CREATE TABLE `t_token`
(
    `token_id`       bigint                                                 NOT NULL COMMENT '用户登录ID',
    `token_account`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户账户，推荐手机号码',
    `like_account`   varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `token_password` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户密码',
    `token_tenant`   bigint                                                 NOT NULL COMMENT '用户所在的租户ID',
    `token_identity` varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NOT NULL COMMENT '用户身份（0表示学生，1表示老师，2表示租户负责人，3表示平台超级管理员）',
    `like_identity`  varchar(64) CHARACTER SET utf8 COLLATE utf8_general_ci NULL     DEFAULT NULL,
    `token_status`   tinyint                                                NOT NULL DEFAULT 0 COMMENT '租户状态（0表示启用，1表示禁用）',
    `create_time`    timestamp                                              NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp                                              NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     tinyint                                                NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`token_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8
  COLLATE = utf8_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_update
-- ----------------------------
DROP TABLE IF EXISTS `t_update`;
CREATE TABLE `t_update`
(
    `update_id`      bigint                                                        NOT NULL COMMENT '修改请求ID',
    `update_tenant`  bigint                                                        NOT NULL COMMENT '发送修改请求的租户ID',
    `update_account` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '修改后的管理员账号',
    `like_account`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `update_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '修改后的管理员姓名',
    `like_name`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `update_gender`  tinyint                                                       NOT NULL COMMENT '修改后的管理员性别',
    `update_email`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改后的管理员邮件',
    `like_email`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `update_picture` varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '修改后的管理员',
    `like_picture`   varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `update_level`   tinyint                                                       NOT NULL COMMENT '修改后的管理员照片URL',
    `update_status`  tinyint                                                       NOT NULL COMMENT '请求状态（0表示待审核，1表示审核通过，2表示审核不通过）',
    `create_time`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     tinyint UNSIGNED                                              NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`update_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
