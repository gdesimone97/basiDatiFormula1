create or replace function INSERIMENTO_RISULTATI() returns trigger as $$
begin
	
	if( 
		(exists (
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
			)
		then
			raise exception 'Punteggi ripetuti';
	else
	
		insert into risultati ( select C.SEDE_PISTA, Ca.NOME_PISTA, R.CODICE_PILOTA_T, R.NUMERO_CAMPIONATO_T, R.PUNTEGGIO_T, R.MIGLIOR_TEMPO_T ,R.TEMPO_QUALIFICA_T, R.RITIRATO_T
								from risultati_temp as R , calendario as C
								where C.NUMERO_GIORNATA = R.NUMERO_GIORNATA_T and R.NUMERO_CAMPIONATO_T = C.NUMERO_CAMPIONATO
								);
			
		delete from risultati_temp;
		
		delete from CLASSIFICA_PILOTI where numero_campionato = NEW.numero_campionato;
		
		insert into CLASSIFICA_PILOTI(
								select numero_campionato, codice_pilota, sum(punteggio)
								from risultati
								where numero_campionato = NEW.numero_campionato
								group by codice_pilota

								order by sum(punteggio) desc
						  );
						  
		delete from CLASSIFICA_SCUDERIE where numero_campionato = NEW.numero_campionato;
		
		insert into CLASSIFICA_SCUDERIE(
								select A.numero_campionato, nome_scuderia, sum(C.punteggio)
								from CLASSIFICA_PILOTI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
								where A.numero_campionato = NEW.numero_campionato
								group by nome_scuderia
								order by sum(C.punteggio) desc
							   );
			
	end if;
	return NEW;
end $$ language plpgsql;

create trigger INSERIMENTO_RISULTATI
after insert on risultati_temp
where ((select count(*) from risultati_temp) = 20) --problema grave qui
execute procedure INSERIMENTO_RISULTATI()
