--comandi DCL
revoke create on schema public from public;
create role amministratore superuser nologin;
create role utente_generico nologin;

grant select on all tables in schema public to amministratore;

create user gennaro login in role amministratore password = 'admin';
create user alfonso login in role amministratore password = 'admin';
create user peppe login in role amministratore password = 'admin';
create user mariano login in role amministratore password = 'admin';

grant amministratore to gennaro;
grant amministratore to alfonso;
grant amministratore to peppe;
grant amministratore to mariano;