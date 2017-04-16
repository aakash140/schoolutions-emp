CREATE ROLE DBAROLE;

GRANT DBA,
CREATE TABLE,
CREATE VIEW,
CREATE SEQUENCE,
CREATE PROCEDURE,
ALTER SESSION,
CONNECT,
SELECT_CATALOG_ROLE TO DBAROLE;


CREATE USER SCHOOLADMIN
IDENTIFIED BY admin_123#
DEFAULT TABLESPACE USERS
TEMPORARY TABLESPACE TEMP
QUOTA UNLIMITED ON USERS;

GRANT DBAROLE TO SCHOOLADMIN;

COMMIT;