set path=C:\Program Files (x86)\MySQL\MySQL Workbench 5.2 CE\

set sqlfileFolder=C:\Users\trungln\Desktop\VTien\DB\Backup\
set sqlfile_struct=LearnLanguage_Struct.sql
set sqlfile_data=LearnLanguage_Data.sql

cd %path%

mysqldump --no-data --compact -uroot -proot LearnLanguage > %sqlfileFolder%%sqlfile_struct%
mysqldump --skip-triggers --compact --no-create-info -uroot -proot LearnLanguage > %sqlfileFolder%%sqlfile_data%

pause