--comandi DCL
revoke create on schema public from public;
drop role amministratore;
drop role utente_generico;
create role amministratore superuser login password 'abc123';
create role utente_generico login password 'abc123';

grant select on all tables in schema public to amministratore;
grant select on all tables in schema public to utente_generico;
