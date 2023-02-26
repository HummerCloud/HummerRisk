

UPDATE `rule_group` SET `level` = '对象存储' where `name` in ('Aliyun OSS合规基线', 'Huawei OBS合规基线', 'Tencent COS合规基线', 'AWS S3合规基线', 'Aliyun OSS 最佳安全实践');

ALTER TABLE `cloud_event` MODIFY column `user_agent` text DEFAULT NULL COMMENT '用户代理';

ALTER TABLE `cloud_event` MODIFY column `user_identity` mediumtext DEFAULT NULL COMMENT '用户认证';

ALTER TABLE `cloud_event` MODIFY column `additional_event_data` text DEFAULT NULL COMMENT '额外数据';

ALTER TABLE `cloud_event` MODIFY column `request_parameters` text DEFAULT NULL COMMENT '请求参数';

ALTER TABLE `cloud_event` MODIFY column `referenced_resources` text DEFAULT NULL COMMENT '映射的资源';

ALTER TABLE `cloud_event` MODIFY column `response_elements` text DEFAULT NULL COMMENT '响应值';

UPDATE `server_rule` SET `script` = 'flag=true\nif [[ $(cat /etc/audit/audit.rules|wc -l) -gt 3 ]];then\n    echo \"HummerSuccess: auditctl 审计规则已设置\"\n    flag=false\n	exit 0\nfi\nif $flag ;then\n    echo \"HummerError: auditctl 审计规则未设置，请及时处理！\"\nfi' WHERE `id` = '1feaf989-836f-4f90-975d-8caac4b18692';
UPDATE `server_rule` SET `script` = 'flag=true\nif [[ $(cat /var/log/secure | grep refused|wc -l) -lt 5 ]];then\n    echo \"HummerSuccess: 检测通过，没有异常登录\"\n    flag=false\n	exit 0\nfi\nif $flag ;then\n    echo \"HummerError: 存在多次登录身份验证失败，请及时处理！\"\nfi' WHERE `id` = '562fd271-6484-42db-b72b-08d679cb4e03';
UPDATE `server_rule` SET `script` = 'flag=true\nif cat /var/log/secure | grep \"Failed\" > /dev/null;then\n    cat /var/log/secure|awk \'/Failed/{print $(NF-3)}\'|sort|uniq -c|awk \'{print \"HummerError: 登入失败的IP和尝试次数: \"$2\"=\"$1\"次---------[需调整]\";}\'\n    flag=false\n	exit 0\nfi\nif $flag ;then\n    echo \"HummerSuccess: 检测通过，无ssh暴力登入信息\"\nfi' WHERE `id` = '5ca75854-b03c-4e9f-9ec7-4bf77f16d94d';
UPDATE `server_rule` SET `script` = 'flag=true\nif [[ $(cat /etc/security/limits.conf|grep -v \'^#\'|grep -v \'^$\'|wc -l) -gt 10 ]];then\n    echo \"HummerSuccess: 检测通过，已经配置资源限定规则\"\n    flag=false\n	exit 0\nfi\nif $flag ;then\n    echo \"HummerError: 未配置资源限定规则，请及时处理！\"\nfi' WHERE `id` = '87ecea8f-3ba1-48d0-8fcd-b3d74e1dae60';

INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`, `suggestion`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', 'Aliyun PostgreSQL 实例网络类型检测', 1, 'HighRisk', 'Aliyun 账号下 PostgreSQL 实例已关联到 VPC；若您配置阈值，则关联的 VpcId 需存在您列出的阈值中，符合视为“合规”，否则视为“不合规”', 'policies:\n    # Aliyun 账号下 PostgreSQL 实例已关联到 VPC；若您配置阈值，则关联的 VpcId 需存在您列出的阈值中，符合视为“合规”，否则视为“不合规”。\n    - name: aliyun-postgre-sql-instance-network-type\n      resource: aliyun.postgre-sql\n      filters:\n        - type: instance-network-type\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"实例的网络类型:Classic经典网络/VPC网络\",\"defaultValue\":\"VPC\",\"required\":true}]', 'hummer-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), '1', 'custodian', NULL);
INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`, `suggestion`) VALUES ('d0edb278-0df2-41fd-9032-7eac190416df', 'Aliyun PostgreSQL 实例公网访问检测', 1, 'HighRisk', 'Aliyun 检测您账号下 PostgreSQL 实例不允许任意来源公网访问，符合视为“合规”，否则视为“不合规”', 'policies:\n    # Aliyun 检测您账号下 PostgreSQL 实例不允许任意来源公网访问，符合视为“合规”，否则视为“不合规”。\n    - name: aliyun-postgre-sql-internet-access\n      resource: aliyun.postgre-sql\n      filters:\n        - type: internet-access\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"启用公网访问\",\"defaultValue\":\"true\",\"required\":true}]', 'hummer-aliyun-plugin', '阿里云', 'aliyun.png', concat(unix_timestamp(now()), '001'), '1', 'custodian', NULL);

INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', 'safety');
INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('d0edb278-0df2-41fd-9032-7eac190416df', 'safety');

INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('63fc2a4e-7fe6-4257-b38e-9f4927c3f8a0', 'd0edb278-0df2-41fd-9032-7eac190416df', 'aliyun.postgre-sql');
INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('a2cc4016-1ff7-4625-a337-67a4269d0f9f', '0eaec886-c769-4dfb-8efa-7933328fde0d', 'aliyun.postgre-sql');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', '3');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', '4');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', '5');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', '6');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', '7');
INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('d0edb278-0df2-41fd-9032-7eac190416df', '97');

SELECT id INTO @groupId1 FROM rule_group WHERE name =  'Aliyun PostgreSQL 最佳安全实践';

SELECT id INTO @groupId2 FROM rule_group WHERE name =  'Aliyun 等保预检';

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', @groupId1);

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('0eaec886-c769-4dfb-8efa-7933328fde0d', @groupId2);

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('d0edb278-0df2-41fd-9032-7eac190416df', @groupId2);

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('d0edb278-0df2-41fd-9032-7eac190416df', @groupId1);

INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`, `suggestion`) VALUES ('1268870b-188d-41a8-a86e-0c83299dc93c', 'Qiniu Bucket 公开读取访问权限检测', 1, 'HighRisk', 'Qiniu 查看您的 存储桶是否不允许公开读取访问权限。如果某个存储桶策略允许公开读取访问权限，则该存储桶“不合规“', 'policies:\n    - name: qiniu-bucket-private\n      resource: qiniu.bucket\n      filters:\n        - type: private\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"私有状态：0 公开， 1 私有\",\"defaultValue\":\"1\",\"required\":true}]', 'hummer-qiniu-plugin', '七牛云', 'qiniu.png', concat(unix_timestamp(now()), '002'), '1', 'custodian', NULL);

INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('1268870b-188d-41a8-a86e-0c83299dc93c', 'safety');

INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('c0cd80bf-259f-4a46-a1a8-9ce7edc5e592', '1268870b-188d-41a8-a86e-0c83299dc93c', 'qiniu.bucket');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('1268870b-188d-41a8-a86e-0c83299dc93c', '10');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('1268870b-188d-41a8-a86e-0c83299dc93c', '13');

INSERT INTO `rule_group` (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Qiniu 对象存储合规基线', '对象存储合规检查为您提供全方位的对象存储资源检查功能。', '对象存储', 'hummer-qiniu-plugin', 1);

SELECT id INTO @groupId3 FROM rule_group WHERE name =  'Qiniu 对象存储合规基线';

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('1268870b-188d-41a8-a86e-0c83299dc93c', @groupId3);

INSERT INTO `rule` (`id`, `name`, `status`, `severity`, `description`, `script`, `parameter`, `plugin_id`, `plugin_name`, `plugin_icon`, `last_modified`, `flag`, `scan_type`, `suggestion`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', 'Baidu BOS 存储桶加密检测', 1, 'HighRisk', 'Baidu 查看并确认您的 BOS 存储桶启用了默认加密，开启视为“合规”，未开启则该存储桶“不合规“', 'policies:\n    - name: baidu-bos-encryption\n      resource: baidu.bos\n      filters:\n        - type: encryption\n          value: ${{value}}', '[{\"key\":\"value\",\"name\":\"加密算法\",\"defaultValue\":\"none\",\"required\":true}]', 'hummer-baidu-plugin', '百度云', 'baidu.png', concat(unix_timestamp(now()), '003'), '1', 'custodian', NULL);

INSERT INTO `rule_tag_mapping` (`rule_id`, `tag_key`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', 'safety');

INSERT INTO `rule_type` (`id`, `rule_id`, `resource_type`) VALUES ('8140407a-9ef7-4042-9834-67c26b3fa8e8', 'accfd8f2-636a-4e01-8b15-18fc61afa7e3', 'baidu.bos');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', '53');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', '55');

INSERT INTO `rule_inspection_report_mapping` (`rule_id`, `report_id`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', '96');

INSERT INTO `rule_group` (`name`, `description`, `level`, `plugin_id`, `flag`) VALUES ('Baidu BOS 合规基线', 'BOS 合规检查为您提供全方位的对象存储资源检查功能。', '对象存储', 'hummer-baidu-plugin', 1);

SELECT id INTO @groupId4 FROM rule_group WHERE name =  'Baidu BOS 合规基线';

SELECT id INTO @groupId5 FROM rule_group WHERE name =  'Baidu 等保预检';

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', @groupId4);

INSERT INTO `rule_group_mapping` (`rule_id`, `group_id`) VALUES ('accfd8f2-636a-4e01-8b15-18fc61afa7e3', @groupId5);
