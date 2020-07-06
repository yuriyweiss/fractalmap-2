create user FRACTALMAP identified by fractalmap default tablespace FRCTMPD temporary tablespace TEMP;

grant connect, resource to FRACTALMAP;

grant select any table to FRACTALMAP;