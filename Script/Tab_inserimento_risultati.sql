--  Tabella temporanea per caricare i risultati

create table if not exists risultati_temp(
	CODICE_PILOTA_T        CHAR(8)              not null,
	PUNTEGGIO_T            INT                  not null,
	MIGLIOR_TEMPO_T        TempoGiro            null,
	TEMPO_QUALIFICA_T      TempoGiro            null,
	RITIRATO_T             BOOL                 not null,
	NUMERO_GIORNATA_T	   INT 					not null,
	NUMERO_CAMPIONATO_T	   INT 					not null,
	constraint PK_RISULTATI primary key (CODICE_PILOTA_T, NUMERO_CAMPIONATO_T, NUMERO_GIORNATA_T),
	check(punteggio between 0 and 25),
	check((punteggio <> 0 and not ritirato) or (punteggio = 0 and ritirato))
);


create or replace function INSERIMENTO_RISULTATI() as trigger $$
being
	
	if( (exists (
			select * 
			from risultati_temp 
			where punteggio <> 0
			group by punteggio
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
		delete * from risultati_temp;
	end if;
	
	
end $$; language plpgsql;

create trigger INSERIMENTO_RISULTATI
after insert on risultati_temp
when ((select count(*) from risultati_temp) = 20)
execute procedure INSERIMENTO_RISULTATI()


 