-- get tablespace free space info

select df.tablespace_name "Tablespace",
totalusedspace "Used MB",
(df.totalspace - tu.totalusedspace) "Free MB",
df.totalspace "Total MB",
round(100 * ( (df.totalspace - tu.totalusedspace)/ df.totalspace))
"Pct. Free"
from
(select tablespace_name,
round(sum(bytes) / 1048576) TotalSpace
from dba_data_files 
group by tablespace_name) df,
(select round(sum(bytes)/(1024*1024)) totalusedspace, tablespace_name
from dba_segments 
group by tablespace_name) tu
where df.tablespace_name = tu.tablespace_name;



-- create and replace undo tablecpase

create undo tablespace undotbs2 datafile '/opt/app/oracle/oradata/orcl/undotbs02.dbf' size 3G autoextend off;

select * from dba_data_files where tablespace_name = 'UNDOTBS2';

alter system set undo_tablespace = undotbs2;

drop tablespace undotbs1;



-- test database is alive

create table t ( x char(2000), y char(2000), z char(2000) );

insert into t values ( 'x', 'x', 'x' );

begin
for i in 1 .. 500
   loop
       update t set x = i, y = i, z = i;
       commit;
    end loop;
end;
/

select * from t;

drop table t;