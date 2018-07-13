#!/bin/bash
#模拟每日热门商品统计的外部环境准备

basepath=$(cd `dirname $0`;cd ..; pwd)

#用户行为记录20160505预先放入HDFS
hadoop fs -mkdir -p hdfs://hadoop000:9000/user/hadoop/user_click/20160505/
hadoop fs -put -f $basepath/others/user_click.txt hdfs://hadoop000:9000/user/hadoop/user_click/20160505/user_click.txt
#MySQL上预先放入一些产品.城市等基础信息
sql=$(cat $basepath/others/prepare_data_sql.sql)
echo '12abAB'|sudo -u mysqladmin -S /usr/local/mysql/bin/mysql -uroot -p12abAB -e "$sql"
