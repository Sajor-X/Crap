/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50725 (5.7.25-log)
 Source Host           : localhost:3306
 Source Schema         : crap

 Target Server Type    : MySQL
 Target Server Version : 50725 (5.7.25-log)
 File Encoding         : 65001

 Date: 08/02/2023 16:06:42
*/

SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for crap_admin_detail
-- ----------------------------
DROP TABLE IF EXISTS `crap_admin_detail`;
CREATE TABLE `crap_admin_detail` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;


-- ----------------------------
-- Table structure for crap_op_log
-- ----------------------------
DROP TABLE IF EXISTS `crap_op_log`;
CREATE TABLE `crap_op_log` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `title` varchar(255) NOT NULL DEFAULT '' COMMENT '标题',
  `resource` varchar(255) NOT NULL DEFAULT '' COMMENT '菜单地址',
  `type` varchar(255) NOT NULL DEFAULT '' COMMENT '类型',
  `action` varchar(100) NOT NULL DEFAULT '' COMMENT '动作',
  `source_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '源数据 ID',
  `source_code` varchar(255) NOT NULL DEFAULT '' COMMENT '源数据 CODE',
  `op_url` varchar(255) NOT NULL DEFAULT '' COMMENT '当前访问地址',
  `view_url` varchar(255) NOT NULL DEFAULT '' COMMENT '预览地址',
  `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '备注',
  `ip` varchar(255) NOT NULL DEFAULT '' COMMENT 'ip',
  `create_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 创建人',
  `create_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 创建人',
  `create_time` datetime NOT NULL COMMENT 'System 创建时间',
  `tid` int(11) NOT NULL DEFAULT '0' COMMENT 'System TID',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='数据变更日志';


-- ----------------------------
-- Table structure for crap_rbac_privilege
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_privilege`;
CREATE TABLE `crap_rbac_privilege` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `pid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'PID',
  `name` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '标题',
  `resource` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '资源标识 uri / code ...',
  `code` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '编码',
  `project` varchar(50) CHARACTER SET utf8 NOT NULL DEFAULT 'crap' COMMENT '项目',
  `sort` int(11) NOT NULL DEFAULT '0' COMMENT '排序权重, 降序',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT '0:禁用, 1:启用',
  `icon` varchar(255) NOT NULL DEFAULT '' COMMENT '图标',
  `show_flag` bit(1) NOT NULL DEFAULT b'0' COMMENT '是否显示',
  `type` int(11) NOT NULL DEFAULT '1' COMMENT '1: 授权访问; 2: 完全公开; 3: 仅用于开发者账号',
  `sql` varchar(255) NOT NULL DEFAULT '' COMMENT 'sql 模板 : SELECT * FROM table WHERE user_id={[''webUser''].id}',
  `tpl` varchar(255) NOT NULL DEFAULT '' COMMENT '前端模板 : @/views*',
  `scope` varchar(255) NOT NULL DEFAULT '' COMMENT '系统标识',
  `remark` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '备注',
  `tid` int(11) NOT NULL DEFAULT '0' COMMENT 'System TID',
  `create_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 创建人',
  `create_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 创建人',
  `create_time` datetime NOT NULL COMMENT 'System 创建时间',
  `update_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 更新人',
  `update_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 更新人',
  `update_time` datetime NOT NULL COMMENT 'System 更新时间',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `pid` (`pid`) USING BTREE,
  KEY `status` (`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 权限';

-- ----------------------------
-- Table structure for crap_rbac_role
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_role`;
CREATE TABLE `crap_rbac_role` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `name` char(20) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '名称',
  `code` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '编码',
  `project` varchar(20) CHARACTER SET utf8 NOT NULL DEFAULT 'crap' COMMENT '项目',
  `status` tinyint(1) NOT NULL DEFAULT '1' COMMENT 'enum 状态. 1:启用; 0:禁用;',
  `remark` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '备注',
  `delete_flag` datetime DEFAULT NULL COMMENT '逻辑删除',
  `tid` int(11) NOT NULL DEFAULT '0' COMMENT 'System TID',
  `create_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 创建人',
  `create_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 创建人',
  `create_time` datetime NOT NULL COMMENT 'System 创建时间',
  `update_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 更新人',
  `update_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 更新人',
  `update_time` datetime NOT NULL COMMENT 'System 更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 角色';

-- ----------------------------
-- Records of crap_rbac_role
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for crap_rbac_role_privilege
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_role_privilege`;
CREATE TABLE `crap_rbac_role_privilege` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT 'ID',
  `role_id` bigint(20) NOT NULL COMMENT '角色 ID',
  `privilege_id` bigint(20) NOT NULL COMMENT '权限 ID',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `crap_rbac_role_privilege_FK` (`role_id`) USING BTREE,
  KEY `crap_rbac_role_privilege_FK_1` (`privilege_id`) USING BTREE,
  CONSTRAINT `crap_rbac_role_privilege_FK` FOREIGN KEY (`role_id`) REFERENCES `crap_rbac_role` (`id`),
  CONSTRAINT `crap_rbac_role_privilege_FK_1` FOREIGN KEY (`privilege_id`) REFERENCES `crap_rbac_privilege` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 角色-权限关联';

-- ----------------------------
-- Records of crap_rbac_role_privilege
-- ----------------------------
BEGIN;
COMMIT;

-- ----------------------------
-- Table structure for crap_rbac_token
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_token`;
CREATE TABLE `crap_rbac_token` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `type` varchar(255) NOT NULL DEFAULT '' COMMENT '渠道',
  `user_id` bigint(20) NOT NULL DEFAULT '0' COMMENT '用户 ID',
  `token` varchar(1000) NOT NULL DEFAULT '' COMMENT 'jwt',
  `token_ttl` int(20) NOT NULL DEFAULT '0' COMMENT 'jwt 有效期(s)',
  `token_time` datetime DEFAULT NULL COMMENT 'jwt  刷新时间',
  `last_op_time` datetime DEFAULT NULL COMMENT '最近操作时间',
  `create_time` datetime NOT NULL COMMENT 'System 创建时间',
  `update_time` datetime NOT NULL COMMENT 'System 更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 登录令牌';


-- ----------------------------
-- Table structure for crap_rbac_user
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_user`;
CREATE TABLE `crap_rbac_user` (
  `id` bigint(20) NOT NULL COMMENT 'ID',
  `code` varchar(255) NOT NULL DEFAULT '' COMMENT 'code(U). 编码',
  `username` varchar(100) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT '账号',
  `password` varchar(255) CHARACTER SET utf8 NOT NULL DEFAULT '' COMMENT 'ignore 密码',
  `name` varchar(100) NOT NULL DEFAULT '' COMMENT '姓名',
  `mobile` varchar(60) DEFAULT NULL COMMENT 'encrypt 手机号码',
  `status` tinyint(4) NOT NULL DEFAULT '1' COMMENT 'bool 是否有效',
  `role_ids` text COMMENT 'json(java.util.List<Long>) 角色 ID 列表',
  `department_ids` text COMMENT 'json(java.util.List<Long>) 部门 ID 列表',
  `lock_flag` tinyint(4) NOT NULL DEFAULT '0' COMMENT '0: 未锁定; 1: 已锁定;',
  `last_op_time` datetime DEFAULT NULL COMMENT '最近操作时间',
  `sys_version` int(11) NOT NULL DEFAULT '0' COMMENT '乐观锁版本号',
  `delete_flag` datetime DEFAULT NULL COMMENT '逻辑删除',
  `create_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 创建人',
  `create_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 创建人',
  `create_time` datetime NOT NULL COMMENT 'System 创建时间',
  `update_uid` bigint(20) NOT NULL DEFAULT '0' COMMENT 'System 更新人',
  `update_uname` varchar(255) NOT NULL DEFAULT '' COMMENT 'System 更新人',
  `update_time` datetime NOT NULL COMMENT 'System 更新时间',
  `tid` int(11) NOT NULL DEFAULT '0' COMMENT 'System TID',
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `k_admin_username_uindex` (`username`) USING BTREE,
  UNIQUE KEY `k_mobile_uniq` (`mobile`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 用户';

-- ----------------------------
-- Records of crap_rbac_user
-- ----------------------------
BEGIN;
INSERT INTO `crap_rbac_user` (`id`, `code`, `username`, `password`, `name`, `mobile`, `status`, `role_ids`, `department_ids`, `lock_flag`, `last_op_time`, `sys_version`, `delete_flag`, `create_uid`, `create_uname`, `create_time`, `update_uid`, `update_uname`, `update_time`, `tid`) VALUES (1, 'sys', 'sys', '$e0801$Af2S6YG9PCKB+bIGkbGt85cIF9JmD7sPw1pFOxW6wmyvqz4DNMda6YTbWEjSoQ2pmWVrTA3sqywIagSF719QlA==$Bcoy8FTW7bJFcQNEkHw8vsnql5XSZZwyu8fWtXcLwVM=', '管理员', 'LLOcCk5eyLNApMF7SSlGxw==', 1, '[]', '[]', 0, '2023-02-07 23:26:55', 0, NULL, 0, 'admin', '2022-11-29 21:21:57', 1, 'wWO6xBp7O+gCQsiAXFUERA==', '2023-02-07 22:15:43', 0);
COMMIT;

-- ----------------------------
-- Table structure for crap_rbac_user_role
-- ----------------------------
DROP TABLE IF EXISTS `crap_rbac_user_role`;
CREATE TABLE `crap_rbac_user_role` (
  `id` bigint(20) NOT NULL,
  `user_id` bigint(20) NOT NULL,
  `role_id` bigint(20) NOT NULL,
  PRIMARY KEY (`id`) USING BTREE,
  UNIQUE KEY `uid_group_id` (`user_id`,`role_id`) USING BTREE,
  KEY `uid` (`user_id`) USING BTREE,
  KEY `group_id` (`role_id`) USING BTREE,
  CONSTRAINT `crap_rbac_user_role_FK` FOREIGN KEY (`user_id`) REFERENCES `crap_rbac_user` (`id`),
  CONSTRAINT `crap_rbac_user_role_FK_1` FOREIGN KEY (`role_id`) REFERENCES `crap_rbac_role` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='RBAC 用户-角色关联';

-- ----------------------------
-- Records of crap_rbac_user_role
-- ----------------------------
BEGIN;
COMMIT;



SET FOREIGN_KEY_CHECKS = 1;
