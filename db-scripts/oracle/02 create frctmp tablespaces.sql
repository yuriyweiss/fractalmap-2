-- data
create tablespace FRCTMPD
datafile '/opt/app/oracle/oradata/orcl/frctmpd01.dbf' 
size 500M reuse autoextend on next 100M maxsize 5G
nologging;

select * from dba_data_files where tablespace_name = 'FRCTMPD';

drop tablespace FRCTMPD;

-- indexes
create tablespace FRCTMPI
datafile '/opt/app/oracle/oradata/orcl/frctmpi01.dbf'
size 100M reuse autoextend on next 50M maxsize 3G
nologging;