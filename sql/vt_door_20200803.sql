-- ----------------------------
-- 1、人员信息表
-- ----------------------------
DROP TABLE IF EXISTS snk_user;
CREATE TABLE snk_user (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(50) NOT NULL COMMENT '姓名',
  user_no int(10) NOT NULL COMMENT '用户号',
  card_no varchar(20) NULL COMMENT '卡号',
  dept_id bigint(20) NULL COMMENT '部门ID',
  post_id bigint(20) NULL COMMENT '岗位ID',
  sex char(1) NULL COMMENT '性别',
  id_type char(1) NULL COMMENT '证件类型',
  id_no varchar(50) NULL COMMENT '证件号',
  nation char(2) NULL COMMENT '民族',
  phone varchar(11) NULL COMMENT '手机',
  email varchar(50) NULL COMMENT '邮箱',
  address varchar(255) NULL COMMENT '地址',
  photo varchar(255) NULL COMMENT '照片',
  admin char(1) NULL COMMENT '管理员',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='人员信息表';

-- ----------------------------
-- 2、人员凭证表
-- ----------------------------
DROP TABLE IF EXISTS snk_user_proof;
CREATE TABLE snk_user_proof (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '用户ID',
  type char(1) NOT NULL COMMENT '凭证类型 1 人脸 2 指纹',
  finger char(1) NULL COMMENT '手指 0-4 左手 小指-拇指 5-9 右手 拇指-小指',
  value varchar(255) NULL COMMENT '通行凭证',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='人员凭证表';

-- ----------------------------
-- 3、黑名单表
-- ----------------------------
DROP TABLE IF EXISTS snk_user_blacklist;
CREATE TABLE snk_user_blacklist (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  ref_id bigint(20) NOT NULL COMMENT '数据源ID',
  lift_time date NULL COMMENT '解禁日期',
  remark varchar(255) NULL COMMENT '备注',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='黑名单表';

-- ----------------------------
-- 4、设备位置表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_position;
CREATE TABLE snk_device_position (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  parent_id bigint(20) NOT NULL COMMENT '父节点ID',
  ancestors varchar(50) NOT NULl COMMENT '祖级列表',
  name varchar(100) NOT NULL COMMENT '位置名称',
  order_num int(3) NOT NULL COMMENT '显示排序',
  comment varchar(255) NULL COMMENT '描述',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='设备位置表';

INSERT INTO snk_device_position values(1, 0, '0', '智慧赛宁科技', 1, null, '0', 'admin', NOW(), 'admin', NOW());

-- ----------------------------
-- 5、设备信息表
-- ----------------------------
DROP TABLE IF EXISTS snk_device;
CREATE TABLE snk_device (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  position_id bigint(20) NOT NULL COMMENT '位置ID',
  sn varchar(20) NULL COMMENT 'SN',
  name varchar(50) NOT NULL COMMENT '名称',
  auto_ip int(1) DEFAULT 0 COMMENT '是否自动获取IP',
  mac varchar(20) NULL COMMENT 'mac地址',
  ip varchar(20) NULL COMMENT 'IP',
  ip_mask varchar(20) NULL COMMENT '子网掩码',
  ip_gateway varchar(20) NULL COMMENT '网关地址',
  tcp_port int(10) NULL COMMENT 'TCP端口',
  udp_port int(10) NULL COMMENT 'UDP端口',
  server_ip varchar(20) NULL COMMENT '服务器IP',
  server_address varchar(255) NULL COMMENT '服务器地址',
  server_port int(10) NULL COMMENT '服务器端口',
  dns varchar(20) NULL COMMENT 'DNS',
  dns_bak varchar(20) NULL COMMENT 'DNS备份',
  control_port varchar(20) NOT NULL COMMENT '控制端口',
  pwd varchar(20) NULL COMMENT '密码',
  model varchar(20) NULL COMMENT '型号',
  keep_time int(10) NULL COMMENT '开锁保持时长',
  record_mode int(1) NULL DEFAULT 1 COMMENT '存储方式',
  card_type int(1) NULL COMMENT '卡片数据库类型',
  ver varchar(20) NULL COMMENT '版本',
  ver_face varchar(20) NULL COMMENT '人脸版本',
  ver_finger varchar(20) NULL COMMENT '指纹版本',
  algorit_ver varchar(20) NULL COMMENT '算法版本',
  dead_line varchar(20) NULL COMMENT '设备有效期',
  use_button int(1) NULL COMMENT '是否启用出门按钮',
  normally_open int(1) NULL COMMENT '长按出门按钮开关5秒保持常开',
  run_day int(10) NULL COMMENT '系统运行天数',
  format_count int(10) NULL COMMENT '格式化次数',
  restart_count int(10) NULL COMMENT '看门狗复位次数',
  temperature float(10,2) NULL COMMENT '系统温度',
  start_time varchar(20) NULL COMMENT '上电时间',
  voltage float(10,2) NULL COMMENT '电压',
  ups int(1) NULL COMMENT 'UPS供电状态',
  anti_back int(1) NULL COMMENT '防潜回',
  delivery varchar(20) COMMENT '出厂日期',
  manufacturer varchar(100) COMMENT '制造商',
  user_capacity int(10) NULL DEFAULT 0 COMMENT '用户容量',
  user_used int(10) NULL DEFAULT 0 COMMENT '已使用用户容量',
  card_capacity int(10) NULL DEFAULT 0 COMMENT '卡容量',
  card_used int(10) NULL DEFAULT 0 COMMENT '已使用卡容量',
  face_capacity int(10) NULL DEFAULT 0 COMMENT '人脸容量',
  face_used int(10) NULL DEFAULT 0 COMMENT '已使用人脸容量',
  fp_capacity int(10) NULL DEFAULT 0 COMMENT '指纹容量',
  fp_used int(10) NULL DEFAULT 0 COMMENT '已使用指纹容量',
  clock_capacity int(10) NULL DEFAULT 0 COMMENT '打卡记录容量',
  clock_used int(10) NULL DEFAULT 0 COMMENT '已使用打卡记录容量',
  button_capacity int(10) NULL DEFAULT 0 COMMENT '出门开关记录容量',
  button_used int(10) NULL DEFAULT 0 COMMENT '已使用出门开关记录容量',
  door_capacity int(10) NULL DEFAULT 0 COMMENT '门磁记录容量',
  door_used int(10) NULL DEFAULT 0 COMMENT '已使用门磁记录容量',
  software_capacity int(10) NULL DEFAULT 0 COMMENT '软件记录容量',
  software_used int(10) NULL DEFAULT 0 COMMENT '已使用软件记录容量',
  alarm_capacity int(10) NULL DEFAULT 0 COMMENT '报警记录容量',
  alarm_used int(10) NULL DEFAULT 0 COMMENT '已使用报警记录容量',
  sys_capacity int(10) NULL DEFAULT 0 COMMENT '系统记录容量',
  sys_used int(10) NULL DEFAULT 0 COMMENT '已使用系统记录容量',
  body_capacity int(10) NULL DEFAULT 0 COMMENT '体温记录容量',
  body_used int(10) NULL DEFAULT 0 COMMENT '已使用体温记录容量',
  work_state char(1) NULL DEFAULT '1' COMMENT '工作状态',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='设备信息表';

-- ----------------------------
-- 6、设备管理权限表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_manager;
CREATE TABLE snk_device_manager (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '管理员ID（sys_user.user_id）',
  device_id bigint(20) NOT NULL COMMENT '设备ID',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='设备管理权限表';

-- ----------------------------
-- 7、时段管理表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_time_slot;
CREATE TABLE snk_device_time_slot (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  sn varchar(20) NOT NULL COMMENT '设备SN',
  idx int(2) NOT NULL COMMENT '1-64 开门时间段',
  name varchar(50) NOT NULL COMMENT '时段名称',
  time_group text NOT NULL COMMENT '开门时段JSON  {"time1": [{"start": "00:00", "end": "01:59"}, {"start": "02:00", "end": "08:00"}], "time2": []}',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='时段管理表';

INSERT INTO snk_device_time_slot(sn, idx, name, time_group, create_by, create_time) values ('SYSTEM-', 1, '开门时段01', '{"time1":[{"start":"00:00","end":"23:59"}],"time2":[{"start":"00:00","end":"23:59"}],"time3":[{"start":"00:00","end":"23:59"}],"time4":[{"start":"00:00","end":"23:59"}],"time0":[{"start":"00:00","end":"23:59"}],"time5":[{"start":"00:00","end":"23:59"}],"time6":[{"start":"00:00","end":"23:59"}]}', 'admin', sysdate());

-- ----------------------------
-- 8、开门权限表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_auth;
CREATE TABLE snk_device_auth (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '用户ID',
  card_no varchar(20) NULL COMMENT '卡号',
  photo varchar(255) NULL COMMENT '照片',
  finger text NULL COMMENT '指纹',
  device_id bigint(20) NOT NULL COMMENT '设备ID',
  slot_id bigint(20) NULL COMMENT '开门时段ID',
  open_num int(10) NULL COMMENT '开门次数',
  holiday char(1) NOT NULL DEFAULT '1' COMMENT '禁止节假日开门',
  special char(1) NOT NULL DEFAULT '0' COMMENT '特殊权限',
  card_status char(1) NOT NULL DEFAULT '0' COMMENT '卡片状态',
  exp_time datetime NOT NULL COMMENT '有效期',
  status char(1) NOT NULL DEFAULT '0' COMMENT '上传状态',
  admin char(1) NOT NULL DEFAULT '0' COMMENT '管理员',
  remark varchar(255) NULL COMMENT '备注',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='开门权限表';

-- ----------------------------
-- 9、设备型号表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_model;
CREATE TABLE snk_device_model (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  model varchar(50) NOT NULL COMMENT '设备型号',
  device_type char(1) NOT NULL COMMENT '设备类型',
  type char(1) NULL COMMENT '控制板类型',
  port char(1) NOT NULL COMMENT '控制端口数量',
  comment varchar(100) NULL COMMENT '型号描述',
  image varchar(255) NULL COMMENT '设备图片',
  con_scheme varchar(255) NULL COMMENT '接线图',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='设备型号表';

-- ----------------------------
-- 10、开门密码表
-- ----------------------------
DROP TABLE IF EXISTS snk_device_pwd;
CREATE TABLE snk_device_pwd (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  sn varchar(20) NOT NULL COMMENT 'SN',
  device_id bigint(20) NOT NULL COMMENT '设备ID',
  password varchar(10) NOT NULL COMMENT '密码',
  open_times int(10) NULL COMMENT '开门次数',
  expiry varchar(20) NOT NULL COMMENT '有效期',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='开门密码表';

-- ----------------------------
-- 11、考勤规则表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_rule;
CREATE TABLE snk_cwa_rule (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(50) NOT NULL COMMENT '考勤规则名称',
  rule_json text NOT NULL COMMENT '考勤规则详情',
  late_mm int(10) NOT NULL COMMENT '超过多少分钟算迟到',
  abs_half int(10) NOT NULL COMMENT '迟到/早退多少分钟算旷工半天',
  abs_day int(10) NOT NULL COMMENT '迟到/早退多少分钟算旷工一天',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考勤规则表';

-- ----------------------------
-- 12、考勤设备表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_device;
CREATE TABLE snk_cwa_device (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  device_id bigint(20) NOT NULL COMMENT '设备ID',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考勤设备表';

-- ----------------------------
-- 13、考勤人员表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_user;
CREATE TABLE snk_cwa_user (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '人员ID',
  rule_id bigint(20) NOT NULL COMMENT '考勤规则ID',
  ot_day float(10, 1) NULL COMMENT '加班小时数',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考勤人员表';

-- ----------------------------
-- 14、手动登记表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_register;
CREATE TABLE snk_cwa_register (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  type char(1) NOT NULL COMMENT '登记类型 1 请假  2 调休  3 加班 4 补卡',
  leave_type char(1) NULL COMMENT '请假类型',
  user_id bigint(20) NOT NULL COMMENT '人员ID',
  start_time datetime NULL COMMENT '开始时间',
  end_time datetime NULL COMMENT '结束时间',
  leave_time float(10, 1) NULL COMMENT '小时数',
  remark varchar(255) NULL COMMENT '备注',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='手动登记表';

-- ----------------------------
-- 15、节假日表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_holiday;
CREATE TABLE snk_cwa_holiday (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  year int(4) NOT NULL COMMENT '年度',
  type char(2) NOT NULL COMMENT '节假日',
  holiday varchar(255) NOT NULL COMMENT '放假时间',
  tb_date varchar(100) NULL COMMENT '调班时间',
  ban_door char(1) NOT NULL COMMENT '禁止开门 0 否  1 是',
  remark varchar(255) NULL COMMENT '备注',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考勤补卡表';

-- ----------------------------
-- 16、考勤汇总表
-- ----------------------------
DROP TABLE IF EXISTS snk_cwa_record;
CREATE TABLE snk_cwa_record (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  user_id bigint(20) NOT NULL COMMENT '人员ID',
  year int(4) NOT NULL COMMENT '年度',
  month int(2) NOT NULL COMMENT '月份',
  be_work int(2) NOT NULL COMMENT '应出勤天数',
  do_work int(2) NOT NULL COMMENT '实际出勤天数',
  late_count int(3) NULL COMMENT '迟到次数',
  late_m int(10) NULL COMMENT '迟到(分钟)',
  out_count int(3) NULL COMMENT '早退次数',
  out_m int(10) NULL COMMENT '早退(分钟)',
  absence_count int(3) NULL COMMENT '缺勤次数',
  absence_m int(10) NULL COMMENT '缺勤(分钟)',
  leave_count int(3) NULL COMMENT '请假次数',
  leave_h float(10,1) NULL COMMENT '请假(小时)',
  fellow_count int(3) NULL COMMENT '调休次数',
  fellow_h float(10,1) NULL COMMENT '调休(小时)',
  ot_count int(3) NULL COMMENT '加班次数',
  ot_h float(10,1) NULL COMMENT '加班(小时)',
  abs_count int(3) NULL COMMENT '旷工次数',
  abs_d float(10,1) NULL COMMENT '旷工天数',
  re_card int(3) NULL COMMENT '补卡次数',
  item_json text NULL COMMENT '考勤详情',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='考勤汇总表';

-- ----------------------------
-- 17、企业信息表
-- ----------------------------
DROP TABLE IF EXISTS snk_visit_company;
CREATE TABLE snk_visit_company (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(255) NOT NULL COMMENT '企业名称',
  leader varchar(50) NULL COMMENT '负责人',
  leader_phone varchar(11) NULL COMMENT '负责人手机号',
  address varchar(255) NULL COMMENT '详细地址',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='企业信息表';

-- ----------------------------
-- 18、访问权限表
-- ----------------------------
DROP TABLE IF EXISTS snk_visit_auth;
CREATE TABLE snk_visit_auth (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  name varchar(50) NULL COMMENT '权限名称',
  slot_id bigint(20) NOT NULL COMMENT '开门时段ID',
  open_num int(10) NULL COMMENT '开门次数',
  exp_time int(10) NOT NULL COMMENT '有效期（分钟）',
  device_json bigint(20) NOT NULL COMMENT '设备信息',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=utf8 COMMENT='访问权限表';

-- ----------------------------
-- 19、来访登记表
-- ----------------------------
DROP TABLE IF EXISTS snk_visit_reg;
CREATE TABLE snk_visit_reg (
  id bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键ID',
  photo varchar(255) NOT NULL COMMENT '拍摄照片',
  company_id bigint(20) NULL COMMENT '被访问企业ID',
  company_user varchar(50) NULL COMMENT '被访问者',
  auth_id bigint(20) NOT NULL COMMENT '访问权限ID',
  id_photo varchar(255) NOT NULL COMMENT '身份证照片',
  name varchar(50) NOT NULL COMMENT '姓名',
  sex char(1) NULL COMMENT '性别',
  phone varchar(11) NOT NULL COMMENT '手机号',
  id_type char(1) NULL COMMENT '证件类型',
  id_no varchar(50) NULL COMMENT '证件号',
  visit_reason char(2) NULL COMMENT '来访事由',
  proof_type char(1) NULL COMMENT '凭证类型 1 卡号  2 二维码',
  proof_value varchar(255) NULL COMMENT '凭证',
  visit_time datetime NULL COMMENT '来访时间',
  cancel char(1) NOT NULL DEFAULT '0' COMMENT '是否注销',
  del_flag char(1) NOT NULL DEFAULT 0 COMMENT '删除标志',
  create_by varchar(64) NOT NULL COMMENT '创建者',
  create_time datetime NOT NULL COMMENT '创建时间',
  update_by varchar(64) DEFAULT '' COMMENT '更新者',
  update_time datetime DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (id)
) ENGINE=InnoDB AUTO_INCREMENT=1000000000 DEFAULT CHARSET=utf8 COMMENT='来访登记表';