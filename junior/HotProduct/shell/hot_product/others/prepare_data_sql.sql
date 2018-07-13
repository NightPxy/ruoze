#产品库信息
create database if not exists ruoze_product;
use ruoze_product;
create table if not exists ruoze_product.product_info (
 product_id int, 
 product_name varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci, 
 extend_info varchar(255) character set utf8mb4 collate utf8mb4_unicode_ci
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;
insert  into product_info(product_id,product_name,extend_info) values (1,'product1','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (2,'product2','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (3,'product3','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (4,'product4','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (5,'product5','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (6,'product6','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (7,'product7','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (8,'product8','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (9,'product9','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (10,'product10','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (11,'product11','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (12,'product12','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (13,'product13','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (14,'product14','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (15,'product15','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (16,'product16','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (17,'product17','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (18,'product18','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (19,'product19','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (20,'product20','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (21,'product21','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (22,'product22','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (23,'product23','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (24,'product24','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (25,'product25','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (26,'product26','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (27,'product27','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (28,'product28','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (29,'product29','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (30,'product30','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (31,'product31','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (32,'product32','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (33,'product33','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (34,'product34','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (35,'product35','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (36,'product36','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (37,'product37','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (38,'product38','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (39,'product39','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (40,'product40','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (41,'product41','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (42,'product42','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (43,'product43','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (44,'product44','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (45,'product45','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (46,'product46','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (47,'product47','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (48,'product48','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (49,'product49','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (50,'product50','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (51,'product51','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (52,'product52','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (53,'product53','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (54,'product54','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (55,'product55','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (56,'product56','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (57,'product57','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (58,'product58','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (59,'product59','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (60,'product60','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (61,'product61','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (62,'product62','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (63,'product63','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (64,'product64','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (65,'product65','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (66,'product66','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (67,'product67','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (68,'product68','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (69,'product69','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (70,'product70','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (71,'product71','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (72,'product72','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (73,'product73','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (74,'product74','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (75,'product75','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (76,'product76','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (77,'product77','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (78,'product78','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (79,'product79','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (80,'product80','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (81,'product81','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (82,'product82','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (83,'product83','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (84,'product84','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (85,'product85','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (86,'product86','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (87,'product87','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (88,'product88','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (89,'product89','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (90,'product90','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (91,'product91','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (92,'product92','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (93,'product93','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (94,'product94','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (95,'product95','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (96,'product96','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (97,'product97','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (98,'product98','{"product_status":1}');
insert  into product_info(product_id,product_name,extend_info) values (99,'product99','{"product_status":0}');
insert  into product_info(product_id,product_name,extend_info) values (100,'product100','{"product_status":1}');
update product_info set product_name = concat('不规则特殊字符:产\t\n\r\001.,;~品',product_name);
#基础库信息
create database if not exists ruoze_basedata;
use ruoze_basedata;
create table if not exists ruoze_basedata.city_info (
 city_id int, 
 city_name varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci, 
 area varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;
insert  into `city_info`(`city_id`,`city_name`,`area`) values (1,'BEIJING','NC'),(2,'SHANGHAI','EC'),(3,'NANJING','EC'),(4,'GUANGZHOU','SC'),(5,'SANYA','SC'),(6,'WUHAN','CC'),(7,'CHANGSHA','CC'),(8,'XIAN','NW'),(9,'CHENGDU','SW'),(10,'HAERBIN','NE');
#统计结果表
create database if not exists ruoze;
use ruoze;
create table if not exists ruoze.hot_product(
 product_id int,
 product_name varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci, 
 area varchar(50) character set utf8mb4 collate utf8mb4_unicode_ci,
 click_count int,
 rank int,
 day varchar(50)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4 AUTO_INCREMENT=1;
