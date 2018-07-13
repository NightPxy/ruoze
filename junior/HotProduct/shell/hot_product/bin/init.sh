#!/bin/bash
#日热门商品统计-数据仓库建立
#  Hive中建表 产品信息,城市信息,用户行为记录,日分区热门商品排名结果

#获取执行根目录
basepath=$(cd `dirname $0`;cd ..; pwd)

#加载Shell自定义库
source $basepath/bin/shell_lib/commmon.sh
#设置配置文件地址
conf_path=$basepath/conf/hot_product.conf

#配置文件读取数据仓库HDFS存储地址
cfg_dw_hdfs=`read_conf $conf_path cfg_dw_hdfs`
#用户行为记录表的外部存储地址
userClick_hdfs=hdfs://hadoop000:9000/user/hadoop/user_click

print_msg "读取DataWarehouse HDFS地址为:$cfg_dw_hdfs"

print_msg "HDFS 开始建立DataWarehouse文件夹"
hadoop fs -mkdir -p \
$cfg_dw_hdfs/warehouse/product_info \
$cfg_dw_hdfs/warehouse/city_info  \
$cfg_dw_hdfs/warehouse/hot_product

print_msg "Hive 开始建表"
hive <<HIVE_EOF
create database if not exists ruoze;
create table if not exists ruoze.product_info (
  product_id int comment '产品Id', 
  product_name string comment '产品名称', 
  extend_info string comment '扩展信息'
) comment '产品信息' 
  row format delimited fields terminated by "\001"
  location "$cfg_dw_hdfs/user/hadoop/warehouse/product_info";
create table if not exists ruoze.city_info (
  city_id int comment '城市Id', 
  city_name string comment '城市名称',
  area string comment '归属大区编号'
) comment '城市信息' 
  row format delimited fields terminated by "\001"
  location "$cfg_dw_hdfs/user/hadoop/warehouse/city_info";
create external table if not exists ruoze.user_click(
  user_id int comment '用户Id',
  session_id string comment '用户会话Id', 
  click_time string comment '操作时间',
  city_id int comment '操作地所在城市Id',
  product_id int  comment '点击商品Id'
) comment '用户行为记录'
  partitioned by (pt int comment '日期分期字段,格式yyyyMMdd')
  row format delimited fields terminated by ','
  location "$userClick_hdfs";
create external table if not exists ruoze.hot_product(
  product_id int comment '产品Id',
  product_name string comment '产品名称', 
  area string comment '归属大区编号',
  click_count int comment '产品总点击数',
  rank int comment '产品总点击数排名',
  day string comment '统计日期 格式yyyyMMdd'
) comment '日分区热门商品排名结果'
  partitioned by (pt int comment '日期分区字段,格式yyyyMMdd')
  row format delimited fields terminated by "\001"
  location "$cfg_dw_hdfs/user/hadoop/warehouse/hot_product/";
exit;
HIVE_EOF
print_msg "数据仓库建立完成"
exit 0;
