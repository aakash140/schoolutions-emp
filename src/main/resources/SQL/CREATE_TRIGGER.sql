CREATE OR REPLACE TRIGGER EMP_ATNDNC_TRGR
AFTER INSERT ON SOD_RCRD
FOR EACH ROW
BEGIN
  emp_atndnc_proc(:NEW.WORK_DAY);

EXCEPTION
  WHEN OTHERS THEN
   INSERT INTO TRIGGER_TABLE_LOG(NAME,TMSTMP) VALUES('EMP_ATNDNC_TRGR',LOCALTIMESTAMP);
   raise_application_error(-20001,'An error was encountered while running EMP_ATNDNC_TRGR - '||SQLCODE||' -ERROR- '||SQLERRM); 
END; 