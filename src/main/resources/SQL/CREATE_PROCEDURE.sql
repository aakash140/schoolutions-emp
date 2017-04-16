--Procedure to insert employee attendance with status '0'.
-- It gets called when a school is opened in a day.
CREATE OR REPLACE PROCEDURE emp_atndnc_proc(
    workday IN DATE)
IS
TYPE array_varchar
IS
  TABLE OF VARCHAR2(20);
  emp_id_array array_varchar;
BEGIN
  SELECT EMP_ID BULK COLLECT
  INTO emp_id_array
  FROM EMP_RCRD
  WHERE EMP_STATUS<>'0';
  FOR i IN 1 .. emp_id_array.COUNT
  LOOP
    INSERT
    INTO EMP_ATNDNC
      (
        WORK_DAY,
        EMP_ID,
        STATUS,
        TMSTMP,
        RCRD_VER
      )
      VALUES
      (
        workday,
        emp_id_array(i),
        '0',
        NULL,
        LOCALTIMESTAMP
      );
  END LOOP;
EXCEPTION
WHEN OTHERS THEN
  raise_application_error(-20001,'An error was encountered while running emp_atndnc_proc - '||SQLCODE||' -ERROR- '||SQLERRM);
END;