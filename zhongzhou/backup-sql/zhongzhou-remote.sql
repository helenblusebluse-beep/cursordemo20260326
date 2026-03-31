-- MySQL dump 10.13  Distrib 8.0.34, for Win64 (x86_64)
--
-- Host: 192.168.81.155    Database: zhongzhou
-- ------------------------------------------------------
-- Server version	5.7.27

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `zz_visit_appointment`
--

DROP TABLE IF EXISTS `zz_visit_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zz_visit_appointment` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `reserve_type` tinyint(4) NOT NULL,
  `visitor_name` varchar(32) NOT NULL,
  `visitor_phone` varchar(20) NOT NULL,
  `elder_name` varchar(32) NOT NULL,
  `scheduled_time` datetime NOT NULL,
  `confirmed_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_visit_status` (`status`),
  KEY `idx_visit_scheduled_time` (`scheduled_time`),
  KEY `idx_visit_visitor_phone` (`visitor_phone`)
) ENGINE=InnoDB AUTO_INCREMENT=23 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zz_visit_appointment`
--

LOCK TABLES `zz_visit_appointment` WRITE;
/*!40000 ALTER TABLE `zz_visit_appointment` DISABLE KEYS */;
INSERT INTO `zz_visit_appointment` VALUES (1,1,'安权','13875568891','安权','2026-03-28 10:00:00',NULL,1,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(2,2,'高启强','13875568892','高启强','2026-03-18 09:30:00',NULL,1,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(3,1,'陈布','13875568893','陈布','2026-03-12 15:00:00','2026-03-12 15:10:00',2,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(4,2,'李自强','13875568894','李自强','2026-03-15 14:00:00','2026-03-15 14:20:00',2,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(5,1,'安艾林','13875568895','安艾林','2026-03-16 11:00:00',NULL,3,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(6,2,'徐江','13875568896','徐江','2026-03-17 16:00:00',NULL,3,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(7,1,'唐小龙','13875568897','唐小龙','2026-03-30 09:00:00',NULL,1,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(8,2,'唐小虎','13875568898','唐小虎','2026-03-10 13:30:00','2026-03-10 13:30:00',2,0,'2026-03-24 16:16:51','2026-03-24 16:19:18'),(9,1,'陈书婷','13875568899','陈书婷','2026-03-13 10:20:00','2026-03-13 10:45:00',2,0,'2026-03-24 16:16:51','2026-03-24 16:16:51'),(10,2,'杨健','13875568900','杨健','2026-03-11 08:40:00',NULL,3,0,'2026-03-24 16:16:51','2026-03-24 16:16:51');
/*!40000 ALTER TABLE `zz_visit_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `zz_visit_record`
--

DROP TABLE IF EXISTS `zz_visit_record`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `zz_visit_record` (
  `id` bigint(20) unsigned NOT NULL AUTO_INCREMENT,
  `appointment_id` bigint(20) unsigned NOT NULL,
  `reserve_type` tinyint(4) NOT NULL DEFAULT '1' COMMENT '预约类型:1日常探访 2节日探访 3医疗陪护',
  `visitor_name` varchar(32) NOT NULL,
  `visitor_phone` varchar(20) NOT NULL,
  `elder_name` varchar(32) NOT NULL,
  `checkin_time` datetime NOT NULL,
  `checkout_time` datetime DEFAULT NULL,
  `status` tinyint(4) NOT NULL DEFAULT '1',
  `is_deleted` tinyint(4) NOT NULL DEFAULT '0',
  `created_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `idx_visit_record_appointment` (`appointment_id`),
  KEY `idx_visit_record_status` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=38 DEFAULT CHARSET=utf8mb4;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `zz_visit_record`
--

LOCK TABLES `zz_visit_record` WRITE;
/*!40000 ALTER TABLE `zz_visit_record` DISABLE KEYS */;
INSERT INTO `zz_visit_record` VALUES (6,0,1,'王建国','13900010001','张桂兰','2026-03-01 09:20:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(7,0,2,'李慧芳','13900010002','孙德华','2026-03-01 14:10:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(8,0,1,'赵明','13900010003','刘淑珍','2026-03-02 10:30:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(9,0,2,'周婷','13900010004','陈玉兰','2026-03-02 16:00:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(10,0,1,'吴浩','13900010005','黄志强','2026-03-03 08:45:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(11,0,2,'郑丽','13900010006','邓春梅','2026-03-03 15:40:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(12,0,1,'冯涛','13900010007','宋秋菊','2026-03-04 09:55:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(13,0,2,'褚敏','13900010008','谢广平','2026-03-04 13:20:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(14,0,1,'卫星','13900010009','韩翠英','2026-03-05 11:00:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(15,0,2,'蒋燕','13900010010','崔桂香','2026-03-05 17:30:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(16,0,1,'沈洋','13900010011','贾美玲','2026-03-06 09:05:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(17,0,2,'韩雪','13900010012','何玉珍','2026-03-06 14:25:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(18,0,1,'杨凯','13900010013','高秀兰','2026-03-07 10:15:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(19,0,2,'朱红','13900010014','林德福','2026-03-07 16:45:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(20,0,1,'秦峰','13900010015','梁凤英','2026-03-08 08:30:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(21,0,2,'尤佳','13900010016','罗建华','2026-03-08 13:55:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(22,0,1,'许宁','13900010017','毕淑华','2026-03-09 09:40:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(23,0,2,'何倩','13900010018','郝春兰','2026-03-09 15:10:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(24,0,1,'吕阳','13900010019','康玉芬','2026-03-10 10:50:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(25,0,2,'施静','13900010020','毛志远','2026-03-10 18:00:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(26,0,1,'张强','13900010021','闫桂芝','2026-03-11 09:30:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(27,0,2,'孔琳','13900010022','苏玉英','2026-03-11 14:40:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(28,0,1,'曹鹏','13900010023','潘志兰','2026-03-12 10:20:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(29,0,2,'严雪','13900010024','田建军','2026-03-12 16:35:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(30,0,1,'华晨','13900010025','董淑芬','2026-03-13 08:50:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(31,0,2,'金婧','13900010026','袁桂琴','2026-03-13 15:25:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(32,0,1,'魏涛','13900010027','谭建民','2026-03-14 09:10:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(33,0,2,'陶敏','13900010028','廖秀英','2026-03-14 17:05:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(34,0,1,'姜洋','13900010029','薛明珠','2026-03-15 10:00:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(35,0,2,'戚璐','13900010030','雷广顺','2026-03-15 14:15:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(36,0,1,'邹杰','13900010031','倪爱华','2026-03-16 09:35:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59'),(37,0,2,'喻娜','13900010032','汤翠兰','2026-03-16 16:20:00',NULL,1,0,'2026-03-24 16:27:59','2026-03-24 16:27:59');
/*!40000 ALTER TABLE `zz_visit_record` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping routines for database 'zhongzhou'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2026-03-24 16:43:23
