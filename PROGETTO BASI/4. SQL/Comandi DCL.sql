--comandi DCL
revoke create on schema public from public;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'amministratore') THEN
        create role amministratore superuser login password 'abc123';
    END IF;
END
$$;

DO $$
BEGIN
    IF NOT EXISTS (SELECT 1 FROM pg_roles WHERE rolname = 'utente_generico') THEN
        create role utente_generico login password 'password';
    END IF;
END
$$;

grant select on all tables in schema public to amministratore;
grant select on all tables in schema public to utente_generico;
