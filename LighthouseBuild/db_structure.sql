
DROP DATABASE lighthouse;
CREATE DATABASE lighthouse;
USE lighthouse;

# Sequel Pro dump
# Version 2492
# http://code.google.com/p/sequel-pro
#
# Host: 127.0.0.1 (MySQL 5.1.51)
# Database: lighthouse
# Generation Time: 2010-11-18 15:56:22 -0800
# ************************************************************

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


# Dump of table LighthouseAuthor
# ------------------------------------------------------------

CREATE TABLE `LighthouseAuthor` (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseClass
# ------------------------------------------------------------

CREATE TABLE `LighthouseClass` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54B36FAEB691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseEntity
# ------------------------------------------------------------

CREATE TABLE `LighthouseEntity` (
  `id` varchar(255) NOT NULL,
  `fullyQualifiedName` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseEvent
# ------------------------------------------------------------

CREATE TABLE `LighthouseEvent` (
  `id` varchar(255) NOT NULL,
  `committedTime` datetime DEFAULT NULL,
  `isCommitted` bit(1) NOT NULL,
  `timestamp` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `entity_id` varchar(255) DEFAULT NULL,
  `relationship_from_id` varchar(255) DEFAULT NULL,
  `relationship_to_id` varchar(255) DEFAULT NULL,
  `relationship_type` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54D438D0DF44E862` (`entity_id`),
  KEY `FK54D438D03432CE02` (`relationship_from_id`,`relationship_to_id`,`relationship_type`),
  KEY `FK54D438D02F7797D2` (`author_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;


/*@author: Lee
*This is trigger has been added to update LighthouseEvent timestamps with the system time
*You will see someone had a similar idea when adding an event below, but they commented it out. 
*/
CREATE TRIGGER systemTimeUpdate BEFORE UPDATE ON LighthouseEvent
FOR EACH ROW SET NEW.timestamp = now();




DELIMITER ;;
/*!50003 SET SESSION SQL_MODE="" */;;
/*!50003 CREATE */ /*!50017 DEFINER=`root`@`localhost` */ /*!50003 TRIGGER `TRIGGER_ADDING_EVENT` BEFORE INSERT ON `LighthouseEvent` FOR EACH ROW BEGIN
		SET NEW.timestamp=now();
		IF ( (NEW.isCommitted=TRUE) AND (NEW.timestamp > NEW.committedTime) ) THEN
			SET NEW.timestamp=NEW.committedTime;
		END IF;
	END */;;
DELIMITER ;
/*!50003 SET SESSION SQL_MODE=@OLD_SQL_MODE */;


# Dump of table LighthouseExternalClass
# ------------------------------------------------------------

CREATE TABLE `LighthouseExternalClass` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK248E2CA3B691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseField
# ------------------------------------------------------------

CREATE TABLE `LighthouseField` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54DC6730B691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseInterface
# ------------------------------------------------------------

CREATE TABLE `LighthouseInterface` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7A9286FB691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseMethod
# ------------------------------------------------------------

CREATE TABLE `LighthouseMethod` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5270D16BB691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseModifier
# ------------------------------------------------------------

CREATE TABLE `LighthouseModifier` (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6F3D08E1B691686` (`id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseQuestion
# ------------------------------------------------------------

CREATE TABLE `LighthouseQuestion` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `lhClass_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4E6F41F0DFAA39AE` (`lhClass_id`),
  KEY `FK4E6F41F02F7797D2` (`author_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseRelationship
# ------------------------------------------------------------

CREATE TABLE `LighthouseRelationship` (
  `type` int(11) NOT NULL,
  `to_id` varchar(255) NOT NULL DEFAULT '',
  `from_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`from_id`,`to_id`,`type`),
  KEY `FK1AEB674211FF96CA` (`to_id`),
  KEY `FK1AEB6742E7FC4B3B` (`from_id`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;



# Dump of table LighthouseRepositoryEvent
# ------------------------------------------------------------

CREATE TABLE `LighthouseRepositoryEvent` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(255) DEFAULT NULL,
  `eventTime` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `versionAffected` bigint(20) DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6C6020262F7797D2` (`author_name`)
) ENGINE=MyISAM DEFAULT CHARSET=latin1;






/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

DROP USER 'lighthouse'@'%';
DROP USER 'lighthouse'@'localhost';

grant all privileges on lighthouse.* to 'lighthouse'@'%' identified by 'light99';
grant all privileges on lighthouse.* to 'lighthouse'@'localhost' identified by 'light99';
