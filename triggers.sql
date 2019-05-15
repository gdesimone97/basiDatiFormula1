drop trigger if exists CONTROLLO_CARDINALITA_PERSONALE on personale cascade;
drop trigger if exists CONTROLLO_CARDINALITA_SCUDERIE on scuderie cascade;
drop trigger if exists CONTROLLO_AMMINISTRATORE on dirigenza cascade;
drop trigger if exists CONTROLLO_PUNTEGGI on risutlati cascade;

-- trigger per esprimere la cardinalità minima 1 su scuderie
create or replace function CONTROLLO_CARDINALITA_SCUDERIE() returns trigger as $$
begin
	if( (not exists ( select * from afferenza_personale where nome_scuderia = NEW.nome_scuderia))  
	  or
		(not exists ( select * from dirigenza where nome_scuderia = NEW.nome_scuderia)))
	then
		delete from scuderie where nome_scuderia = NEW.nome_scuderia;
	end if;
return null;
end $$ language plpgsql;

create constraint trigger CONTROLLO_CARDINALITA_SCUDERIE
after insert on scuderie
deferrable initially deferred					
for each row
execute procedure CONTROLLO_CARDINALITA_SCUDERIE();
					 
-- trigger per esprimere la cardinalità minima 1 su personale
create or replace function CONTROLLO_CARDINALITA_PERSONALE() returns trigger as $$
begin
	if(not exists ( select * from afferenza_personale where codice_personale = NEW.codice_personale)) 
	then
		delete from personale where codice_personale = NEW.codice_personale;
	end if;
return null;
end $$ language plpgsql;

create constraint trigger CONTROLLO_CARDINALITA_PERSONALE
after insert on personale
deferrable initially deferred
for each row
execute procedure CONTROLLO_CARDINALITA_PERSONALE();

-- trigger per controllare che l'amministratore delegato sia un dirigente
create or replace function CONTROLLO_AMMINISTRATORE() returns trigger as $$
begin
	if(not exists (select professione from personale where codice_personale = NEW.codice_personale and professione = 'dirigente')) then
	raise exception 'Amministratore Delegato inserito non è un dirigente.';
	end if;
return NEW;
end $$ language plpgsql;

create trigger CONTROLLO_AMMINISTRATORE
before insert on dirigenza
for each row
execute procedure CONTROLLO_AMMINISTRATORE();
				   
-- trigger per controllare che i punteggi dei risultati inseriti di una giornata non siano sovrapposti
create or replace function CONTROLLO_PUNTEGGI() returns trigger as $$
begin
	if((
	select punteggio from risultati where NEW.codice_pilota <> codice_pilota and NEW.sede_pista = sede_pista and NEW.nome_pista  = nome_pista  and NEW.numero_campionato = numero_campionato and NEW.punteggio = punteggio
	) <> 0) then
	raise exception 'Punteggi sovrapposti';
	end if;
return NEW;
end $$ language plpgsql;

create trigger CONTROLLO_PUNTEGGI
before insert on risultati
for each row
execute procedure CONTROLLO_PUNTEGGI();

