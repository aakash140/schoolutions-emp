CREATE TABLE EMP_CNTCT
(
  CNTC_ID         INTEGER                     NOT NULL,
  EMAIL           VARCHAR2(100),
  MOBILE1         VARCHAR2(10)                NOT NULL,
  MOBILE2         VARCHAR2(10),
  HOME            VARCHAR2(15),
  WHATSAPP        VARCHAR2(10),
  RCRD_VER        TIMESTAMP                   NOT NULL,
  
  CONSTRAINT EMP_CNCT_PK PRIMARY KEY(CNTC_ID)
);

CREATE TABLE EMP_RCRD
(
  EMP_ID          VARCHAR2(20)                NOT NULL,
  EMP_FNAME       VARCHAR2(50)                NOT NULL,
  EMP_MNAME       VARCHAR2(50),        
  EMP_LNAME       VARCHAR2(50),        
  EMP_DESIG       VARCHAR2(50),
  EMP_TYP         VARCHAR2(20)                NOT NULL,
  EMP_DEPT        INTEGER                     NOT NULL,
  EMP_GNDR        VARCHAR2(13)                NOT NULL,
  EMP_BG          VARCHAR2(3),
  EMP_DOB         DATE                        NOT NULL,
  EMP_DOJ         DATE                        NOT NULL,
  EMP_FATH_NAME   VARCHAR2(50),
  EMP_MOTH_NAME   VARCHAR2(50),
  EMP_MRTL        NUMBER(1)                   NOT NULL,
  EMP_SPOUS_NAME  VARCHAR2(50),
  EMP_NAT_ID      VARCHAR2(50),
  EMP_PAN_ID      VARCHAR2(50),
  EMP_EXP         NUMERIC(4,2)   DEFAULT 0.0  NOT NULL,
  CNTCT_ID        INTEGER                     NOT NULL,
  EMP_STATUS      VARCHAR2(1)    DEFAULT '1'  NOT NULL,
  RCRD_VER        TIMESTAMP                   NOT NULL,
  
  CONSTRAINT EMP_RCRD_PK PRIMARY KEY(EMP_ID),
  CONSTRAINT EMP_RCRD_FK FOREIGN KEY(CNTCT_ID) REFERENCES EMP_CNTCT(CNTC_ID)
);




CREATE TABLE EMP_LOGIN_CRDN
(
  EMP_ID          VARCHAR2(20)                NOT NULL,
  PWD_HASH        VARCHAR2(200)               NOT NULL,
  FLD_ATMPTS      INTEGER      DEFAULT 0      NOT NULL,
  LST_LOGN_TMSTMP TIMESTAMP,
  RCRD_VER        DATE                        NOT NULL,
  
  CONSTRAINT EMP_LOGIN_PK PRIMARY KEY(EMP_ID),
  CONSTRAINT EMP_LOGIN_FK FOREIGN KEY(EMP_ID) REFERENCES EMP_RCRD(EMP_ID)
);

CREATE TABLE EMP_ADRS
(
  ADRS_ID         INTEGER                     NOT NULL,
  EMP_ID          VARCHAR2(20),
  ADRS_TYPE       VARCHAR2(50)                NOT NULL,
  ADRS_LINE       VARCHAR2(100)               NOT NULL,
  STATE           VARCHAR2(25)                NOT NULL,
  CITY            VARCHAR2(35)                NOT NULL,
  PINCODE         VARCHAR2(6)                 NOT NULL,
  RCRD_VER        TIMESTAMP                   NOT NULL,
  
  CONSTRAINT EMP_ADRS_PK  PRIMARY KEY(ADRS_ID),
  CONSTRAINT EMP_ADRS_FK  FOREIGN KEY(EMP_ID) REFERENCES EMP_RCRD(EMP_ID)
);

CREATE TABLE DOC_RCRD
(
  OWNER_ID        VARCHAR2(20)                NOT NULL,
  DOC_TYPE        INTEGER                     NOT NULL,
  DOC_LOC         VARCHAR2(400)               NOT NULL,
  RCRD_VER        TIMESTAMP                   NOT NULL,
  
  CONSTRAINT DOC_RCRD_PK  PRIMARY KEY(OWNER_ID,DOC_TYPE)
);


CREATE TABLE USER_OTP
(
  EMP_ID          VARCHAR2(50)                NOT NULL,
  OTP             VARCHAR2(10)                NOT NULL,
  OTP_TMSTMP      TIMESTAMP                   NOT NULL,
  ACTV_STATUS     NUMBER(1)      DEFAULT 1    NOT NULL,
  RCRD_VER        TIMESTAMP                   NOT NULL,
  
  CONSTRAINT OTP_PK PRIMARY KEY(EMP_ID),
  CONSTRAINT OTP_FK FOREIGN KEY(EMP_ID) REFERENCES EMP_RCRD(EMP_ID)
);

CREATE TABLE SOD_RCRD
(
  WORK_DAY     DATE                        NOT NULL,
  ADMIN_ID     VARCHAR2(20)                NOT NULL,
  TMSTMP       TIMESTAMP                   NOT NULL,
  RCRD_VER     TIMESTAMP                   NOT NULL,
  
  CONSTRAINT  SOD_RCRD_PK  PRIMARY KEY(WORK_DAY),
  CONSTRAINT  SOD_RCRD_FK  FOREIGN KEY(ADMIN_ID) REFERENCES EMP_RCRD(EMP_ID)
);

CREATE TABLE EMP_ATNDNC
(
  WORK_DAY    DATE                         NOT NULL,
  EMP_ID      VARCHAR2(20)                 NOT NULL,
  STATUS      VARCHAR2(1)   DEFAULT '0'    NOT NULL,
  TMSTMP      TIMESTAMP,
  RCRD_VER    TIMESTAMP                    NOT NULL,
  
  CONSTRAINT  EMP_ATNDNC_PK PRIMARY KEY(WORK_DAY,EMP_ID),
  CONSTRAINT  EMP_ATNDNC_FK_DAY FOREIGN KEY(WORK_DAY) REFERENCES SOD_RCRD(WORK_DAY),
  CONSTRAINT  EMP_ATNDNC_FK_ID FOREIGN KEY(EMP_ID) REFERENCES EMP_RCRD(EMP_ID)
);


--TRIGGER TABLE LOGS
CREATE TABLE TRIGGER_TABLE_LOG
(
  NAME            VARCHAR2(30 BYTE),
  TMSTMP          TIMESTAMP
);

COMMIT;