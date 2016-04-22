/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_lesson` (
  `LESSON_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_NO` int(3) DEFAULT NULL,
  `LESSON_NAME` varchar(100) DEFAULT NULL,
  `PASS_SCORE` int(3) DEFAULT NULL,
  PRIMARY KEY (`LESSON_ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_lesson_course` (
  `LESSON_COURSE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_ID` int(11) NOT NULL,
  `COURSE_TYPE` char(1) DEFAULT NULL,
  PRIMARY KEY (`LESSON_COURSE_ID`),
  KEY `T_LESSON_COURCE-T_LESSON` (`LESSON_ID`),
  CONSTRAINT `T_LESSON_COURCE-T_LESSON` FOREIGN KEY (`LESSON_ID`) REFERENCES `t_lesson` (`LESSON_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_conversation` (
  `CONVERSATION_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `CONTENT` text,
  `CONTENT_TRANSLATE` text,
  `CONTENT_FILE` varchar(100) DEFAULT NULL,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`CONVERSATION_ID`),
  KEY `T_CONVERSATION-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_CONVERSATION-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_exercise` (
  `EXERCISE_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_ID` int(11) NOT NULL,
  `QUESTION_TYPE` char(1) DEFAULT NULL,
  `ANSWER_TYPE` char(1) DEFAULT NULL,
  `QUESTION_CONTENT` text,
  `QUESTION_CONTENT_FILE` varchar(100) DEFAULT NULL,
  `MARK` int(5) DEFAULT NULL,
  `TIME` int(5) DEFAULT NULL,
  `ANSWER_CHOOSE` char(5) DEFAULT NULL,
  `ANSWER_1` text,
  `ANSWER_2` text,
  `ANSWER_3` text,
  `ANSWER_4` text,
  `ANSWER_5` text,
  PRIMARY KEY (`EXERCISE_ID`),
  KEY `T_LESSON_QUESTION-T_LESSON_COURSE` (`LESSON_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_grammar` (
  `GRAMMAR_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `SYNTAX` text,
  `EXPLAIN` text,
  `EXAMPLE` text,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`GRAMMAR_ID`),
  KEY `T_GRAMMAR-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_GRAMMAR-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_kanji` (
  `KANJI_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `WORD` varchar(50) DEFAULT NULL,
  `MEANING` varchar(100) DEFAULT NULL,
  `WRITE_FILE` varchar(100) DEFAULT NULL,
  `EXPLAIN` text,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`KANJI_ID`),
  KEY `T_KANJI-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_KANJI-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_learn` (
  `LEARN_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_ID` int(11) NOT NULL,
  `START_DATE` date DEFAULT NULL,
  `END_DATE` date DEFAULT NULL,
  `STATUS` tinyint(1) DEFAULT NULL,
  `COURSE_STATUS` char(6) DEFAULT NULL,
  `EXAM_MARK` int(5) DEFAULT NULL,
  PRIMARY KEY (`LEARN_ID`),
  KEY `T_LEARN-T_LESSON_COURSE` (`LESSON_ID`),
  CONSTRAINT `T_LEARN-T_LESSON_COURSE` FOREIGN KEY (`LESSON_ID`) REFERENCES `t_lesson` (`LESSON_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_listening` (
  `LISTENING_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `CONTENT_FILE` varchar(100) DEFAULT NULL,
  `CONTENT_TRANSLATE` text,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`LISTENING_ID`),
  KEY `T_LISTENING-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_LISTENING-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_reading` (
  `READING_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `CONTENT` text,
  `CONTENT_PRON` text,
  `CONTENT_TRANSLATE` text,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`READING_ID`),
  KEY `T_READING-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_READING-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_user` (
  `USER_ID` int(11) NOT NULL AUTO_INCREMENT,
  `USER_NAME` varchar(20) DEFAULT NULL,
  `PASS_WORD` varchar(100) DEFAULT NULL,
  `USER_TYPE` char(1) NOT NULL DEFAULT '2',
  `NICK_NAME` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`USER_ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `t_vocabulary` (
  `VOCABULARY_ID` int(11) NOT NULL AUTO_INCREMENT,
  `LESSON_COURSE_ID` int(11) NOT NULL,
  `WORD` varchar(50) DEFAULT NULL,
  `MEANING` varchar(100) DEFAULT NULL,
  `PRONUNCE_FILE` varchar(100) DEFAULT NULL,
  `EXPLAIN` text,
  `ORDER_INDEX` int(3) DEFAULT NULL,
  PRIMARY KEY (`VOCABULARY_ID`),
  KEY `T_VOCABULARY-T_LESSON_COURSE` (`LESSON_COURSE_ID`),
  CONSTRAINT `T_VOCABULARY-T_LESSON_COURSE` FOREIGN KEY (`LESSON_COURSE_ID`) REFERENCES `t_lesson_course` (`LESSON_COURSE_ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
