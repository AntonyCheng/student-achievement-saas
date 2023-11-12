/*
 Navicat Premium Data Transfer

 Target Server Type    : MySQL
 Target Server Version : 80028 (8.0.28)
 File Encoding         : 65001

 Date: 12/11/2023 23:00:59
*/
CREATE database if NOT EXISTS `sass_user_db` default character set utf8mb4 collate utf8mb4_general_ci;
use
    `sass_user_db`;

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for t_achievement
-- ----------------------------
DROP TABLE IF EXISTS `t_achievement`;
CREATE TABLE `t_achievement`
(
    `achievement_id`           bigint                                                         NOT NULL COMMENT '成果ID',
    `achievement_type_id`      bigint                                                         NOT NULL COMMENT '成果类型ID',
    `achievement_type_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '成果类型名称',
    `like_type_name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `achievement_author_id`    bigint                                                         NOT NULL COMMENT '成果第一作者ID',
    `achievement_author_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '成果第一作者姓名',
    `like_author_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `achievement_teacher_id`   bigint                                                         NULL     DEFAULT NULL COMMENT '成果指导老师ID',
    `achievement_teacher_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '成果指导老师姓名',
    `like_teacher_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `achievement_censor_id`    bigint                                                         NOT NULL COMMENT '成果审核老师ID',
    `achievement_censor_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '成果审核老师姓名',
    `like_censor_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `achievement_members`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '成果其他成员JSON字段（包含每个成员的ID和姓名）',
    `like_members`             varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `achievement_evidence`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '成果佐证材料URL',
    `like_evidence`            varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `achievement_overview`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '成果概述',
    `like_overview`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `achievement_feedback`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '成果审核反馈',
    `like_feedback`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `achievement_status`       tinyint                                                        NOT NULL COMMENT '成果审核状态（0表示待审核，1表示审核通过，2表示审核不通过）',
    `create_time`              timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`               tinyint                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`achievement_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_achievement_type
-- ----------------------------
DROP TABLE IF EXISTS `t_achievement_type`;
CREATE TABLE `t_achievement_type`
(
    `achievement_type_id`   bigint                                                       NOT NULL COMMENT '成果类型ID',
    `achievement_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '成果类型名称',
    `like_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`           timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`            tinyint                                                      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`achievement_type_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_competition
-- ----------------------------
DROP TABLE IF EXISTS `t_competition`;
CREATE TABLE `t_competition`
(
    `competition_id`           bigint                                                         NOT NULL COMMENT '竞赛ID',
    `competition_type_id`      bigint                                                         NOT NULL COMMENT '竞赛类型ID',
    `competition_type_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '竞赛类型名称',
    `like_type_name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `competition_author_id`    bigint                                                         NOT NULL COMMENT '竞赛第一作者ID',
    `competition_author_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '竞赛第一作者姓名',
    `like_author_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `competition_teacher_id`   bigint                                                         NULL     DEFAULT NULL COMMENT '竞赛指导老师ID',
    `competition_teacher_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '竞赛指导老师姓名',
    `like_teacher_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `competition_censor_id`    bigint                                                         NOT NULL COMMENT '竞赛审核老师ID',
    `competition_censor_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '竞赛审核老师姓名',
    `like_censor_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `competition_members`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '竞赛其他成员JSON字段（包含每个成员的ID和姓名）',
    `like_members`             varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `competition_evidence`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '竞赛佐证材料URL',
    `like_evidence`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `competition_overview`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '竞赛概述',
    `like_overview`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `competition_feedback`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '竞赛审核反馈',
    `like_feedback`            varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `competition_status`       tinyint                                                        NOT NULL COMMENT '竞赛审核状态（0表示待审核，1表示审核通过，2表示审核不通过）',
    `create_time`              timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`              timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`               tinyint                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`competition_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_competition_type
-- ----------------------------
DROP TABLE IF EXISTS `t_competition_type`;
CREATE TABLE `t_competition_type`
(
    `competition_type_id`   bigint                                                       NOT NULL COMMENT '竞赛类型ID',
    `competition_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '竞赛类型名称',
    `like_name`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`           timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`           timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`            tinyint                                                      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`competition_type_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honor
-- ----------------------------
DROP TABLE IF EXISTS `t_honor`;
CREATE TABLE `t_honor`
(
    `honor_id`           bigint                                                         NOT NULL COMMENT '荣誉ID',
    `honor_type_id`      bigint                                                         NOT NULL COMMENT '荣誉类型ID',
    `honor_type_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '荣誉类型名称',
    `like_type_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `honor_author_id`    bigint                                                         NOT NULL COMMENT '荣誉第一作者ID',
    `honor_author_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '荣誉第一作者姓名',
    `like_author_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `honor_teacher_id`   bigint                                                         NULL     DEFAULT NULL COMMENT '荣誉指导老师ID',
    `honor_teacher_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '荣誉指导老师姓名',
    `like_teacher_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `honor_censor_id`    bigint                                                         NOT NULL COMMENT '荣誉审核老师ID',
    `honor_censor_name`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '荣誉审核老师姓名',
    `like_censor_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `honor_members`      varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '荣誉其他成员JSON字段（包含每个成员的ID和姓名）',
    `like_members`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `honor_evidence`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '荣誉佐证材料URL',
    `like_evidence`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `honor_overview`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '荣誉概述',
    `like_overview`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `honor_feedback`     varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL COMMENT '荣誉审核反馈',
    `like_feedback`      varchar(512) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `honor_status`       tinyint                                                        NOT NULL COMMENT '荣誉审核状态（0表示待审核，1表示审核通过，2表示审核不通过）',
    `create_time`        timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`        timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`         tinyint                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`honor_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_honor_type
-- ----------------------------
DROP TABLE IF EXISTS `t_honor_type`;
CREATE TABLE `t_honor_type`
(
    `honor_type_id`   bigint                                                       NOT NULL COMMENT '荣誉类型ID',
    `honor_type_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '荣誉类型名称',
    `like_name`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`     timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`      tinyint                                                      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`honor_type_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_major
-- ----------------------------
DROP TABLE IF EXISTS `t_major`;
CREATE TABLE `t_major`
(
    `major_id`      bigint                                                        NOT NULL COMMENT '专业ID',
    `major_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '专业名称',
    `like_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `major_grade`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '专业年级',
    `major_classes` varchar(128) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '专业班级',
    `create_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`   timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`    tinyint                                                       NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`major_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_manager
-- ----------------------------
DROP TABLE IF EXISTS `t_manager`;
CREATE TABLE `t_manager`
(
    `manager_id`       bigint                                                        NOT NULL COMMENT '管理员ID',
    `manager_tenant`   bigint                                                        NOT NULL COMMENT '管理员所在租户ID',
    `manager_account`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '管理员账号',
    `like_account`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `manager_password` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '管理员密码',
    `manager_name`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '管理员姓名',
    `like_name`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `manager_gender`   tinyint                                                       NOT NULL COMMENT '管理员性别（0表示女性，1表示男性）',
    `manager_email`    varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员邮件',
    `like_email`       varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `manager_picture`  varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '管理员图片',
    `like_picture`     varchar(255) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `manager_school`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '管理员所在学校',
    `like_school`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `manager_college`  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NOT NULL COMMENT '管理员所在学院',
    `like_college`     varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci  NULL     DEFAULT NULL,
    `manager_level`    tinyint                                                       NOT NULL COMMENT '管理员所在租户等级（0表示0-500，1表示501-1000，2表示1001-1500，3表示1501-2000，4表示2000人以上）',
    `create_time`      timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`      timestamp                                                     NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`       tinyint                                                       NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`manager_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_student
-- ----------------------------
DROP TABLE IF EXISTS `t_student`;
CREATE TABLE `t_student`
(
    `student_id`                 bigint                                                         NOT NULL COMMENT '学生ID',
    `student_account`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生账号',
    `like_account`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_password`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生密码',
    `student_name`               varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生姓名',
    `like_name`                  varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_gender`             tinyint                                                        NOT NULL COMMENT '学生性别（0表示女性，1表示男性）',
    `student_major_id`           bigint                                                         NOT NULL COMMENT '学生专业ID',
    `student_major_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生专业名称',
    `like_major_name`            varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_grade_name`         varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生年级名称',
    `student_class_number`       tinyint                                                        NULL     DEFAULT NULL COMMENT '学生所在班级',
    `student_phone`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '学生电话',
    `like_phone`                 varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_teacher_id`         bigint                                                         NULL     DEFAULT NULL COMMENT '学生导员ID',
    `student_teacher_name`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '学生导员姓名',
    `like_teacher_name`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_pro_teacher_id`     bigint                                                         NULL     DEFAULT NULL COMMENT '学生指导老师ID',
    `student_pro_teacher_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '学生指导老师姓名',
    `like_pro_teacher_name`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `student_status`             tinyint                                                        NOT NULL COMMENT '学生状态(0表示正常，1表示转专业，2表示参军，3表示离校)',
    `student_status_description` varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '学生状态描述',
    `like_status_description`    varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`                timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`                timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`                 tinyint                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`student_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_teacher
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher`;
CREATE TABLE `t_teacher`
(
    `teacher_id`             bigint                                                         NOT NULL COMMENT '教师ID',
    `teacher_account`        varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '教师账户',
    `like_account`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `teacher_password`       varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '教师密码',
    `teacher_name`           varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '教师姓名',
    `like_name`              varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `teacher_gender`         tinyint                                                        NOT NULL COMMENT '教师性别（0表示女性，1表示男性）',
    `teacher_phone`          varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NOT NULL COMMENT '教师电话',
    `like_phone`             varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `teacher_role_ids`       varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL COMMENT '教师角色ID，名称 JSON 字段',
    `like_role_ids`          varchar(1024) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `teacher_job_title_id`   bigint                                                         NULL     DEFAULT NULL COMMENT '教师职称ID',
    `teacher_job_title_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL COMMENT '教师职称名称',
    `like_job_title_name`    varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci   NULL     DEFAULT NULL,
    `create_time`            timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`            timestamp                                                      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`             tinyint                                                        NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`teacher_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_teacher_job_title
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher_job_title`;
CREATE TABLE `t_teacher_job_title`
(
    `job_title_id`   bigint                                                       NOT NULL COMMENT '教师职称ID',
    `job_title_name` varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师职称名称(助教、讲师、副教授、教授)',
    `like_name`      varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time`    timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`    timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`     tinyint                                                      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`job_title_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

-- ----------------------------
-- Table structure for t_teacher_role
-- ----------------------------
DROP TABLE IF EXISTS `t_teacher_role`;
CREATE TABLE `t_teacher_role`
(
    `role_id`     bigint                                                       NOT NULL COMMENT '教师角色ID',
    `role_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NOT NULL COMMENT '教师角色名称(导员、授课老师、行政老师)',
    `like_name`   varchar(64) CHARACTER SET utf8mb4 COLLATE utf8mb4_general_ci NULL     DEFAULT NULL,
    `create_time` timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` timestamp                                                    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `is_deleted`  tinyint                                                      NOT NULL DEFAULT 0 COMMENT '逻辑删除（0表示未删除，1表示已删除）',
    PRIMARY KEY (`role_id`) USING BTREE
) ENGINE = InnoDB
  CHARACTER SET = utf8mb4
  COLLATE = utf8mb4_general_ci
  ROW_FORMAT = DYNAMIC;

SET FOREIGN_KEY_CHECKS = 1;
