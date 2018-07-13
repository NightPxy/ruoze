#!/bin/bash
#Sqoop将城市信息从MySQL导入到Hive数据仓库中(全量导入)
sqoop import \
--connect jdbc:mysql://192.168.198.151:3306/ruoze_basedata \
--username root \
--password 12abAB \
-m 2 \
--table city_info \
--split-by 'city_id' \
--mapreduce-job-name hotProduct_cityInfo2Hive \
--input-fields-terminated-by '\t' \
--hive-table ruoze.city_info \
--hive-import \
--hive-overwrite \
--delete-target-dir \
--hive-drop-import-delims
