drop trigger if exists CONTROLLO_CARDINALITA_PERSONALE on personale cascade;
drop trigger if exists CONTROLLO_CARDINALITA_SCUDERIE on scuderie cascade;
drop trigger if exists CONTROLLO_AMMINISTRATORE on dirigenza cascade;
drop trigger if exists CONTROLLO_PUNTEGGI on risultati_attuali cascade;

-- trigger per esprimere la cardinalità minima 1 su scuderie
create or replace function CONTROLLO_CARDINALITA_SCUDERIE() returns trigger as $$
begin
	if( (not exists ( select * from afferenza_personale where nome_scuderia = NEW.nome_scuderia))  
	  or
		(not exists ( select * from dirigenza where nome_scuderia = NEW.nome_scuderia)))
	then
		raise exception 'Cardinalità scuderie non rispettata.';
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
		raise exception 'Cardinalità personale non rispettata.';
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
create or replace function AGGIORNAMENTO_RISULTATI() returns trigger as $$
declare
	miglior_tempo_TMP int;
	sede_pista_TMP varchar(50);
	nome_pista_TMP varchar(50);
begin
	-- controllare che ci siano 20 risultati
	if( 20 <> (	select count(*)
				from NUOVI_RISULTATI
				)) 
		then raise exception 'Numero risultati non corretto.';
	end if;
	-- controllare che siano tutti dello stesso campionato e sulla stessa pista
	if ( 0 = (
			select count(*)
			from NUOVI_RISULTATI
			group by numero_campionato, sede_pista, nome_pista
			having count(*) = 20 ))
		then raise exception 'Inseriti risultati su più giornate.';
	end if;
	-- controllare che non ci siano punteggi ripetuti (tranne 0)
	if ( exists (
			select '', '', '', ''
			from NUOVI_RISULTATI
			where punteggio <> 0
			group by punteggio
			having count(*) > 1
			) )
		then raise exception 'Inseriti punteggi ripetuti.';
	end if;
	
	-- aggiornamento record della pista
	select distinct sede_pista into sede_pista_TMP
	from NUOVI_RISULTATI;
	select distinct nome_pista into nome_pista_TMP
	from NUOVI_RISULTATI;
	select min(miglior_tempo) into miglior_tempo_TMP
		from NUOVI_RISULTATI;
	if( miglior_tempo_TMP < (select distinct giro_veloce
								from piste p, NUOVI_RISULTATI n
								where p.sede_pista = n.sede_pista and p.nome_pista = n.nome_pista)
	   )						  
	then
		
		update piste
		set giro_veloce =  miglior_tempo_TMP
		where sede_pista = sede_pista_TMP and nome_pista = nome_pista_TMP;
	end if;
			   
return NULL;
end $$ language plpgsql;

create trigger AGGIORNAMENTO_RISULTATI
after insert on risultati_attuali
referencing new table as NUOVI_RISULTATI
for each statement
execute procedure AGGIORNAMENTO_RISULTATI();
