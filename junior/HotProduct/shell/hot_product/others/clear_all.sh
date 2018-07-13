#!/bin/bash
#清理全部的每日热门商品统计相关,方便从头演示

#删除Hive数据仓库全部数据
hive<<EOF
  drop database if exists ruoze CASCADE;exit;
EOF
#删除HDFS上的相关信息
hadoop fs -rm -f hdfs://hadoop000:9000/user/hadoop/user_click/20160505/user_click.txt
#删除MySQL上的相关信息
echo '12abAB'|sudo -u mysqladmin -S /usr/local/mysql/bin/mysql -uroot -p12abAB -e 'drop database if exists ruoze_product;drop database if exists ruoze_basedata;drop database if exists ruoze;'

exit 0
