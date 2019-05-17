
-- ------------------------------------------------------------------------------- --

-- procedura per aggiornare il miglior tempo di una pista dopo l'inserimento di nuovi risultati	(POTENZIALMENTE NON FUNZIONANTE)
create or replace function AGGIORNAMENTO_TEMPO_PISTA() returns trigger as $$
begin

	if( (select min(miglior_tempo)
		from NUOVI_RISULTATI) < (select tempo_giro
								from piste
								where sede_pista = NUOVI_RISULTATI.sede_pista and nome_pista = NUOVI_RISULTATI.nome_pista)
	   )						  
	then
		update piste
		set giro_veloce =  (select min(miglior_tempo)
			  				from NUOVI_RISULTATI)
		where sede_pista = NUOVI_RISULTATI.sede_pista and nome_pista = NUOVI_RISULTATI.nome_pista
	end if;
			   
return NULL;
end $$ language plpgsql;


-- ------------------------------------------------------------------------------- --


-- trigger per controllare che i punteggi dei risultati inseriti di una giornata non siano sovrapposti (fa anche partire l'update dei tempi)
create or replace function CONTROLLO_PUNTEGGI() returns trigger as $$
begin
	-- controllare che ci siano 20 risultati
	if( 20 <> (	select count(*)
				from NUOVI_RISULTATI
				)) 
		then raise exception 'Numero risultati non corretto.';
	end if;
	-- controllare che siano tutti dello stesso campionato e sulla stessa pista
	if ( not exists (
			select *
			from NUOVI_RISULTATI
			group by numero_campionato, sede_pista, nome_pista
			having count(*) = 20 ))
		then raise exception 'Inseriti risultati su più giornate.';
	end if;
	-- controllare che non ci siano punteggi ripetuti (tranne 0)
	if( exists (
			select *
			from NUOVI_RISULTATI
			where punteggio <> 0
			group by punteggio
			having count(*) > 1))
		then raise exception 'Inseriti punteggi ripetuti.';
	end if;	
	execute procedure AGGIORNAMENTO_TEMPO_PISTA(); -- nel caso in cui questa chiamata non funzioni (perché nella procedure c'è un riferimento a NUOVI_RISULTATI) basta ricopiare il suo codice qui
return NULL;
end $$ language plpgsql;

create trigger CONTROLLO_PUNTEGGI
after insert on risultati
referencing new table as NUOVI_RISULTATI
for each statement
execute procedure CONTROLLO_PUNTEGGI();


-- ------------------------------------------------------------------------------- --





