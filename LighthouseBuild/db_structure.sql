DROP DATABASE lighthouse;
CREATE DATABASE lighthouse;

CREATE TABLE lighthouse.LighthouseAuthor (
  `name` varchar(255) NOT NULL,
  PRIMARY KEY (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseEntity (
  `id` varchar(255) NOT NULL,
  `fullyQualifiedName` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseClass (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54B36FAEB691686` (`id`),
  CONSTRAINT `FK54B36FAEB691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseExternalClass (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK248E2CA3B691686` (`id`),
  CONSTRAINT `FK248E2CA3B691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseField (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK54DC6730B691686` (`id`),
  CONSTRAINT `FK54DC6730B691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseInterface (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK7A9286FB691686` (`id`),
  CONSTRAINT `FK7A9286FB691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseMethod (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK5270D16BB691686` (`id`),
  CONSTRAINT `FK5270D16BB691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseModifier (
  `id` varchar(255) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6F3D08E1B691686` (`id`),
  CONSTRAINT `FK6F3D08E1B691686` FOREIGN KEY (`id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;


CREATE TABLE lighthouse.LighthouseRelationship (
  `type` int(11) NOT NULL,
  `to_id` varchar(255) NOT NULL DEFAULT '',
  `from_id` varchar(255) NOT NULL DEFAULT '',
  PRIMARY KEY (`from_id`,`to_id`,`type`),
  KEY `FK1AEB674211FF96CA` (`to_id`),
  KEY `FK1AEB6742E7FC4B3B` (`from_id`),
  CONSTRAINT `FK1AEB6742E7FC4B3B` FOREIGN KEY (`from_id`) REFERENCES `lighthouseentity` (`id`),
  CONSTRAINT `FK1AEB674211FF96CA` FOREIGN KEY (`to_id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseEvent (
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
  KEY `FK54D438D02F7797D2` (`author_name`),
  CONSTRAINT `FK54D438D02F7797D2` FOREIGN KEY (`author_name`) REFERENCES `lighthouseauthor` (`name`),
  CONSTRAINT `FK54D438D03432CE02` FOREIGN KEY (`relationship_from_id`, `relationship_to_id`, `relationship_type`) REFERENCES `lighthouserelationship` (`from_id`, `to_id`, `type`),
  CONSTRAINT `FK54D438D0DF44E862` FOREIGN KEY (`entity_id`) REFERENCES `lighthouseentity` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseRepositoryEvent (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `className` varchar(255) DEFAULT NULL,
  `eventTime` datetime DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `versionAffected` bigint(20) DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK6C6020262F7797D2` (`author_name`),
  CONSTRAINT `FK6C6020262F7797D2` FOREIGN KEY (`author_name`) REFERENCES `lighthouseauthor` (`name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

CREATE TABLE lighthouse.LighthouseQuestion (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `subject` varchar(255) DEFAULT NULL,
  `text` varchar(255) DEFAULT NULL,
  `timestamp` datetime DEFAULT NULL,
  `author_name` varchar(255) DEFAULT NULL,
  `lhClass_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FK4E6F41F0DFAA39AE` (`lhClass_id`),
  KEY `FK4E6F41F02F7797D2` (`author_name`),
  CONSTRAINT `FK4E6F41F02F7797D2` FOREIGN KEY (`author_name`) REFERENCES `lighthouseauthor` (`name`),
  CONSTRAINT `FK4E6F41F0DFAA39AE` FOREIGN KEY (`lhClass_id`) REFERENCES `lighthouseclass` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

DELIMITER |
CREATE TRIGGER lighthouse.TRIGGER_ADDING_EVENT BEFORE INSERT ON LighthouseEvent
	FOR EACH ROW
	BEGIN
		SET NEW.timestamp=now();
		IF ( (NEW.isCommitted=TRUE) AND (NEW.timestamp > NEW.committedTime) ) THEN
			SET NEW.timestamp=NEW.committedTime;
		END IF;
	END
|
DELIMITER ;

DROP USER 'lighthouse'@'%';
DROP USER 'lighthouse'@'localhost';

grant all privileges on lighthouse.* to 'lighthouse'@'%' identified by 'light99';
grant all privileges on lighthouse.* to 'lighthouse'@'localhost' identified by 'light99';
