#!/bin/bash

#Sqoop将产品信息从MySQL导入到Hive数据仓库中(全量导入)
sqoop import \
--connect jdbc:mysql://192.168.198.151:3306/ruoze_product \
--username root \
--password 12abAB \
-m 2 \
--table product_info \
--split-by 'product_id' \
--mapreduce-job-name hotProduct_productInfo2Hive \
--input-fields-terminated-by '\t' \
--hive-table ruoze.product_info \
--hive-import \
--hive-overwrite \
--delete-target-dir \
--hive-drop-import-delims