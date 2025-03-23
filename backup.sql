/*!999999\- enable the sandbox mode */ 
-- MariaDB dump 10.19  Distrib 10.6.18-MariaDB, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: bukmacher
-- ------------------------------------------------------
-- Server version	10.6.18-MariaDB-0ubuntu0.22.04.1

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `Kupon`
--

DROP TABLE IF EXISTS `Kupon`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Kupon` (
  `KuponID` int(11) NOT NULL AUTO_INCREMENT,
  `UserID` int(11) NOT NULL,
  `Kurs` decimal(10,2) DEFAULT NULL,
  `Wplata` decimal(10,2) NOT NULL CHECK (`Wplata` >= 0),
  `MozliwaWygrana` decimal(10,2) NOT NULL CHECK (`MozliwaWygrana` >= 0),
  PRIMARY KEY (`KuponID`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `Kupon_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `Ludzie` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Kupon`
--

LOCK TABLES `Kupon` WRITE;
/*!40000 ALTER TABLE `Kupon` DISABLE KEYS */;
INSERT INTO `Kupon` VALUES (5,1,2.73,20.00,48.05),(6,1,1.32,15.00,17.42),(9,1,2.73,20.00,48.05),(10,1,1.32,1.00,1.16),(14,14,2.72,10.00,23.94),(15,1,2.72,50.00,119.68);
/*!40000 ALTER TABLE `Kupon` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `KuponMecze`
--

DROP TABLE IF EXISTS `KuponMecze`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `KuponMecze` (
  `KuponID` int(11) NOT NULL,
  `MeczID` int(11) NOT NULL,
  `Kurs` decimal(5,2) NOT NULL,
  PRIMARY KEY (`KuponID`,`MeczID`),
  KEY `MeczID` (`MeczID`),
  CONSTRAINT `KuponMecze_ibfk_1` FOREIGN KEY (`KuponID`) REFERENCES `Kupon` (`KuponID`) ON DELETE CASCADE,
  CONSTRAINT `KuponMecze_ibfk_2` FOREIGN KEY (`MeczID`) REFERENCES `Mecze` (`MeczID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `KuponMecze`
--

LOCK TABLES `KuponMecze` WRITE;
/*!40000 ALTER TABLE `KuponMecze` DISABLE KEYS */;
INSERT INTO `KuponMecze` VALUES (5,1,2.73),(6,1,1.32),(9,1,2.73),(10,1,1.32),(14,1,2.72),(15,1,2.72);
/*!40000 ALTER TABLE `KuponMecze` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Logowanie`
--

DROP TABLE IF EXISTS `Logowanie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Logowanie` (
  `UserID` int(11) NOT NULL,
  `Login` varchar(50) NOT NULL,
  `Password` varchar(255) NOT NULL,
  UNIQUE KEY `Login` (`Login`),
  KEY `UserID` (`UserID`),
  CONSTRAINT `Logowanie_ibfk_1` FOREIGN KEY (`UserID`) REFERENCES `Ludzie` (`UserID`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Logowanie`
--

LOCK TABLES `Logowanie` WRITE;
/*!40000 ALTER TABLE `Logowanie` DISABLE KEYS */;
INSERT INTO `Logowanie` VALUES (15,'e','$2a$12$uXYO8viJOy/D5dQcoOxWvO5DDfbOPwD9C/CO.YNavkJyM6KRebFqe'),(14,'q','$2a$12$B0R.T/KvUysgvHkrti.4xOVFSNI7VffYiahVR0wOQDk6V8jd/6LEK'),(1,'siemoniere','$2a$12$4G6aPnYFOREQXCMq00r9fe9W28eiS5xn.7LzOGfmsKXG3ZfM3sdBW'),(14,'w','$2a$12$FBmZ57jiADWCnHrUAupMw.2lQkRqUKacGahK3cOSQzLVjNC9IFMOu');
/*!40000 ALTER TABLE `Logowanie` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Ludzie`
--

DROP TABLE IF EXISTS `Ludzie`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Ludzie` (
  `UserID` int(11) NOT NULL AUTO_INCREMENT,
  `Imie` varchar(15) NOT NULL,
  `Nazwisko` varchar(20) NOT NULL,
  `DataUrodzenia` date NOT NULL,
  `StanKonta` decimal(10,2) NOT NULL DEFAULT 0.00 CHECK (`StanKonta` >= 0),
  `isAdmin` tinyint(1) DEFAULT 0,
  PRIMARY KEY (`UserID`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Ludzie`
--

LOCK TABLES `Ludzie` WRITE;
/*!40000 ALTER TABLE `Ludzie` DISABLE KEYS */;
INSERT INTO `Ludzie` VALUES (1,'Szymon','Hladyszewski','2005-01-10',155.13,1),(14,'q','q','2000-01-01',110.00,0),(15,'e','e','2000-10-19',0.00,0);
/*!40000 ALTER TABLE `Ludzie` ENABLE KEYS */;
UNLOCK TABLES;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`root`@`localhost`*/ /*!50003 TRIGGER check_age_before_insert
BEFORE INSERT ON Ludzie
FOR EACH ROW
BEGIN
    IF TIMESTAMPDIFF(YEAR, NEW.DataUrodzenia, CURDATE()) < 18 THEN
        SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Użytkownik musi mieć co najmniej 18 lat!';
    END IF;
end */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb3 */ ;
/*!50003 SET character_set_results = utf8mb3 */ ;
/*!50003 SET collation_connection  = utf8mb3_general_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,ERROR_FOR_DIVISION_BY_ZERO,NO_AUTO_CREATE_USER,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
/*!50003 CREATE*/ /*!50017 DEFINER=`bukmacher`@`localhost`*/ /*!50003 TRIGGER before_delete_user
BEFORE DELETE ON Ludzie
FOR EACH ROW
BEGIN
    
    DELETE FROM KuponMecze 
    WHERE KuponID IN (
        SELECT KuponID FROM Kupon WHERE UserID = OLD.UserID
    );

    
    DELETE FROM Kupon WHERE UserID = OLD.UserID;

    
    DELETE FROM Logowanie WHERE UserID = OLD.UserID;

    
END */;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;

--
-- Table structure for table `Mecze`
--

DROP TABLE IF EXISTS `Mecze`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Mecze` (
  `MeczID` int(11) NOT NULL AUTO_INCREMENT,
  `Data` date NOT NULL,
  `Zespol1` int(11) NOT NULL,
  `Zespol2` int(11) NOT NULL,
  `Kurs1` decimal(5,2) NOT NULL CHECK (`Kurs1` >= 1),
  `Kurs2` decimal(5,2) NOT NULL CHECK (`Kurs2` >= 1),
  `KursRemis` decimal(5,2) NOT NULL CHECK (`KursRemis` >= 1),
  `Wynik` enum('1','2','X') DEFAULT NULL,
  PRIMARY KEY (`MeczID`),
  KEY `Zespol1` (`Zespol1`),
  KEY `Zespol2` (`Zespol2`),
  CONSTRAINT `Mecze_ibfk_1` FOREIGN KEY (`Zespol1`) REFERENCES `Zespol` (`ZespolID`),
  CONSTRAINT `Mecze_ibfk_2` FOREIGN KEY (`Zespol2`) REFERENCES `Zespol` (`ZespolID`),
  CONSTRAINT `CONSTRAINT_1` CHECK (`Zespol1` <> `Zespol2`)
) ENGINE=InnoDB AUTO_INCREMENT=12 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Mecze`
--

LOCK TABLES `Mecze` WRITE;
/*!40000 ALTER TABLE `Mecze` DISABLE KEYS */;
INSERT INTO `Mecze` VALUES (1,'2025-10-10',1,2,1.54,2.72,3.00,NULL);
/*!40000 ALTER TABLE `Mecze` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `Zespol`
--

DROP TABLE IF EXISTS `Zespol`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `Zespol` (
  `ZespolID` int(11) NOT NULL AUTO_INCREMENT,
  `Nazwa` varchar(50) NOT NULL,
  PRIMARY KEY (`ZespolID`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `Zespol`
--

LOCK TABLES `Zespol` WRITE;
/*!40000 ALTER TABLE `Zespol` DISABLE KEYS */;
INSERT INTO `Zespol` VALUES (1,'Real Madryt'),(2,'FC Barcelona'),(3,'Bayer Leverkusen'),(4,'Bayern Monachium'),(5,'Borussia Dortmund'),(6,'FC Schalke'),(8,'Juventus'),(9,'AC Milan'),(10,'Pogon Szczecin'),(11,'Slask Wroclaw'),(12,'q'),(13,'w');
/*!40000 ALTER TABLE `Zespol` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2025-03-23 22:15:28
