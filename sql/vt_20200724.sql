-- ----------------------------
-- 1、部门表
-- ----------------------------
drop table if exists sys_dept;
create table sys_dept (
  dept_id           bigint(20)      not null auto_increment    comment '部门id',
  parent_id         bigint(20)      default 0                  comment '父部门id',
  ancestors         varchar(50)     default ''                 comment '祖级列表',
  dept_name         varchar(30)     default ''                 comment '部门名称',
  order_num         int(4)          default 0                  comment '显示顺序',
  leader            varchar(20)     default null               comment '负责人',
  phone             varchar(11)     default null               comment '联系电话',
  email             varchar(50)     default null               comment '邮箱',
  status            char(1)         default '0'                comment '部门状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (dept_id)
) engine=innodb auto_increment=200 comment = '部门表';

-- ----------------------------
-- 初始化-部门表数据
-- ----------------------------
insert into sys_dept values(100,  0,   '0',          '智慧凡提网络',   0, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(101,  100, '0,100',      'A公司', 1, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(102,  100, '0,100',      'B公司', 2, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(103,  101, '0,100,101',  '研发部门',   1, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(104,  101, '0,100,101',  '财务部门',   2, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(105,  102, '0,100,102',  '市场部门',   1, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());
insert into sys_dept values(106,  102, '0,100,102',  '财务部门',   2, '智慧凡提', '15888888888', 'snk@qq.com', '0', '0', 'admin', NOW(), 'admin', NOW());


-- ----------------------------
-- 2、用户信息表
-- ----------------------------
drop table if exists sys_user;
create table sys_user (
  user_id           bigint(20)      not null auto_increment    comment '用户ID',
  dept_id           bigint(20)      default null               comment '部门ID',
  login_name        varchar(30)     not null                   comment '登录账号',
  user_name         varchar(30)     default ''                 comment '用户昵称',
  user_type         varchar(2)      default '00'               comment '用户类型（00系统用户 01注册用户）',
  email             varchar(50)     default ''                 comment '用户邮箱',
  phonenumber       varchar(11)     default ''                 comment '手机号码',
  sex               char(1)         default '0'                comment '用户性别（0男 1女 2未知）',
  avatar            varchar(100)    default ''                 comment '头像路径',
  password          varchar(50)     default ''                 comment '密码',
  salt              varchar(20)     default ''                 comment '盐加密',
  status            char(1)         default '0'                comment '帐号状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  login_ip          varchar(50)     default ''                 comment '最后登陆IP',
  login_date        datetime                                   comment '最后登陆时间',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (user_id)
) engine=innodb auto_increment=100 comment = '用户信息表';

-- ----------------------------
-- 初始化-用户信息表数据
-- ----------------------------
insert into sys_user values(1,  100, 'admin', '超级管理员', '00', 'vt@163.com', '15888888888', '1', '', '29c67a30398638269fe600f73a054934', '111111', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), 'admin', NOW(), '管理员');
insert into sys_user values(2,  100, 'vt',    '管理员', '00', 'vt@qq.com',  '15666666666', '1', '', 'fb24d16b133521148205277f0f68080c', '04304a', '0', '0', '127.0.0.1', NOW(), 'admin', NOW(), 'admin', NOW(), '管理员');


-- ----------------------------
-- 3、岗位信息表
-- ----------------------------
drop table if exists sys_post;
create table sys_post
(
  post_id       bigint(20)      not null auto_increment    comment '岗位ID',
  post_code     varchar(64)     not null                   comment '岗位编码',
  post_name     varchar(50)     not null                   comment '岗位名称',
  post_sort     int(4)          not null                   comment '显示顺序',
  status        char(1)         not null                   comment '状态（0正常 1停用）',
  create_by     varchar(64)     default ''                 comment '创建者',
  create_time   datetime                                   comment '创建时间',
  update_by     varchar(64)     default ''			       comment '更新者',
  update_time   datetime                                   comment '更新时间',
  remark        varchar(500)    default null               comment '备注',
  primary key (post_id)
) engine=innodb comment = '岗位信息表';

-- ----------------------------
-- 初始化-岗位信息表数据
-- ----------------------------
insert into sys_post values(1, 'ceo',  '董事长',    1, '0', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_post values(2, 'se',   '项目经理',  2, '0', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_post values(3, 'hr',   '人力资源',  3, '0', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_post values(4, 'user', '普通员工',  4, '0', 'admin', NOW(), 'admin', NOW(), '');


-- ----------------------------
-- 4、角色信息表
-- ----------------------------
drop table if exists sys_role;
create table sys_role (
  role_id           bigint(20)      not null auto_increment    comment '角色ID',
  role_name         varchar(30)     not null                   comment '角色名称',
  role_key          varchar(100)    not null                   comment '角色权限字符串',
  role_sort         int(4)          not null                   comment '显示顺序',
  data_scope        char(1)         default '1'                comment '数据范围（1：全部数据权限 2：自定数据权限 3：本部门数据权限 4：本部门及以下数据权限）',
  status            char(1)         not null                   comment '角色状态（0正常 1停用）',
  del_flag          char(1)         default '0'                comment '删除标志（0代表存在 2代表删除）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (role_id)
) engine=innodb auto_increment=100 comment = '角色信息表';

-- ----------------------------
-- 初始化-角色信息表数据
-- ----------------------------
insert into sys_role values('1', '超级管理员', 'admin',  1, 1, '0', '0', 'admin', NOW(), 'admin', NOW(), '超级管理员');
insert into sys_role values('2', '管理员',   'common', 2, 2, '0', '0', 'admin', NOW(), 'admin', NOW(), '管理员');


-- ----------------------------
-- 5、菜单权限表
-- ----------------------------
drop table if exists sys_menu;
create table sys_menu (
  menu_id           bigint(20)      not null auto_increment    comment '菜单ID',
  menu_name         varchar(50)     not null                   comment '菜单名称',
  parent_id         bigint(20)      default 0                  comment '父菜单ID',
  order_num         int(4)          default 0                  comment '显示顺序',
  url               varchar(200)    default '#'                comment '请求地址',
  target            varchar(20)     default ''                 comment '打开方式（menuItem页签 menuBlank新窗口）',
  menu_type         char(1)         default ''                 comment '菜单类型（M目录 C菜单 F按钮）',
  visible           char(1)         default 0                  comment '菜单状态（0显示 1隐藏）',
  perms             varchar(100)    default null               comment '权限标识',
  icon              varchar(100)    default '#'                comment '菜单图标',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default ''                 comment '备注',
  primary key (menu_id)
) engine=innodb auto_increment=2000 comment = '菜单权限表';

-- ----------------------------
-- 初始化-菜单信息表数据
-- ----------------------------
-- 一级菜单
insert into sys_menu values('1', '系统管理', '0', '1', '#',                '',          'M', '0', '', 'fa fa-gear',           'admin', NOW(), 'admin', NOW(), '系统管理目录');
insert into sys_menu values('2', '系统监控', '0', '2', '#',                '',          'M', '0', '', 'fa fa-video-camera',   'admin', NOW(), 'admin', NOW(), '系统监控目录');
insert into sys_menu values('3', '系统工具', '0', '3', '#',                '',          'M', '0', '', 'fa fa-bars',           'admin', NOW(), 'admin', NOW(), '系统工具目录');
-- 二级菜单
insert into sys_menu values('100',  '用户管理', '1', '1', '/system/user',          '', 'C', '0', 'system:user:view',         '#', 'admin', NOW(), 'admin', NOW(), '用户管理菜单');
insert into sys_menu values('101',  '角色管理', '1', '2', '/system/role',          '', 'C', '0', 'system:role:view',         '#', 'admin', NOW(), 'admin', NOW(), '角色管理菜单');
insert into sys_menu values('102',  '菜单管理', '1', '3', '/system/menu',          '', 'C', '0', 'system:menu:view',         '#', 'admin', NOW(), 'admin', NOW(), '菜单管理菜单');
insert into sys_menu values('103',  '部门管理', '1', '4', '/system/dept',          '', 'C', '0', 'system:dept:view',         '#', 'admin', NOW(), 'admin', NOW(), '部门管理菜单');
insert into sys_menu values('104',  '岗位管理', '1', '5', '/system/post',          '', 'C', '0', 'system:post:view',         '#', 'admin', NOW(), 'admin', NOW(), '岗位管理菜单');
insert into sys_menu values('105',  '字典管理', '1', '6', '/system/dict',          '', 'C', '0', 'system:dict:view',         '#', 'admin', NOW(), 'admin', NOW(), '字典管理菜单');
insert into sys_menu values('106',  '参数设置', '1', '7', '/system/config',        '', 'C', '0', 'system:config:view',       '#', 'admin', NOW(), 'admin', NOW(), '参数设置菜单');
insert into sys_menu values('107',  '通知公告', '1', '8', '/system/notice',        '', 'C', '0', 'system:notice:view',       '#', 'admin', NOW(), 'admin', NOW(), '通知公告菜单');
insert into sys_menu values('108',  '日志管理', '1', '9', '#',                     '', 'M', '0', '',                         '#', 'admin', NOW(), 'admin', NOW(), '日志管理菜单');
insert into sys_menu values('109',  '在线用户', '2', '1', '/monitor/online',       '', 'C', '0', 'monitor:online:view',      '#', 'admin', NOW(), 'admin', NOW(), '在线用户菜单');
insert into sys_menu values('110',  '定时任务', '2', '2', '/monitor/job',          '', 'C', '0', 'monitor:job:view',         '#', 'admin', NOW(), 'admin', NOW(), '定时任务菜单');
insert into sys_menu values('111',  '数据监控', '2', '3', '/monitor/data',         '', 'C', '0', 'monitor:data:view',        '#', 'admin', NOW(), 'admin', NOW(), '数据监控菜单');
insert into sys_menu values('112',  '服务监控', '2', '3', '/monitor/server',       '', 'C', '0', 'monitor:server:view',      '#', 'admin', NOW(), 'admin', NOW(), '服务监控菜单');
insert into sys_menu values('113',  '表单构建', '3', '1', '/tool/build',           '', 'C', '0', 'tool:build:view',          '#', 'admin', NOW(), 'admin', NOW(), '表单构建菜单');
insert into sys_menu values('114',  '代码生成', '3', '2', '/tool/gen',             '', 'C', '0', 'tool:gen:view',            '#', 'admin', NOW(), 'admin', NOW(), '代码生成菜单');
insert into sys_menu values('115',  '系统接口', '3', '3', '/tool/swagger',         '', 'C', '0', 'tool:swagger:view',        '#', 'admin', NOW(), 'admin', NOW(), '系统接口菜单');
-- 三级菜单
insert into sys_menu values('500',  '操作日志', '108', '1', '/monitor/operlog',    '', 'C', '0', 'monitor:operlog:view',     '#', 'admin', NOW(), 'admin', NOW(), '操作日志菜单');
insert into sys_menu values('501',  '登录日志', '108', '2', '/monitor/logininfor', '', 'C', '0', 'monitor:logininfor:view',  '#', 'admin', NOW(), 'admin', NOW(), '登录日志菜单');
-- 用户管理按钮
insert into sys_menu values('1000', '用户查询', '100', '1',  '#', '',  'F', '0', 'system:user:list',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1001', '用户新增', '100', '2',  '#', '',  'F', '0', 'system:user:add',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1002', '用户修改', '100', '3',  '#', '',  'F', '0', 'system:user:edit',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1003', '用户删除', '100', '4',  '#', '',  'F', '0', 'system:user:remove',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1004', '用户导出', '100', '5',  '#', '',  'F', '0', 'system:user:export',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1005', '用户导入', '100', '6',  '#', '',  'F', '0', 'system:user:import',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1006', '重置密码', '100', '7',  '#', '',  'F', '0', 'system:user:resetPwd',    '#', 'admin', NOW(), 'admin', NOW(), '');
-- 角色管理按钮
insert into sys_menu values('1007', '角色查询', '101', '1',  '#', '',  'F', '0', 'system:role:list',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1008', '角色新增', '101', '2',  '#', '',  'F', '0', 'system:role:add',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1009', '角色修改', '101', '3',  '#', '',  'F', '0', 'system:role:edit',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1010', '角色删除', '101', '4',  '#', '',  'F', '0', 'system:role:remove',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1011', '角色导出', '101', '5',  '#', '',  'F', '0', 'system:role:export',      '#', 'admin', NOW(), 'admin', NOW(), '');
-- 菜单管理按钮
insert into sys_menu values('1012', '菜单查询', '102', '1',  '#', '',  'F', '0', 'system:menu:list',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1013', '菜单新增', '102', '2',  '#', '',  'F', '0', 'system:menu:add',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1014', '菜单修改', '102', '3',  '#', '',  'F', '0', 'system:menu:edit',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1015', '菜单删除', '102', '4',  '#', '',  'F', '0', 'system:menu:remove',      '#', 'admin', NOW(), 'admin', NOW(), '');
-- 部门管理按钮
insert into sys_menu values('1016', '部门查询', '103', '1',  '#', '',  'F', '0', 'system:dept:list',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1017', '部门新增', '103', '2',  '#', '',  'F', '0', 'system:dept:add',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1018', '部门修改', '103', '3',  '#', '',  'F', '0', 'system:dept:edit',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1019', '部门删除', '103', '4',  '#', '',  'F', '0', 'system:dept:remove',      '#', 'admin', NOW(), 'admin', NOW(), '');
-- 岗位管理按钮
insert into sys_menu values('1020', '岗位查询', '104', '1',  '#', '',  'F', '0', 'system:post:list',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1021', '岗位新增', '104', '2',  '#', '',  'F', '0', 'system:post:add',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1022', '岗位修改', '104', '3',  '#', '',  'F', '0', 'system:post:edit',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1023', '岗位删除', '104', '4',  '#', '',  'F', '0', 'system:post:remove',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1024', '岗位导出', '104', '5',  '#', '',  'F', '0', 'system:post:export',      '#', 'admin', NOW(), 'admin', NOW(), '');
-- 字典管理按钮
insert into sys_menu values('1025', '字典查询', '105', '1', '#', '',  'F', '0', 'system:dict:list',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1026', '字典新增', '105', '2', '#', '',  'F', '0', 'system:dict:add',          '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1027', '字典修改', '105', '3', '#', '',  'F', '0', 'system:dict:edit',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1028', '字典删除', '105', '4', '#', '',  'F', '0', 'system:dict:remove',       '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1029', '字典导出', '105', '5', '#', '',  'F', '0', 'system:dict:export',       '#', 'admin', NOW(), 'admin', NOW(), '');
-- 参数设置按钮
insert into sys_menu values('1030', '参数查询', '106', '1', '#', '',  'F', '0', 'system:config:list',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1031', '参数新增', '106', '2', '#', '',  'F', '0', 'system:config:add',       '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1032', '参数修改', '106', '3', '#', '',  'F', '0', 'system:config:edit',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1033', '参数删除', '106', '4', '#', '',  'F', '0', 'system:config:remove',    '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1034', '参数导出', '106', '5', '#', '',  'F', '0', 'system:config:export',    '#', 'admin', NOW(), 'admin', NOW(), '');
-- 通知公告按钮
insert into sys_menu values('1035', '公告查询', '107', '1', '#', '',  'F', '0', 'system:notice:list',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1036', '公告新增', '107', '2', '#', '',  'F', '0', 'system:notice:add',       '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1037', '公告修改', '107', '3', '#', '',  'F', '0', 'system:notice:edit',      '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1038', '公告删除', '107', '4', '#', '',  'F', '0', 'system:notice:remove',    '#', 'admin', NOW(), 'admin', NOW(), '');
-- 操作日志按钮
insert into sys_menu values('1039', '操作查询', '500', '1', '#', '',  'F', '0', 'monitor:operlog:list',    '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1040', '操作删除', '500', '2', '#', '',  'F', '0', 'monitor:operlog:remove',  '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1041', '详细信息', '500', '3', '#', '',  'F', '0', 'monitor:operlog:detail',  '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1042', '日志导出', '500', '4', '#', '',  'F', '0', 'monitor:operlog:export',  '#', 'admin', NOW(), 'admin', NOW(), '');
-- 登录日志按钮
insert into sys_menu values('1043', '登录查询', '501', '1', '#', '',  'F', '0', 'monitor:logininfor:list',         '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1044', '登录删除', '501', '2', '#', '',  'F', '0', 'monitor:logininfor:remove',       '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1045', '日志导出', '501', '3', '#', '',  'F', '0', 'monitor:logininfor:export',       '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1046', '账户解锁', '501', '4', '#', '',  'F', '0', 'monitor:logininfor:unlock',       '#', 'admin', NOW(), 'admin', NOW(), '');
-- 在线用户按钮
insert into sys_menu values('1047', '在线查询', '109', '1', '#', '',  'F', '0', 'monitor:online:list',             '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1048', '批量强退', '109', '2', '#', '',  'F', '0', 'monitor:online:batchForceLogout', '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1049', '单条强退', '109', '3', '#', '',  'F', '0', 'monitor:online:forceLogout',      '#', 'admin', NOW(), 'admin', NOW(), '');
-- 定时任务按钮
insert into sys_menu values('1050', '任务查询', '110', '1', '#', '',  'F', '0', 'monitor:job:list',                '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1051', '任务新增', '110', '2', '#', '',  'F', '0', 'monitor:job:add',                 '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1052', '任务修改', '110', '3', '#', '',  'F', '0', 'monitor:job:edit',                '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1053', '任务删除', '110', '4', '#', '',  'F', '0', 'monitor:job:remove',              '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1054', '状态修改', '110', '5', '#', '',  'F', '0', 'monitor:job:changeStatus',        '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1055', '任务详细', '110', '6', '#', '',  'F', '0', 'monitor:job:detail',              '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1056', '任务导出', '110', '7', '#', '',  'F', '0', 'monitor:job:export',              '#', 'admin', NOW(), 'admin', NOW(), '');
-- 代码生成按钮
insert into sys_menu values('1057', '生成查询', '114', '1', '#', '',  'F', '0', 'tool:gen:list',     '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1058', '生成修改', '114', '2', '#', '',  'F', '0', 'tool:gen:edit',     '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1059', '生成删除', '114', '3', '#', '',  'F', '0', 'tool:gen:remove',   '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1060', '预览代码', '114', '4', '#', '',  'F', '0', 'tool:gen:preview',  '#', 'admin', NOW(), 'admin', NOW(), '');
insert into sys_menu values('1061', '生成代码', '114', '5', '#', '',  'F', '0', 'tool:gen:code',     '#', 'admin', NOW(), 'admin', NOW(), '');
-- snk新增
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1062', '人员管理', '0', '4', '#', 'menuItem', 'M', '0', '', 'fa fa-users', 'admin', '2020-08-03 09:00:01', 'admin', '2020-09-19 14:39:33', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1063', '门禁管理', '0', '5', '#', 'menuItem', 'M', '0', '', 'fa fa-home', 'admin', '2020-08-03 09:02:19', 'admin', '2020-10-27 16:01:58', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1064', '访客管理', '0', '7', '#', 'menuItem', 'M', '1', '', 'fa fa-user-o', 'admin', '2020-08-03 09:04:33', 'admin', '2020-11-06 13:33:13', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1065', '人员信息', '1062', '1', '/door/user', 'menuItem', 'C', '0', 'door:user:view', '#', 'admin', '2020-08-03 09:38:53', 'admin', '2020-09-19 14:39:44', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1066', '列表', '1065', '1', '#', 'menuItem', 'F', '0', 'door:user:list', '#', 'admin', '2020-08-03 09:44:13', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1067', '新增', '1065', '2', '#', 'menuItem', 'F', '0', 'door:user:add', '#', 'admin', '2020-08-03 09:44:50', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1068', '编辑', '1065', '3', '#', 'menuItem', 'F', '0', 'door:user:edit', '#', 'admin', '2020-08-03 09:45:11', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1069', '删除', '1065', '4', '#', 'menuItem', 'F', '0', 'door:user:remove', '#', 'admin', '2020-08-03 09:45:29', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1070', '导出', '1065', '5', '#', 'menuItem', 'F', '0', 'door:user:export', '#', 'admin', '2020-08-03 09:45:50', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1071', '黑名单', '1062', '2', '/door/user/blacklist', 'menuItem', 'C', '0', 'door:blacklist:view', '#', 'admin', '2020-08-06 06:58:41', 'admin', '2020-08-12 15:33:20', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1072', '列表', '1071', '1', '#', 'menuItem', 'F', '0', 'door:blacklist:list', '#', 'admin', '2020-08-06 06:59:19', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1073', '编辑', '1071', '2', '#', 'menuItem', 'F', '0', 'door:blacklist:edit', '#', 'admin', '2020-08-06 07:25:56', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1074', '删除', '1071', '3', '#', 'menuItem', 'F', '0', 'door:blacklist:remove', '#', 'admin', '2020-08-06 07:26:22', 'admin', '2020-09-05 10:09:57', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1075', '导出', '1071', '4', '#', 'menuItem', 'F', '0', 'door:blacklist:export', '#', 'admin', '2020-08-06 07:37:58', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1076', '拉黑', '1065', '6', '#', 'menuItem', 'F', '0', 'door:user:blacklist', '#', 'admin', '2020-08-10 11:35:46', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1077', '导入', '1065', '7', '#', 'menuItem', 'F', '0', 'door:user:import', '#', 'admin', '2020-08-11 15:08:07', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1078', '安装位置', '1063', '1', '/door/device/position', 'menuItem', 'C', '0', 'door:position:view', '#', 'admin', '2020-08-12 15:30:44', 'admin', '2020-09-10 13:45:18', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1079', '列表', '1078', '1', '#', 'menuItem', 'F', '0', 'door:position:list', '#', 'admin', '2020-08-12 15:31:01', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1080', '新增', '1078', '2', '#', 'menuItem', 'F', '0', 'door:position:add', '#', 'admin', '2020-08-12 15:31:21', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1081', '修改', '1078', '3', '#', 'menuItem', 'F', '0', 'door:position:edit', '#', 'admin', '2020-08-12 15:31:38', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1082', '删除', '1078', '4', '#', 'menuItem', 'F', '0', 'door:position:remove', '#', 'admin', '2020-08-12 15:31:54', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1083', '设备管理', '1063', '3', '/door/device', 'menuItem', 'C', '0', 'door:device:view', '#', 'admin', '2020-08-13 09:23:41', 'admin', '2020-09-19 14:40:25', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1084', '列表', '1083', '1', '#', 'menuItem', 'F', '0', 'door:device:list', '#', 'admin', '2020-08-13 10:13:35', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1085', '安装', '1083', '2', '#', 'menuItem', 'F', '0', 'door:device:add', '#', 'admin', '2020-08-13 10:13:50', 'admin', '2020-09-02 11:19:06', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1086', '属性', '1083', '3', '#', 'menuItem', 'F', '0', 'door:device:edit', '#', 'admin', '2020-08-13 10:14:05', 'admin', '2020-08-28 17:28:08', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1087', '删除', '1083', '4', '#', 'menuItem', 'F', '0', 'door:device:remove', '#', 'admin', '2020-08-13 10:14:25', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1088', '型号管理', '1063', '2', '/door/device/model', 'menuItem', 'C', '0', 'door:model:view', '#', 'admin', '2020-08-13 13:47:08', 'admin', '2020-09-19 14:40:14', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1089', '列表', '1088', '1', '#', 'menuItem', 'F', '0', 'door:model:list', '#', 'admin', '2020-08-13 13:47:22', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1090', '新增', '1088', '2', '#', 'menuItem', 'F', '0', 'door:model:add', '#', 'admin', '2020-08-13 13:47:38', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1091', '修改', '1088', '3', '#', 'menuItem', 'F', '0', 'door:model:edit', '#', 'admin', '2020-08-13 13:47:55', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1092', '删除', '1088', '4', '#', 'menuItem', 'F', '0', 'door:model:remove', '#', 'admin', '2020-08-13 13:48:10', 'admin', '2020-08-27 16:59:28', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1093', '远程开门', '1083', '5', '#', 'menuItem', 'F', '0', 'door:device:open', '#', 'admin', '2020-08-21 15:54:15', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1094', '远程关门', '1083', '6', '#', 'menuItem', 'F', '0', 'door:device:close', '#', 'admin', '2020-08-21 15:54:42', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1095', '修改设备密码', '1083', '7', '#', 'menuItem', 'F', '0', 'door:device:writeCp', '#', 'admin', '2020-08-21 15:55:10', 'admin', '2020-09-21 09:44:34', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1096', '重置设备密码', '1083', '8', '#', 'menuItem', 'F', '0', 'door:device:resetCp', '#', 'admin', '2020-08-21 15:55:29', 'admin', '2020-09-21 09:44:51', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1097', '远程保持门常开', '1083', '9', '#', 'menuItem', 'F', '0', 'door:device:hold', '#', 'admin', '2020-08-21 16:03:22', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1098', '远程锁定门', '1083', '10', '#', 'menuItem', 'F', '0', 'door:device:lock', '#', 'admin', '2020-08-21 16:03:41', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1099', '远程解除锁定门', '1083', '11', '#', 'menuItem', 'F', '0', 'door:device:unlock', '#', 'admin', '2020-08-21 16:04:06', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1100', '设置开门保持时长', '1083', '12', '#', 'menuItem', 'F', '0', 'door:device:writeRrt', '#', 'admin', '2020-08-24 14:43:27', 'admin', '2020-08-25 11:15:10', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1101', '设置存储方式', '1083', '13', '#', 'menuItem', 'F', '0', 'door:device:writeRm', '#', 'admin', '2020-08-24 14:43:53', 'admin', '2020-08-25 11:15:30', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1102', '设置设备有效期', '1083', '14', '#', 'menuItem', 'F', '0', 'door:device:writeDl', '#', 'admin', '2020-08-25 14:28:31', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1103', '设置TCP网络参数', '1083', '15', '#', 'menuItem', 'F', '0', 'door:device:writeTcp', '#', 'admin', '2020-08-26 16:13:41', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1104', '设置出门按钮', '1083', '16', '#', 'menuItem', 'F', '0', 'door:device:writePbs', '#', 'admin', '2020-08-26 16:14:49', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1105', '设置防潜回', '1083', '17', '#', 'menuItem', 'F', '0', 'door:device:writeAp', '#', 'admin', '2020-08-27 16:55:56', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1106', '初始化设备', '1083', '18', '#', 'menuItem', 'F', '0', 'door:device:init', '#', 'admin', '2020-08-28 17:25:51', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1107', '管理员', '1083', '19', '#', 'menuItem', 'F', '0', 'door:device:manager', '#', 'admin', '2020-08-31 17:35:41', 'admin', '2020-09-19 14:41:02', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1108', '数据监控', '1083', '20', '#', 'menuItem', 'F', '0', 'door:device:watch', '#', 'admin', '2020-09-04 15:56:56', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1109', '设置开门时段', '1083', '21', '#', 'menuItem', 'F', '0', 'door:device:writeTg', '#', 'admin', '2020-09-09 18:02:03', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1110', '清空开门时段', '1083', '22', '#', 'menuItem', 'F', '0', 'door:device:clearTg', '#', 'admin', '2020-09-09 18:02:36', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1112', '权限列表', '1127', '4', '/door/device/auth', 'menuItem', 'C', '0', 'door:auth:view', '#', 'admin', '2020-09-17 11:34:06', 'admin', '2020-09-28 10:10:22', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1113', '列表', '1112', '1', '#', 'menuItem', 'F', '0', 'door:auth:list', '#', 'admin', '2020-09-17 11:34:24', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1114', '上传权限', '1112', '2', '#', 'menuItem', 'F', '0', 'door:auth:upload', '#', 'admin', '2020-09-17 13:49:08', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1115', '导出', '1112', '3', '#', 'menuItem', 'F', '0', 'door:auth:export', '#', 'admin', '2020-09-17 16:00:11', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1116', '清空权限数据', '1083', '24', '#', 'menuItem', 'F', '0', 'door:device:clearCdb', '#', 'admin', '2020-09-17 16:17:55', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1117', '开门密码', '1063', '4', '/door/device/pwd', 'menuItem', 'C', '0', 'door:pwd:view', '#', 'admin', '2020-09-22 09:34:02', 'admin', '2020-09-28 10:11:09', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1118', '列表', '1117', '1', '#', 'menuItem', 'F', '0', 'door:pwd:list', '#', 'admin', '2020-09-23 17:59:56', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1119', '添加开门密码', '1117', '2', '#', 'menuItem', 'F', '0', 'door:pwd:add', '#', 'admin', '2020-09-23 18:00:21', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1120', '删除开门密码', '1117', '3', '#', 'menuItem', 'F', '0', 'door:pwd:remove', '#', 'admin', '2020-09-23 18:00:38', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1121', '清空开门密码', '1117', '4', '#', 'menuItem', 'F', '0', 'door:pwd:clear', '#', 'admin', '2020-09-23 18:00:54', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1122', '设备记录', '1063', '5', '/door/device/record', 'menuItem', 'C', '0', 'door:record:view', '#', 'admin', '2020-09-24 14:06:05', 'admin', '2020-09-28 10:11:16', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1123', '列表', '1122', '1', '#', 'menuItem', 'F', '0', 'door:record:list', '#', 'admin', '2020-09-24 14:06:20', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1124', '采集记录', '1122', '2', '#', 'menuItem', 'F', '0', 'door:record:collect', '#', 'admin', '2020-09-25 15:59:07', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1125', '导出', '1122', '3', '#', 'menuItem', 'F', '0', 'door:record:export', '#', 'admin', '2020-09-25 15:59:39', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1127', '开门权限', '1063', '6', '#', 'menuItem', 'M', '0', '', '#', 'admin', '2020-09-28 10:10:09', 'admin', '2020-09-28 10:23:50', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1128', '开门时段', '1127', '1', '/door/device/timeslot', 'menuItem', 'C', '0', 'door:timeslot:view', '#', 'admin', '2020-09-28 10:13:06', 'admin', '2020-09-28 10:17:09', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1129', '权限设置', '1127', '2', '/door/device/auth/grant', 'menuItem', 'C', '0', 'door:auth:grant', '#', 'admin', '2020-09-28 10:14:26', 'admin', '2020-09-29 17:16:55', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1130', '列表', '1128', '1', '#', 'menuItem', 'F', '0', 'door:timeslot:list', '#', 'admin', '2020-09-28 10:16:53', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1131', '新增', '1128', '2', '#', 'menuItem', 'F', '0', 'door:timeslot:add', '#', 'admin', '2020-09-28 10:18:41', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1132', '修改', '1128', '3', '#', 'menuItem', 'F', '0', 'door:timeslot:edit', '#', 'admin', '2020-09-28 10:19:25', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1133', '删除', '1128', '4', '#', 'menuItem', 'F', '0', 'door:timeslot:remove', '#', 'admin', '2020-09-28 10:20:13', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1134', '允许通过已选门', '1129', '1', '#', 'menuItem', 'F', '0', 'door:auth:pass', '#', 'admin', '2020-09-28 10:20:49', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1135', '允许通过已选门并上传', '1129', '2', '#', 'menuItem', 'F', '0', 'door:auth:passupload', '#', 'admin', '2020-09-28 10:21:19', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1136', '禁止通过已选门', '1129', '3', '#', 'menuItem', 'F', '0', 'door:auth:ban', '#', 'admin', '2020-09-28 10:21:49', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1137', '禁止通过已选门并上传', '1129', '4', '#', 'menuItem', 'F', '0', 'door:auth:banupload', '#', 'admin', '2020-09-28 10:22:14', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1138', '权限设置', '1083', '23', '#', 'menuItem', 'F', '0', 'door:device:grant', '#', 'admin', '2020-09-29 18:17:45', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1139', '考勤管理', '0', '6', '#', 'menuItem', 'M', '0', NULL, 'fa fa-calendar', 'admin', '2020-10-27 16:04:31', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1140', '考勤规则', '1139', '1', '/door/cwa/rule', 'menuItem', 'C', '0', 'door:rule:view', '#', 'admin', '2020-10-28 10:16:12', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1141', '新增', '1140', '2', '#', 'menuItem', 'F', '0', 'door:rule:add', '#', 'admin', '2020-10-28 10:17:23', 'admin', '2020-10-28 10:18:03', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1142', '列表', '1140', '1', '#', 'menuItem', 'F', '0', 'door:rule:list', '#', 'admin', '2020-10-28 10:17:53', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1143', '修改', '1140', '3', '#', 'menuItem', 'F', '0', 'door:rule:edit', '#', 'admin', '2020-10-28 10:18:19', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1144', '删除', '1140', '4', '#', 'menuItem', 'F', '0', 'door:rule:remove', '#', 'admin', '2020-10-28 10:18:34', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1145', '考勤设备', '1139', '2', '/door/cwa/device', 'menuItem', 'C', '0', 'door:cwaDevice:view', '#', 'admin', '2020-10-28 14:57:10', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1146', '列表', '1145', '1', '#', 'menuItem', 'F', '0', 'door:cwaDevice:list', '#', 'admin', '2020-10-28 14:57:46', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1147', '新增', '1145', '2', '#', 'menuItem', 'F', '0', 'door:cwaDevice:add', '#', 'admin', '2020-10-28 14:58:04', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1148', '删除', '1145', '3', '#', 'menuItem', 'F', '0', 'door:cwaDevice:remove', '#', 'admin', '2020-10-28 14:58:21', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1149', '考勤人员', '1139', '3', '/door/cwa/user', 'menuItem', 'C', '0', 'door:cwaUser:view', '#', 'admin', '2020-10-29 09:39:31', 'admin', '2020-10-29 09:40:11', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1150', '列表', '1149', '1', '#', 'menuItem', 'F', '0', 'door:cwaUser:list', '#', 'admin', '2020-10-29 14:02:45', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1151', '新增', '1149', '2', '#', 'menuItem', 'F', '0', 'door:cwaUser:add', '#', 'admin', '2020-10-29 14:03:23', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1152', '删除', '1149', '3', '#', 'menuItem', 'F', '0', 'door:cwaUser:remove', '#', 'admin', '2020-10-29 14:03:41', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1153', '手动登记', '1139', '4', '/door/cwa/register', 'menuItem', 'C', '0', 'door:register:view', '#', 'admin', '2020-10-29 14:04:18', 'admin', '2020-10-29 14:10:26', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1154', '列表', '1153', '1', '#', 'menuItem', 'F', '0', 'door:register:list', '#', 'admin', '2020-10-31 16:49:23', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1155', '新增', '1153', '2', '#', 'menuItem', 'F', '0', 'door:register:add', '#', 'admin', '2020-10-31 16:49:41', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1156', '删除', '1153', '3', '#', 'menuItem', 'F', '0', 'door:register:remove', '#', 'admin', '2020-10-31 16:49:56', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1157', '节假日', '1139', '5', '/door/cwa/holiday', 'menuItem', 'C', '0', 'door:holiday:view', '#', 'admin', '2020-10-31 16:50:12', 'admin', '2020-10-31 16:50:45', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1158', '列表', '1157', '1', '#', 'menuItem', 'F', '0', 'door:holiday:list', '#', 'admin', '2020-10-31 16:51:08', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1159', '新增', '1157', '2', '#', 'menuItem', 'F', '0', 'door:holiday:add', '#', 'admin', '2020-10-31 16:51:28', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1160', '修改', '1157', '3', '#', 'menuItem', 'F', '0', 'door:holiday:edit', '#', 'admin', '2020-10-31 16:51:49', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1161', '删除', '1157', '4', '#', 'menuItem', 'F', '0', 'door:holiday:remove', '#', 'admin', '2020-10-31 16:52:10', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1162', '考勤报表', '1139', '6', '/door/cwa/record', 'menuItem', 'C', '0', 'door:record:view', '#', 'admin', '2020-11-02 14:08:55', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1163', '列表', '1162', '1', '#', 'menuItem', 'F', '0', 'door:cwaRecord:list', '#', 'admin', '2020-11-02 14:09:08', 'admin', '2020-11-06 13:32:12', '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1164', '生成报表', '1162', '2', '#', 'menuItem', 'F', '0', 'door:cwaRecord:init', '#', 'admin', '2020-11-06 13:32:34', '', NULL, '');
INSERT INTO `snk`.`sys_menu` (`menu_id`, `menu_name`, `parent_id`, `order_num`, `url`, `target`, `menu_type`, `visible`, `perms`, `icon`, `create_by`, `create_time`, `update_by`, `update_time`, `remark`) VALUES ('1165', '导出', '1162', '3', '#', 'menuItem', 'F', '0', 'door:cwaRecord:export', '#', 'admin', '2020-11-06 13:32:53', '', NULL, '');

-- ----------------------------
-- 6、用户和角色关联表  用户N-1角色
-- ----------------------------
drop table if exists sys_user_role;
create table sys_user_role (
  user_id   bigint(20) not null comment '用户ID',
  role_id   bigint(20) not null comment '角色ID',
  primary key(user_id, role_id)
) engine=innodb comment = '用户和角色关联表';

-- ----------------------------
-- 初始化-用户和角色关联表数据
-- ----------------------------
insert into sys_user_role values ('1', '1');
insert into sys_user_role values ('2', '2');


-- ----------------------------
-- 7、角色和菜单关联表  角色1-N菜单
-- ----------------------------
drop table if exists sys_role_menu;
create table sys_role_menu (
  role_id   bigint(20) not null comment '角色ID',
  menu_id   bigint(20) not null comment '菜单ID',
  primary key(role_id, menu_id)
) engine=innodb comment = '角色和菜单关联表';

-- ----------------------------
-- 初始化-角色和菜单关联表数据
-- ----------------------------
insert into sys_role_menu values ('2', '1');
insert into sys_role_menu values ('2', '2');
insert into sys_role_menu values ('2', '4');
insert into sys_role_menu values ('2', '100');
insert into sys_role_menu values ('2', '101');
insert into sys_role_menu values ('2', '103');
insert into sys_role_menu values ('2', '104');
insert into sys_role_menu values ('2', '107');
insert into sys_role_menu values ('2', '108');
insert into sys_role_menu values ('2', '109');
insert into sys_role_menu values ('2', '500');
insert into sys_role_menu values ('2', '501');
insert into sys_role_menu values ('2', '1000');
insert into sys_role_menu values ('2', '1001');
insert into sys_role_menu values ('2', '1002');
insert into sys_role_menu values ('2', '1003');
insert into sys_role_menu values ('2', '1004');
insert into sys_role_menu values ('2', '1005');
insert into sys_role_menu values ('2', '1006');
insert into sys_role_menu values ('2', '1007');
insert into sys_role_menu values ('2', '1008');
insert into sys_role_menu values ('2', '1009');
insert into sys_role_menu values ('2', '1010');
insert into sys_role_menu values ('2', '1011');
insert into sys_role_menu values ('2', '1016');
insert into sys_role_menu values ('2', '1017');
insert into sys_role_menu values ('2', '1018');
insert into sys_role_menu values ('2', '1019');
insert into sys_role_menu values ('2', '1020');
insert into sys_role_menu values ('2', '1021');
insert into sys_role_menu values ('2', '1022');
insert into sys_role_menu values ('2', '1023');
insert into sys_role_menu values ('2', '1024');
insert into sys_role_menu values ('2', '1035');
insert into sys_role_menu values ('2', '1036');
insert into sys_role_menu values ('2', '1037');
insert into sys_role_menu values ('2', '1038');
insert into sys_role_menu values ('2', '1039');
insert into sys_role_menu values ('2', '1040');
insert into sys_role_menu values ('2', '1041');
insert into sys_role_menu values ('2', '1042');
insert into sys_role_menu values ('2', '1043');
insert into sys_role_menu values ('2', '1044');
insert into sys_role_menu values ('2', '1045');
insert into sys_role_menu values ('2', '1046');
insert into sys_role_menu values ('2', '1047');
insert into sys_role_menu values ('2', '1048');
insert into sys_role_menu values ('2', '1049');

-- ----------------------------
-- 8、角色和部门关联表  角色1-N部门
-- ----------------------------
drop table if exists sys_role_dept;
create table sys_role_dept (
  role_id   bigint(20) not null comment '角色ID',
  dept_id   bigint(20) not null comment '部门ID',
  primary key(role_id, dept_id)
) engine=innodb comment = '角色和部门关联表';

-- ----------------------------
-- 初始化-角色和部门关联表数据
-- ----------------------------
insert into sys_role_dept values ('2', '100');
insert into sys_role_dept values ('2', '101');
insert into sys_role_dept values ('2', '105');

-- ----------------------------
-- 9、用户与岗位关联表  用户1-N岗位
-- ----------------------------
drop table if exists sys_user_post;
create table sys_user_post
(
  user_id   bigint(20) not null comment '用户ID',
  post_id   bigint(20) not null comment '岗位ID',
  primary key (user_id, post_id)
) engine=innodb comment = '用户与岗位关联表';

-- ----------------------------
-- 初始化-用户与岗位关联表数据
-- ----------------------------
insert into sys_user_post values ('1', '1');
insert into sys_user_post values ('2', '2');


-- ----------------------------
-- 10、操作日志记录
-- ----------------------------
drop table if exists sys_oper_log;
create table sys_oper_log (
  oper_id           bigint(20)      not null auto_increment    comment '日志主键',
  title             varchar(50)     default ''                 comment '模块标题',
  business_type     int(2)          default 0                  comment '业务类型（0其它 1新增 2修改 3删除）',
  method            varchar(100)    default ''                 comment '方法名称',
  request_method    varchar(10)     default ''                 comment '请求方式',
  operator_type     int(1)          default 0                  comment '操作类别（0其它 1后台用户 2手机端用户）',
  oper_name         varchar(50)     default ''                 comment '操作人员',
  dept_name         varchar(50)     default ''                 comment '部门名称',
  oper_url          varchar(255)    default ''                 comment '请求URL',
  oper_ip           varchar(50)     default ''                 comment '主机地址',
  oper_location     varchar(255)    default ''                 comment '操作地点',
  oper_param        varchar(2000)   default ''                 comment '请求参数',
  json_result       varchar(2000)   default ''                 comment '返回参数',
  status            int(1)          default 0                  comment '操作状态（0正常 1异常）',
  error_msg         varchar(2000)   default ''                 comment '错误消息',
  oper_time         datetime                                   comment '操作时间',
  primary key (oper_id)
) engine=innodb auto_increment=100 comment = '操作日志记录';


-- ----------------------------
-- 11、字典类型表
-- ----------------------------
drop table if exists sys_dict_type;
create table sys_dict_type
(
  dict_id          bigint(20)      not null auto_increment    comment '字典主键',
  dict_name        varchar(100)    default ''                 comment '字典名称',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_id),
  unique (dict_type)
) engine=innodb auto_increment=100 comment = '字典类型表';

insert into sys_dict_type values(1,  '用户性别', 'sys_user_sex',        '0', 'admin', NOW(), 'admin', NOW(), '用户性别列表');
insert into sys_dict_type values(2,  '菜单状态', 'sys_show_hide',       '0', 'admin', NOW(), 'admin', NOW(), '菜单状态列表');
insert into sys_dict_type values(3,  '系统开关', 'sys_normal_disable',  '0', 'admin', NOW(), 'admin', NOW(), '系统开关列表');
insert into sys_dict_type values(4,  '任务状态', 'sys_job_status',      '0', 'admin', NOW(), 'admin', NOW(), '任务状态列表');
insert into sys_dict_type values(5,  '任务分组', 'sys_job_group',       '0', 'admin', NOW(), 'admin', NOW(), '任务分组列表');
insert into sys_dict_type values(6,  '系统是否', 'sys_yes_no',          '0', 'admin', NOW(), 'admin', NOW(), '系统是否列表');
insert into sys_dict_type values(7,  '通知类型', 'sys_notice_type',     '0', 'admin', NOW(), 'admin', NOW(), '通知类型列表');
insert into sys_dict_type values(8,  '通知状态', 'sys_notice_status',   '0', 'admin', NOW(), 'admin', NOW(), '通知状态列表');
insert into sys_dict_type values(9,  '操作类型', 'sys_oper_type',       '0', 'admin', NOW(), 'admin', NOW(), '操作类型列表');
insert into sys_dict_type values(10, '系统状态', 'sys_common_status',   '0', 'admin', NOW(), 'admin', NOW(), '登录状态列表');
insert into sys_dict_type values(11, '民族',    'snk_nation',          '0', 'admin', NOW(), 'admin', NOW(), '名族列表');
insert into sys_dict_type values(12, '证件类型', 'snk_id_type',         '0', 'admin', NOW(), 'admin', NOW(), '证件类型列表');
insert into sys_dict_type values(13, '出入类型', 'snk_io_type',         '0', 'admin', NOW(), 'admin', NOW(), '出入类型列表');
insert into sys_dict_type values(14, '控制板类型', 'snk_model_type',     '0', 'admin', NOW(), 'admin', NOW(), '控制板类型列表');
insert into sys_dict_type values(15, '门禁类型', 'snk_device_type',     '0', 'admin', NOW(), 'admin', NOW(), '门禁类型列表');
insert into sys_dict_type values(16, '工作状态', 'snk_work_state',      '0', 'admin', NOW(), 'admin', NOW(), '工作状态列表');
insert into sys_dict_type values(17, '存储方式', 'snk_record_mode',     '0', 'admin', NOW(), 'admin', NOW(), '存储方式列表');
insert into sys_dict_type values(18, '控制端口', 'snk_model_port',      '0', 'admin', NOW(), 'admin', NOW(), '控制端口列表');
insert into sys_dict_type values(19, '特殊权限', 'snk_special_type',    '0', 'admin', NOW(), 'admin', NOW(), '特殊权限列表');
insert into sys_dict_type values(20, '上传状态', 'snk_upload_status',   '0', 'admin', NOW(), 'admin', NOW(), '上传状态列表');
insert into sys_dict_type values(21, '是否删除', 'snk_del_flag',        '0', 'admin', NOW(), 'admin', NOW(), '是否删除列表');
insert into sys_dict_type values(22, '是否受限制', 'snk_limit_flag',     '0', 'admin', NOW(), 'admin', NOW(), '是否受限制列表');
insert into sys_dict_type values(23, '是否', 'snk_yes_no',     '0', 'admin', NOW(), 'admin', NOW(), '是否列表');
insert into sys_dict_type values(24, '卡片状态', 'snk_card_status',     '0', 'admin', NOW(), 'admin', NOW(), '卡片状态列表');
insert into sys_dict_type values(25, '控制板记录类型', 'snk_record_type',     '0', 'admin', NOW(), 'admin', NOW(), '控制板记录类型列表');
insert into sys_dict_type values(26, '人脸|指纹记录类型', 'snk_fg_record_type',     '0', 'admin', NOW(), 'admin', NOW(), '人脸|指纹记录类型列表');
insert into sys_dict_type values(27, '手动登记类型', 'snk_register_type',     '0', 'admin', NOW(), 'admin', NOW(), '手动登记类型列表');
insert into sys_dict_type values(28, '请假类型', 'snk_leave_type',     '0', 'admin', NOW(), 'admin', NOW(), '请假类型列表');
insert into sys_dict_type values(29, '节假日', 'snk_holiday_type',     '0', 'admin', NOW(), 'admin', NOW(), '节假日列表');
insert into sys_dict_type values(30, '来访事由', 'snk_visit_reason',     '0', 'admin', NOW(), 'admin', NOW(), '来访事由列表');
insert into sys_dict_type values(31, '访客状态', 'snk_visit_state',     '0', 'admin', NOW(), 'admin', NOW(), '访客状态列表');


-- ----------------------------
-- 12、字典数据表
-- ----------------------------
drop table if exists sys_dict_data;
create table sys_dict_data
(
  dict_code        bigint(20)      not null auto_increment    comment '字典编码',
  dict_sort        int(4)          default 0                  comment '字典排序',
  dict_label       varchar(100)    default ''                 comment '字典标签',
  dict_value       varchar(100)    default ''                 comment '字典键值',
  dict_type        varchar(100)    default ''                 comment '字典类型',
  css_class        varchar(100)    default null               comment '样式属性（其他样式扩展）',
  list_class       varchar(100)    default null               comment '表格回显样式',
  is_default       char(1)         default 'N'                comment '是否默认（Y是 N否）',
  status           char(1)         default '0'                comment '状态（0正常 1停用）',
  create_by        varchar(64)     default ''                 comment '创建者',
  create_time      datetime                                   comment '创建时间',
  update_by        varchar(64)     default ''                 comment '更新者',
  update_time      datetime                                   comment '更新时间',
  remark           varchar(500)    default null               comment '备注',
  primary key (dict_code)
) engine=innodb auto_increment=200 comment = '字典数据表';

insert into sys_dict_data values(1,  1,  '男',       '1',       'sys_user_sex',        '',   '',        'Y', '0', 'admin', NOW(), 'admin', NOW(), '性别男');
insert into sys_dict_data values(2,  2,  '女',       '2',       'sys_user_sex',        '',   '',        'N', '0', 'admin', NOW(), 'admin', NOW(), '性别女');
insert into sys_dict_data values(3,  3,  '未知',     '9',       'sys_user_sex',        '',   '',        'N', '0', 'admin', NOW(), 'admin', NOW(), '性别未知');
insert into sys_dict_data values(4,  1,  '显示',     '0',       'sys_show_hide',       '',   'primary', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '显示菜单');
insert into sys_dict_data values(5,  2,  '隐藏',     '1',       'sys_show_hide',       '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '隐藏菜单');
insert into sys_dict_data values(6,  1,  '正常',     '0',       'sys_normal_disable',  '',   'primary', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '正常状态');
insert into sys_dict_data values(7,  2,  '停用',     '1',       'sys_normal_disable',  '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '停用状态');
insert into sys_dict_data values(8,  1,  '正常',     '0',       'sys_job_status',      '',   'primary', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '正常状态');
insert into sys_dict_data values(9,  2,  '暂停',     '1',       'sys_job_status',      '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '停用状态');
insert into sys_dict_data values(10, 1,  '默认',     'DEFAULT', 'sys_job_group',       '',   '',        'Y', '0', 'admin', NOW(), 'admin', NOW(), '默认分组');
insert into sys_dict_data values(11, 2,  '系统',     'SYSTEM',  'sys_job_group',       '',   '',        'N', '0', 'admin', NOW(), 'admin', NOW(), '系统分组');
insert into sys_dict_data values(12, 1,  '是',       'Y',       'sys_yes_no',          '',   'primary', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '系统默认是');
insert into sys_dict_data values(13, 2,  '否',       'N',       'sys_yes_no',          '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '系统默认否');
insert into sys_dict_data values(14, 1,  '通知',     '1',       'sys_notice_type',     '',   'warning', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '通知');
insert into sys_dict_data values(15, 2,  '公告',     '2',       'sys_notice_type',     '',   'success', 'N', '0', 'admin', NOW(), 'admin', NOW(), '公告');
insert into sys_dict_data values(16, 1,  '正常',     '0',       'sys_notice_status',   '',   'primary', 'Y', '0', 'admin', NOW(), 'admin', NOW(), '正常状态');
insert into sys_dict_data values(17, 2,  '关闭',     '1',       'sys_notice_status',   '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '关闭状态');
insert into sys_dict_data values(18, 99, '其他',     '0',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', NOW(), 'admin', NOW(), '其他操作');
insert into sys_dict_data values(19, 1,  '新增',     '1',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', NOW(), 'admin', NOW(), '新增操作');
insert into sys_dict_data values(20, 2,  '修改',     '2',       'sys_oper_type',       '',   'info',    'N', '0', 'admin', NOW(), 'admin', NOW(), '修改操作');
insert into sys_dict_data values(21, 3,  '删除',     '3',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '删除操作');
insert into sys_dict_data values(22, 4,  '授权',     '4',       'sys_oper_type',       '',   'primary', 'N', '0', 'admin', NOW(), 'admin', NOW(), '授权操作');
insert into sys_dict_data values(23, 5,  '导出',     '5',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', NOW(), 'admin', NOW(), '导出操作');
insert into sys_dict_data values(24, 6,  '导入',     '6',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', NOW(), 'admin', NOW(), '导入操作');
insert into sys_dict_data values(25, 7,  '强退',     '7',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '强退操作');
insert into sys_dict_data values(26, 8,  '生成代码', '8',       'sys_oper_type',       '',   'warning', 'N', '0', 'admin', NOW(), 'admin', NOW(), '生成操作');
insert into sys_dict_data values(27, 9,  '清空数据', '9',       'sys_oper_type',       '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '清空操作');
insert into sys_dict_data values(28, 1,  '成功',     '0',       'sys_common_status',   '',   'primary', 'N', '0', 'admin', NOW(), 'admin', NOW(), '正常状态');
insert into sys_dict_data values(29, 2,  '失败',     '1',       'sys_common_status',   '',   'danger',  'N', '0', 'admin', NOW(), 'admin', NOW(), '停用状态');
insert into sys_dict_data values(30, 1,  '汉族',     '01',      'snk_nation',         '',         '',     'Y',    '0', 'admin', NOW(), 'admin', NOW(), '汉族');
insert into sys_dict_data values(31, 2,  '壮族',     '02',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '壮族');
insert into sys_dict_data values(32, 3,  '满族',     '03',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '满族');
insert into sys_dict_data values(33, 4,  '回族',     '04',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '回族');
insert into sys_dict_data values(34, 5,  '苗族',     '05',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '苗族');
insert into sys_dict_data values(35, 6,  '维吾尔族',  '06',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '维吾尔族');
insert into sys_dict_data values(36, 7,  '土家族',    '07',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '土家族');
insert into sys_dict_data values(37, 8,  '彝族',     '08',       'snk_nation',        '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '彝族');
insert into sys_dict_data values(38, 9,  '蒙古族',    '09',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '蒙古族');
insert into sys_dict_data values(39, 10, '藏族',     '10',       'snk_nation',        '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '藏族');
insert into sys_dict_data values(40, 11, '布依族',    '11',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '布依族');
insert into sys_dict_data values(41, 12, '侗族',     '12',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '侗族');
insert into sys_dict_data values(42, 13, '瑶族',     '13',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '瑶族');
insert into sys_dict_data values(43, 14, '朝鲜族',    '14',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '朝鲜族');
insert into sys_dict_data values(44, 15, '白族',     '15',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '白族');
insert into sys_dict_data values(45, 16, '哈尼族',   '16',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '哈尼族');
insert into sys_dict_data values(46, 17, '哈萨克族',  '17',      'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '哈萨克族');
insert into sys_dict_data values(47, 18, '黎族',     '18',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '黎族');
insert into sys_dict_data values(48, 19, '傣族',     '19',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '傣族');
insert into sys_dict_data values(49, 20, '畲族',     '20',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '畲族');
insert into sys_dict_data values(50, 21, '傈僳族',   '21',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '傈僳族');
insert into sys_dict_data values(51, 22, '仡佬族',   '22',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '仡佬族');
insert into sys_dict_data values(52, 23, '东乡族',   '23',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '东乡族');
insert into sys_dict_data values(53, 24, '高山族',   '24',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '高山族');
insert into sys_dict_data values(54, 25, '拉祜族',   '25',       'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '拉祜族');
insert into sys_dict_data values(55, 26, '水族',     '26',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '水族');
insert into sys_dict_data values(56, 27, '佤族',     '27',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '佤族');
insert into sys_dict_data values(57, 28, '纳西族',   '28',      'snk_nation',          '',         '',     'N',     '0', 'admin', NOW(), 'admin', NOW(), '纳西族');
insert into sys_dict_data values(58, 29, '羌族',     '29',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '羌族');
insert into sys_dict_data values(59, 30, '土族',     '30',      'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '土族');
insert into sys_dict_data values(60, 31, '仫佬族',   '31',      'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '仫佬族');
insert into sys_dict_data values(61, 32, '锡伯族',    '32',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '锡伯族');
insert into sys_dict_data values(62, 33, '柯尔克孜族', '33',     'snk_nation',           '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '柯尔克孜族');
insert into sys_dict_data values(63, 34, '达斡尔族',   '34',    'snk_nation',           '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '达斡尔族');
insert into sys_dict_data values(64, 35, '景颇族',    '35',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '景颇族');
insert into sys_dict_data values(65, 36, '毛南族',    '36',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '毛南族');
insert into sys_dict_data values(66, 37, '撒拉族',    '37',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '撒拉族');
insert into sys_dict_data values(67, 38, '布朗族',    '38',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '布朗族');
insert into sys_dict_data values(68, 39, '塔吉克族',  '39',      'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '塔吉克族');
insert into sys_dict_data values(69, 40, '阿昌族',    '40',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '阿昌族');
insert into sys_dict_data values(70, 41, '普米族',    '41',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '普米族');
insert into sys_dict_data values(71, 42, '鄂温克族',   '42',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '鄂温克族');
insert into sys_dict_data values(72, 43, '怒族',      '43',    'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '怒族');
insert into sys_dict_data values(73, 44, '京族',      '44',     'snk_nation',         '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '京族');
insert into sys_dict_data values(74, 45, '基诺族',    '45',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '基诺族');
insert into sys_dict_data values(75, 46, '德昂族',    '46',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '德昂族');
insert into sys_dict_data values(76, 47, '保安族',    '47',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '保安族');
insert into sys_dict_data values(77, 48, '俄罗斯族',  '48',      'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '俄罗斯族');
insert into sys_dict_data values(78, 49, '裕固族',    '49',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '裕固族');
insert into sys_dict_data values(79, 50, '乌孜别克族', '50',     'snk_nation',           '',         '',     'N',     '0', 'admin', NOW(), 'admin', NOW(), '乌孜别克族');
insert into sys_dict_data values(80, 51, '门巴族',    '51',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '门巴族');
insert into sys_dict_data values(81, 52, '鄂伦春族',   '52',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '鄂伦春族');
insert into sys_dict_data values(82, 53, '独龙族',     '53',    'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '独龙族');
insert into sys_dict_data values(83, 54, '塔塔尔族',   '54',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '塔塔尔族');
insert into sys_dict_data values(84, 55, '赫哲族',    '55',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '赫哲族');
insert into sys_dict_data values(85, 56, '珞巴族',    '56',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '珞巴族');
insert into sys_dict_data values(86, 99, '其他',     '57',     'snk_nation',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '其他');
insert into sys_dict_data values(87, 1, '身份证',    '1',      'snk_id_type',          '',         '',     'Y',    '0', 'admin', NOW(), 'admin', NOW(), '身份证');
insert into sys_dict_data values(88, 2, '驾驶证',    '2',      'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '驾驶证');
insert into sys_dict_data values(89, 3, '护照',    '3',        'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '护照');
insert into sys_dict_data values(90, 4, '军官证',    '4',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '军官证');
insert into sys_dict_data values(91, 5, '士兵证',    '5',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '士兵证');
insert into sys_dict_data values(92, 6, '警官证',    '6',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '警官证');
insert into sys_dict_data values(93, 7, '户口本',    '7',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '户口本');
insert into sys_dict_data values(94, 8, '学生证',    '8',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '学生证');
insert into sys_dict_data values(95, 9, '港澳居民来往内地通行证',    '9',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '港澳居民来往内地通行证');
insert into sys_dict_data values(96, 10, '台湾居民来往大陆通行证',    '10',     'snk_id_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '台湾居民来往大陆通行证');
insert into sys_dict_data values(97, 1, '入',    '1',     'snk_io_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '入');
insert into sys_dict_data values(98, 2, '出',    '2',     'snk_io_type',          '',         '',     'N',    '0', 'admin', NOW(), 'admin', NOW(), '出');
insert into sys_dict_data values(99, 1,  '普通板',       '1',       'snk_model_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '普通板');
insert into sys_dict_data values(100, 2,  '高级板',       '2',       'snk_model_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '高级板');
insert into sys_dict_data values(101, 1,  '控制板',       '1',       'snk_device_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '刷卡机');
insert into sys_dict_data values(102, 2,  '人脸机',       '2',       'snk_device_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '人脸机');
insert into sys_dict_data values(103, 3,  '指纹机',       '3',       'snk_device_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '指纹机');
insert into sys_dict_data values(104, 1,  '在线',       '1',       'snk_work_state',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '在线');
insert into sys_dict_data values(105, 2,  '离线',       '2',       'snk_work_state',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '离线');
insert into sys_dict_data values(106, 1,  '满循环',       '0',       'snk_record_mode',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '满循环');
insert into sys_dict_data values(107, 2,  '满不循环',       '1',       'snk_record_mode',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '满不循环');
insert into sys_dict_data values(108, 1,  '单门',       '1',       'snk_model_port',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '单门');
insert into sys_dict_data values(109, 2,  '双门',       '2',       'snk_model_port',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '双门');
insert into sys_dict_data values(110, 3,  '四门',       '4',       'snk_model_port',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '四门');
insert into sys_dict_data values(111, 1,  '普通开门卡',       '0',       'snk_special_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '普通开门卡');
insert into sys_dict_data values(112, 2,  '首卡特权开门卡',       '1',       'snk_special_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '首卡特权开门卡');
insert into sys_dict_data values(113, 3,  '常开特权开门卡',       '2',       'snk_special_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '常开特权开门卡');
insert into sys_dict_data values(114, 4,  '巡更签到卡',       '3',       'snk_special_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '巡更签到卡');
insert into sys_dict_data values(115, 5,  '防盗设置卡',       '4',       'snk_special_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '防盗设置卡');
insert into sys_dict_data values(116, 1,  '未上传',       '0',       'snk_upload_status',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '未上传');
insert into sys_dict_data values(117, 2,  '已上传',       '1',       'snk_upload_status',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '已上传');
insert into sys_dict_data values(118, 1,  '未删除',       '0',       'snk_del_flag',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '未删除');
insert into sys_dict_data values(119, 2,  '已删除',       '1',       'snk_del_flag',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '已删除');
insert into sys_dict_data values(120, 1,  '不受限制',       '0',       'snk_limit_flag',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '不受限制');
insert into sys_dict_data values(121, 2,  '受限制',       '1',       'snk_limit_flag',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '受限制');
insert into sys_dict_data values(122, 1,  '否',       '0',       'snk_yes_no',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '否');
insert into sys_dict_data values(123, 2,  '是',       '1',       'snk_yes_no',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '是');
insert into sys_dict_data values(124, 1,  '普通',       '0',       'snk_card_status',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '普通');
insert into sys_dict_data values(125, 2,  '挂失',       '1',       'snk_card_status',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '挂失');
insert into sys_dict_data values(126, 3,  '黑名单',       '2',       'snk_card_status',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '黑名单');
insert into sys_dict_data values(127, 1,  '刷卡记录',       '1',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '刷卡记录');
insert into sys_dict_data values(128, 2,  '出门按钮记录',       '2',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '出门按钮记录');
insert into sys_dict_data values(129, 3,  '门磁记录',       '3',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '门磁记录');
insert into sys_dict_data values(130, 4,  '软件操作记录',       '4',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '软件操作记录');
insert into sys_dict_data values(131, 5,  '报警记录',       '5',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '报警记录');
insert into sys_dict_data values(132, 6,  '系统记录',       '6',       'snk_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '系统记录');
insert into sys_dict_data values(133, 1,  '刷卡记录',       '1',       'snk_fg_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '刷卡记录');
insert into sys_dict_data values(134, 2,  '门磁记录',       '2',       'snk_fg_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '门磁记录');
insert into sys_dict_data values(135, 3,  '系统记录',       '3',       'snk_fg_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '系统记录');
insert into sys_dict_data values(136, 4,  '体温记录',       '4',       'snk_fg_record_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '体温记录');
insert into sys_dict_data values(137, 1,  '请假',       '1',       'snk_register_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '请假');
insert into sys_dict_data values(138, 2,  '调休',       '2',       'snk_register_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '调休');
insert into sys_dict_data values(139, 3,  '加班',       '3',       'snk_register_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '加班');
insert into sys_dict_data values(140, 4,  '补卡',       '4',       'snk_register_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '补卡');
insert into sys_dict_data values(141, 1,  '事假',       '1',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '事假');
insert into sys_dict_data values(142, 2,  '年假',       '2',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '年假');
insert into sys_dict_data values(143, 3,  '婚假',       '3',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '婚假');
insert into sys_dict_data values(144, 4,  '产假',       '4',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '产假');
insert into sys_dict_data values(145, 5,  '病假',       '5',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '病假');
insert into sys_dict_data values(146, 6,  '丧假',       '6',       'snk_leave_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '丧假');
insert into sys_dict_data values(147, 1,  '元旦节',       '1',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '元旦节');
insert into sys_dict_data values(148, 2,  '春节',       '2',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '春节');
insert into sys_dict_data values(149, 3,  '清明节',       '3',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '清明节');
insert into sys_dict_data values(150, 4,  '劳动节',       '4',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '劳动节');
insert into sys_dict_data values(151, 5,  '端午节',       '5',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '端午节');
insert into sys_dict_data values(152, 6,  '中秋节',       '6',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '中秋节');
insert into sys_dict_data values(153, 7,  '国庆节',       '7',       'snk_holiday_type',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '国庆节');
insert into sys_dict_data values(154, 1,  '面试',       '1',       'snk_visit_reason',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '国庆节');
insert into sys_dict_data values(155, 2,  '送快递',       '2',       'snk_visit_reason',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '国庆节');
insert into sys_dict_data values(156, 3,  '走访客户',       '3',       'snk_visit_reason',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '国庆节');
insert into sys_dict_data values(157, 4,  '企业员工',       '4',       'snk_visit_reason',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '国庆节');
insert into sys_dict_data values(158, 1,  '新建',       '1',       'snk_visit_state',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '新建');
insert into sys_dict_data values(159, 2,  '已审核',       '2',       'snk_visit_state',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '已审核');
insert into sys_dict_data values(160, 3,  '注销',       '3',       'snk_visit_state',          '',   '',  'N', '0', 'admin', NOW(), 'admin', NOW(), '注销');

-- ----------------------------
-- 13、参数配置表
-- ----------------------------
drop table if exists sys_config;
create table sys_config (
  config_id         int(5)          not null auto_increment    comment '参数主键',
  config_name       varchar(100)    default ''                 comment '参数名称',
  config_key        varchar(100)    default ''                 comment '参数键名',
  config_value      varchar(500)    default ''                 comment '参数键值',
  config_type       char(1)         default 'N'                comment '系统内置（Y是 N否）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(500)    default null               comment '备注',
  primary key (config_id)
) engine=innodb auto_increment=100 comment = '参数配置表';

insert into sys_config values(1, '主框架页-默认皮肤样式名称',     'sys.index.skinName',       'skin-blue',     'Y', 'admin', NOW(), 'admin', NOW(), '蓝色 skin-blue、绿色 skin-green、紫色 skin-purple、红色 skin-red、黄色 skin-yellow');
insert into sys_config values(2, '用户管理-账号初始密码',         'sys.user.initPassword',    '123456',        'Y', 'admin', NOW(), 'admin', NOW(), '初始化密码 123456');
insert into sys_config values(3, '主框架页-侧边栏主题',           'sys.index.sideTheme',      'theme-dark',    'Y', 'admin', NOW(), 'admin', NOW(), '深黑主题theme-dark，浅色主题theme-light，深蓝主题theme-blue');
insert into sys_config values(4, '账号自助-是否开启用户注册功能', 'sys.account.registerUser', 'false',         'Y', 'admin', NOW(), 'admin', NOW(), '是否开启注册用户功能');


-- ----------------------------
-- 14、系统访问记录
-- ----------------------------
drop table if exists sys_logininfor;
create table sys_logininfor (
  info_id        bigint(20)     not null auto_increment   comment '访问ID',
  login_name     varchar(50)    default ''                comment '登录账号',
  ipaddr         varchar(50)    default ''                comment '登录IP地址',
  login_location varchar(255)   default ''                comment '登录地点',
  browser        varchar(50)    default ''                comment '浏览器类型',
  os             varchar(50)    default ''                comment '操作系统',
  status         char(1)        default '0'               comment '登录状态（0成功 1失败）',
  msg            varchar(255)   default ''                comment '提示消息',
  login_time     datetime                                 comment '访问时间',
  primary key (info_id)
) engine=innodb auto_increment=100 comment = '系统访问记录';


-- ----------------------------
-- 15、在线用户记录
-- ----------------------------
drop table if exists sys_user_online;
create table sys_user_online (
  sessionId         varchar(50)   default ''                comment '用户会话id',
  login_name        varchar(50)   default ''                comment '登录账号',
  dept_name         varchar(50)   default ''                comment '部门名称',
  ipaddr            varchar(50)   default ''                comment '登录IP地址',
  login_location    varchar(255)  default ''                comment '登录地点',
  browser           varchar(50)   default ''                comment '浏览器类型',
  os                varchar(50)   default ''                comment '操作系统',
  status            varchar(10)   default ''                comment '在线状态on_line在线off_line离线',
  start_timestamp   datetime                                comment 'session创建时间',
  last_access_time  datetime                                comment 'session最后访问时间',
  expire_time       int(5)        default 0                 comment '超时时间，单位为分钟',
  primary key (sessionId)
) engine=innodb comment = '在线用户记录';


-- ----------------------------
-- 16、定时任务调度表
-- ----------------------------
drop table if exists sys_job;
create table sys_job (
  job_id              bigint(20)    not null auto_increment    comment '任务ID',
  job_name            varchar(64)   default ''                 comment '任务名称',
  job_group           varchar(64)   default 'DEFAULT'          comment '任务组名',
  invoke_target       varchar(500)  not null                   comment '调用目标字符串',
  cron_expression     varchar(255)  default ''                 comment 'cron执行表达式',
  misfire_policy      varchar(20)   default '3'                comment '计划执行错误策略（1立即执行 2执行一次 3放弃执行）',
  concurrent          char(1)       default '1'                comment '是否并发执行（0允许 1禁止）',
  status              char(1)       default '0'                comment '状态（0正常 1暂停）',
  create_by           varchar(64)   default ''                 comment '创建者',
  create_time         datetime                                 comment '创建时间',
  update_by           varchar(64)   default ''                 comment '更新者',
  update_time         datetime                                 comment '更新时间',
  remark              varchar(500)  default ''                 comment '备注信息',
  primary key (job_id, job_name, job_group)
) engine=innodb auto_increment=100 comment = '定时任务调度表';

insert into sys_job values(1, '黑名单解禁', 'SYSTEM', 'systemTask.blacklist',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(2, '采集设备版本号', 'SYSTEM', 'systemTask.readVer('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(3, '采集设备在线状态', 'SYSTEM', 'systemTask.readState('''')',        '0 */10 * * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每10分钟执行一次');
insert into sys_job values(4, '采集设备卡信息', 'SYSTEM', 'systemTask.readCardInfo('''')',        '0 0 */1 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每1小时执行一次');
insert into sys_job values(5, '采集设备数据信息', 'SYSTEM', 'systemTask.readTransactionData('''')',        '0 0 */1 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每1小时执行一次');
insert into sys_job values(6, '采集设备存储方式', 'SYSTEM', 'systemTask.readRecordMode('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(7, '采集设备密码', 'SYSTEM', 'systemTask.readPwd('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(8, '采集设备卡片数据', 'SYSTEM', 'systemTask.readCardData('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(9, '采集设备开门时长', 'SYSTEM', 'systemTask.readReleaseTime('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(10, '采集设备有效期', 'SYSTEM', 'systemTask.readDeadline('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(11, '采集设备出门按钮', 'SYSTEM', 'systemTask.readPushButton('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(12, '采集设备运行信息', 'SYSTEM', 'systemTask.readSystemStatus('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(13, '采集设备防潜回', 'SYSTEM', 'systemTask.readAntiBack('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(14, '采集设备网络参数', 'SYSTEM', 'systemTask.readTcp('''')',        '0 0 0 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每天0点执行');
insert into sys_job values(15, '采集设备记录', 'SYSTEM', 'systemTask.readRecord('''')',        '0 */30 * * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每30分钟执行一次');
insert into sys_job values(16, '采集设备人员数据', 'SYSTEM', 'systemTask.readPersonDetail('''')',        '0 0 */1 * * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每1小时执行一次');
insert into sys_job values(17, '采集OEM', 'SYSTEM', 'systemTask.readOEM('''')',        '0 0 0 1 * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每月1号执行');
insert into sys_job values(18, '统计考勤', 'SYSTEM', 'systemTask.cwaRecord()',        '0 0 0 1-5 * ?', '3', '1', '1', 'admin', NOW(), 'admin', NOW(), '每月1-5号零点执行');

-- ----------------------------
-- 17、定时任务调度日志表
-- ----------------------------
drop table if exists sys_job_log;
create table sys_job_log (
  job_log_id          bigint(20)     not null auto_increment    comment '任务日志ID',
  job_name            varchar(64)    not null                   comment '任务名称',
  job_group           varchar(64)    not null                   comment '任务组名',
  invoke_target       varchar(500)   not null                   comment '调用目标字符串',
  job_message         varchar(500)                              comment '日志信息',
  status              char(1)        default '0'                comment '执行状态（0正常 1失败）',
  exception_info      varchar(2000)  default ''                 comment '异常信息',
  create_time         datetime                                  comment '创建时间',
  primary key (job_log_id)
) engine=innodb comment = '定时任务调度日志表';


-- ----------------------------
-- 18、通知公告表
-- ----------------------------
drop table if exists sys_notice;
create table sys_notice (
  notice_id         int(4)          not null auto_increment    comment '公告ID',
  notice_title      varchar(50)     not null                   comment '公告标题',
  notice_type       char(1)         not null                   comment '公告类型（1通知 2公告）',
  notice_content    varchar(2000)   default null               comment '公告内容',
  status            char(1)         default '0'                comment '公告状态（0正常 1关闭）',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time       datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  remark            varchar(255)    default null               comment '备注',
  primary key (notice_id)
) engine=innodb auto_increment=10 comment = '通知公告表';

-- ----------------------------
-- 初始化-公告信息表数据
-- ----------------------------
insert into sys_notice values('1', '温馨提醒：2018-07-01 智慧凡提新版本发布啦', '2', '新版本内容', '0', 'admin', NOW(), 'admin', NOW(), '管理员');
insert into sys_notice values('2', '维护通知：2018-07-01 智慧凡提系统凌晨维护', '1', '维护内容',   '0', 'admin', NOW(), 'admin', NOW(), '管理员');


-- ----------------------------
-- 19、代码生成业务表
-- ----------------------------
drop table if exists gen_table;
create table gen_table (
  table_id             bigint(20)      not null auto_increment    comment '编号',
  table_name           varchar(200)    default ''                 comment '表名称',
  table_comment        varchar(500)    default ''                 comment '表描述',
  sub_table_name       varchar(64)     default null               comment '关联子表的表名',
  sub_table_fk_name    varchar(64)     default null               comment '子表关联的外键名',
  class_name           varchar(100)    default ''                 comment '实体类名称',
  tpl_category         varchar(200)    default 'crud'             comment '使用的模板（crud单表操作 tree树表操作 sub主子表操作）',
  package_name         varchar(100)                               comment '生成包路径',
  module_name          varchar(30)                                comment '生成模块名',
  business_name        varchar(30)                                comment '生成业务名',
  function_name        varchar(50)                                comment '生成功能名',
  function_author      varchar(50)                                comment '生成功能作者',
  gen_type             char(1)         default '0'                comment '生成代码方式（0zip压缩包 1自定义路径）',
  gen_path             varchar(200)    default '/'                comment '生成路径（不填默认项目路径）',
  options              varchar(1000)                              comment '其它生成选项',
  create_by            varchar(64)     default ''                 comment '创建者',
  create_time 	       datetime                                   comment '创建时间',
  update_by            varchar(64)     default ''                 comment '更新者',
  update_time          datetime                                   comment '更新时间',
  remark               varchar(500)    default null               comment '备注',
  primary key (table_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表';


-- ----------------------------
-- 20、代码生成业务表字段
-- ----------------------------
drop table if exists gen_table_column;
create table gen_table_column (
  column_id         bigint(20)      not null auto_increment    comment '编号',
  table_id          varchar(64)                                comment '归属表编号',
  column_name       varchar(200)                               comment '列名称',
  column_comment    varchar(500)                               comment '列描述',
  column_type       varchar(100)                               comment '列类型',
  java_type         varchar(500)                               comment 'JAVA类型',
  java_field        varchar(200)                               comment 'JAVA字段名',
  is_pk             char(1)                                    comment '是否主键（1是）',
  is_increment      char(1)                                    comment '是否自增（1是）',
  is_required       char(1)                                    comment '是否必填（1是）',
  is_insert         char(1)                                    comment '是否为插入字段（1是）',
  is_edit           char(1)                                    comment '是否编辑字段（1是）',
  is_list           char(1)                                    comment '是否列表字段（1是）',
  is_query          char(1)                                    comment '是否查询字段（1是）',
  query_type        varchar(200)    default 'EQ'               comment '查询方式（等于、不等于、大于、小于、范围）',
  html_type         varchar(200)                               comment '显示类型（文本框、文本域、下拉框、复选框、单选框、日期控件）',
  dict_type         varchar(200)    default ''                 comment '字典类型',
  sort              int                                        comment '排序',
  create_by         varchar(64)     default ''                 comment '创建者',
  create_time 	    datetime                                   comment '创建时间',
  update_by         varchar(64)     default ''                 comment '更新者',
  update_time       datetime                                   comment '更新时间',
  primary key (column_id)
) engine=innodb auto_increment=1 comment = '代码生成业务表字段';