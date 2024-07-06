-- MySQL dump 10.13  Distrib 8.4.0, for Linux (x86_64)
--
-- Host: localhost    Database: store_db
-- ------------------------------------------------------
-- Server version	8.4.0

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
-- Table structure for table `orders`
--

DROP TABLE IF EXISTS `orders`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `user_id` bigint NOT NULL,
  `amount` double NOT NULL,
  `date_order` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `contact` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `status_order` varchar(50) NOT NULL,
  `date_update` timestamp NOT NULL,
  `payment_reference` bigint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `user_id` (`user_id`),
  CONSTRAINT `orders_ibfk_1` FOREIGN KEY (`user_id`) REFERENCES `users` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders`
--

LOCK TABLES `orders` WRITE;
/*!40000 ALTER TABLE `orders` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `orders_details`
--

DROP TABLE IF EXISTS `orders_details`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `orders_details` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `quantity` bigint NOT NULL,
  `price` double NOT NULL,
  PRIMARY KEY (`id`),
  KEY `order_id` (`order_id`),
  KEY `product_id` (`product_id`),
  CONSTRAINT `orders_details_ibfk_1` FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`) ON DELETE CASCADE,
  CONSTRAINT `orders_details_ibfk_2` FOREIGN KEY (`product_id`) REFERENCES `products` (`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `orders_details`
--

LOCK TABLES `orders_details` WRITE;
/*!40000 ALTER TABLE `orders_details` DISABLE KEYS */;
/*!40000 ALTER TABLE `orders_details` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `products`
--

DROP TABLE IF EXISTS `products`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `products` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `manufacturer` varchar(50) NOT NULL,
  `model` varchar(100) NOT NULL,
  `country_origin` varchar(50) DEFAULT NULL,
  `descript` varchar(255) DEFAULT NULL,
  `price` double NOT NULL,
  `quantity` bigint NOT NULL,
  `is_active` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `products`
--

LOCK TABLES `products` WRITE;
/*!40000 ALTER TABLE `products` DISABLE KEYS */;
INSERT INTO `products` VALUES (1,'Смартфон','Apple','iPhone 15 128 ГБ черный','Китай','6.1\", ядер - 6x(3.46 ГГц), 6 ГБ, 2 SIM, Super Retina XDR, 2556x1179, камера 48+12 Мп, NFC, 5G, GPS, 3349 мА*ч',83999,10,1),(2,'Смартфон','Apple','iPhone 15 Pro 256 ГБ серый','Китай','6.1\", ядер - 6x(3.78 ГГц), 8 ГБ, 2 SIM, Super Retina XDR, 2556x1179, камера 48+12+12 Мп, NFC, 5G, GPS, 3274 мА*ч',139999,25,1),(3,'Смартфон','Apple','iPhone 13 128 ГБ белый','Китай','6.1\", ядер - 6x(3.23 ГГц), 4 ГБ, 1 SIM, Super Retina XDR, 2532x1170, камера 12+12 Мп, NFC, 5G, GPS, 3240 мА*ч',65999,3,1),(4,'Смартфон','Xiaomi','Redmi Note 12 128 ГБ зеленый','China','6.67\", ядер - 8x(2.8 ГГц), 6 ГБ, 2 SIM, AMOLED, 2400x1080, камера 50+8+2 Мп, NFC, 4G, GPS, FM, 5000 мА*ч',15999,17,1),(5,'Смартфон','Samsung','Galaxy A55 5G 256 ГБ фиолетовый','China','6.6\", ядер - 8x(2.75 ГГц), 8 ГБ, 2 SIM, Super AMOLED, 2340x1080, камера 50+12+5 Мп, NFC, 5G, GPS, 5000 мА*ч',43999,8,1),(6,'Смартфон','Samsung','Galaxy S24 Ultra 512 ГБ желтый','China','6.8\", ядер - 8x(3.39 ГГц), 12 ГБ, 2 SIM, Dynamic AMOLED 2X, 3120x1440, камера 200+50+12+10 Мп, NFC, 5G, GPS, 5000 мА*ч',144999,11,1),(7,'Ноутбук','Xiaomi','RedmiBook 15 серый','China','15.6\", Full HD (1920x1080), TN+film, Intel Core i5-11320H, ядра: 4 х 2.5 ГГц, RAM 8 ГБ, SSD 512 ГБ, Intel Iris Xe Graphics, Windows 11 Home',49999,12,1),(8,'Ноутбук','Samsung','Book3 NP750 серый','China','15.6\", Full HD (1920x1080), IPS, Intel Core i7-1355U, ядра: 2 + 8 х 1.7 ГГц + 1.2 ГГц, RAM 16 ГБ, SSD 512 ГБ, Intel Iris Xe Graphics, Windows 11 Home',126999,8,1),(9,'Ноутбук','Apple','MacBook Air золотистый','South Korea','13.3\", 2560x1600, IPS, Apple M1, ядра: 4 + 4 х 3.2 ГГц + 2.1 ГГц, RAM 8 ГБ, SSD 256 ГБ, Apple M1 7-core, macOS',74999,100,1),(10,'Холодильник','Samsung','RS62R50311L/WT белый','Корея','Side by Side, 647 л, внешнее покрытие-стекло, размораживание - No Frost, дисплей, 91.2 см х 178 см х 71.6 см',130999,5,1),(11,'Стиральная машина','Samsung','WW60AG4S00CELP белый','Корея','стирка - 6 кг, фронтальная загрузка, отжим - 1000 об/мин, программ - 12, пар, 56 дБ, 60 см x 85 см x 44.2 см - 51 см',37999,15,1),(12,'Стиральная машина','LG','F2J3NS1W белый','Корея','стирка - 6 кг, фронтальная загрузка, отжим - 1200 об/мин, программ - 10, пар, 55 дБ, 60 см x 85 см x 44 см - 49 см',32999,5,1);
/*!40000 ALTER TABLE `products` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `phone` varchar(50) DEFAULT NULL,
  `address` varchar(255) NOT NULL,
  `email` varchar(50) NOT NULL,
  `pwd` varchar(255) NOT NULL,
  `user_role` varchar(100) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email` (`email`),
  UNIQUE KEY `UK6dotkott2kjsp8vw4d0m25fb7` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (1,'Василий','Чапаев','1234567890','Екатеринбург, проспект Ленина, д.26','a@a.a','$2a$05$5guUDeXU1.sAL7q4q6NzUeGgk4sQxPnXsDJ4FZky2E9dDR6uoHkX.','ROLE_ADMIN, ROLE_USER'),(2,'Петр','Петров','0123456789','Москва, Красная площадь, д.2','u1@u.u','$2a$05$TDLPQ0PKQTEr0lq13lZjWOTdCn2E804FqtFe0no3mm.qIECA83Eum','ROLE_USER'),(3,'Анна','Аннина','0012345678','Москва, Красная площадь, д.2','u2@u.u','$2a$05$TDLPQ0PKQTEr0lq13lZjWOTdCn2E804FqtFe0no3mm.qIECA83Eum','ROLE_USER');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2024-07-04 14:00:12
