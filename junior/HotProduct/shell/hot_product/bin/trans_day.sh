#!/bin/bash
#日热门商品统计-每日数据清洗

#获取执行根目录
basepath=$(cd `dirname $0`;cd ..; pwd)

#加载Shell自定义库
source $basepath/bin/shell_lib/commmon.sh
#设置配置文件地址
conf_path=$basepath/conf/hot_product.conf

#配置文件读取数据仓库HDFS存储地址
cfg_dw_hdfs=`read_conf $conf_path cfg_dw_hdfs`

#清洗结果目的端信息
resultTo_url='jdbc:mysql://192.168.198.151:3306/ruoze?useUnicode=true&characterEncoding=utf8'
resultTo_user="root"
resultTo_pwd="12abAB"
resultTo_db="ruoze"
resultTo_table="hot_product"


#指定日期热门商品统计
function train_day(){

#统计时间
train_time=$1

print_msg "[$train_time]用户行为导入开始"
hive <<HIVE_EOF
   use ruoze;
   alter table user_click add if not exists partition(pt=$train_time) location "$cfg_hdfs/user/hadoop/user_click/$train_time";
   exit;
HIVE_EOF

print_msg "[$train_time]热门商品统计开始"
hive <<HIVE_EOF
  use ruoze;
  create temporary table temp_product_hot_count 
  as 
   select c.area,uc.product_id,from_unixtime(unix_timestamp(uc.click_time),"yyyyMMdd") as day,count(1) as click_count
   from user_click uc
   join city_info c on uc.city_id = c.city_id
   where from_unixtime(unix_timestamp(uc.click_time),"yyyyMMdd")=$train_time
   group by c.area,uc.product_id,from_unixtime(unix_timestamp(uc.click_time),"yyyyMMdd");
   insert overwrite table ruoze.hot_product partition(pt=$train_time)
   select rz.product_id,p.product_name,rz.area,rz.click_count as click_count,rz.rank,rz.day
  from (
    select rank() over(partition by area order by click_count desc) as rank,area ,product_id,click_count,day
    from temp_product_hot_count
  ) as rz 
  join product_info p on p.product_id = rz.product_id
  where rz.rank <=3;
  drop table temp_product_hot_count;
  exit;
HIVE_EOF

print_msg "[$train_time]热门商品统计目的端已有数据清理"
echo '12abAB'|sudo -u mysqladmin -S /usr/local/mysql/bin/mysql -u$resultTo_user -p$resultTo_pwd -e "delete from $resultTo_db.$resultTo_table where day=$train_time;"

print_msg " 重新[$cfg_hdfs/user/hadoop/warehouse/hot_product/pt=$train_time]导出至[$resultTo_url].[$resultTo_table] ["$train_time"]"
sqoop export \
--connect $resultTo_url \
--username $resultTo_user \
--password $resultTo_pwd \
-m 1 \
--table $resultTo_table \
--fields-terminated-by "\001" \
--export-dir $cfg_dw_hdfs/user/hadoop/warehouse/hot_product/pt=$train_time

print_msg "[$train_time]热门商品统计结束"
}


#帮助信息
helpInfo="train_day [ -day 统计日期(格式:yyyyMMdd) | -daysection 统计起始时间(格式:yyyyMMdd) 统计结束时间(格式:yyyyMMdd)]"

#获取参数
if [[ $# -ge 1 ]]; then
  opt="$1"
  shift
  case "$opt" in
    -help)
      print_msg "$helpInfo"
    ;;
    -day)
        targetTime=`date -d "$1" "+%Y%m%d"`
        train_day $targetTime
    ;;
    -daysection)
        beginTime=`date -d "$1" "+%Y%m%d"`
        endTime=`date -d "$2" "+%Y%m%d"`
        while [ "$beginTime" -le "$endTime" ]
        do
            train_day $beginTime
            beginTime=`date -d "$beginTime +1 day " +%Y%m%d`
        done
    ;;
    *)
        print_msg "train_day参数错误 $helpInfo" -err
        exit 1
    ;;
  esac
else
  targetTime=`date -d "-1 day " +%Y%m%d`
  print_msg "默认统计昨日->$targetTime"
  train_day $targetTime
  exit 0
fi

exit 0



