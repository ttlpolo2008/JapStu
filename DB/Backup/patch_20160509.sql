ALTER TABLE `learnlanguage`.`t_reading` ADD COLUMN `TITLE` CHAR(30) NULL DEFAULT NULL  AFTER `LESSON_COURSE_ID` ;
ALTER TABLE `learnlanguage`.`t_conversation` ADD COLUMN `TITLE` CHAR(30) NULL DEFAULT NULL  AFTER `LESSON_COURSE_ID` ;
ALTER TABLE `learnlanguage`.`t_conversation` DROP COLUMN `CONTENT_TRANSLATE` ;