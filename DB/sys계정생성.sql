-- SYS 관리자 계정

ALTER SESSION SET "_ORACLE_SCRIPT" = TRUE;


-- 사용자 계정 생성
CREATE USER member_sj IDENTIFIED BY member1234;

-- 생성한 사용자 계정에 권한 부여
GRANT CONNECT, RESOURCE, CREATE VIEW TO member_sj;

-- 테이블 스페이스 할당
ALTER USER member_sj DEFAULT
TABLESPACE SYSTEM QUOTA UNLIMITED ON SYSTEM;

