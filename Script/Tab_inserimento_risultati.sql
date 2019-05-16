--  Tabella temporanea per caricare i risultati

create table if not exists risultati_temp(
	CODICE_PILOTA_T        CHAR(8)              not null,
	PUNTEGGIO_T            INT                  not null,
	MIGLIOR_TEMPO_T        TempoGiro            null,
	TEMPO_QUALIFICA_T      TempoGiro            null,
	RITIRATO_T             BOOL                 not null,
	NUMERO_GIORNATA_T	   INT 					not null,
	NUMERO_CAMPIONATO_T	   INT 					not null,
	constraint PK_RISULTATI_T primary key (CODICE_PILOTA_T, NUMERO_CAMPIONATO_T, NUMERO_GIORNATA_T),
	check(punteggio_t between 0 and 25),
	check((punteggio_t <> 0 and not ritirato_t) or (punteggio_t = 0 and ritirato_t))
);

create or replace function INSERIMENTO_RISULTATI() returns trigger as $$
begin
	
	if( (exists (
			select * 
			from risultati_temp 
			where punteggio_t <> 0
			group by punteggio_t
			having count(*) > 1))
		and
		(not exists ( 
			select *
			from risultati_temp
			group by NUMERO_GIORNATA_T, NUMERO_CAMPIONATO_T
			having count(*) = 20))
			
		then
			raise exception 'Punteggi ripetuti';
	else
	
		insert into risultati ( select C.SEDE_PISTA, C.NOME_PISTA, R.CODICE_PILOTA_T, R.NUMERO_CAMPIONATO_T, R.PUNTEGGIO_T, R.MIGLIOR_TEMPO_T R.TEMPO_QUALIFICA_T, R.RITIRATO_T
								from risultati_temp as R join calendario as C
								where C.NUMERO_GIORNATA = R.NUMERO_GIORNATA_T and R.NUMERO_CAMPIONATO_T = C.NUMERO_CAMPIONATO
								);
			
		delete from risultati_temp;
		
		delete from CLASSIFICA_PILOTI;
		insert into CLASSIFICA_PILOTI(
								select codice_pilota, sum(punteggio)
								from risultati
								where numero_campionato = NEW.numero_campionato
								group by codice_pilota
								order by sum(punteggio) desc;
						  );
		delete from CLASSIFICA_SCUDERIE;
		insert into CLASSIFICA_SCUDERIE(
								select nome_scuderia, sum(C.punteggio)
								from CLASSIFICA_PILOTI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
								where A.numero_campionato = NEW.numero_campionato
								group by nome_scuderia
								order by sum(C.punteggio) desc;
							   );
			
	end if;
	return NEW;
end $$ language plpgsql;

create trigger INSERIMENTO_RISULTATI
after insert on risultati_temp
when ((select count(*) from risultati_temp) = 20)
execute procedure INSERIMENTO_RISULTATI()
