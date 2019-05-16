-- Script per la generazione e l'aggiornamento delle viste principali

create view CLASSIFICA_PILOTI(codice_pilota, punteggio) as 
	select codice_pilota, sum(punteggio)
	from risultati
	where numero_campionato = NEW.numero_campionato
	group by codice_pilota
	order by sum(punteggio) desc

create view CLASSIFICA_COSTRUTTORI (nome_scuderia, punteggio) as
	select nome_scuderia, sum(C.punteggio)
	from CLASSIFICA_PILOTI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
	where A.numero_campionato = NEW.numero_campionato
	group by nome_scuderia
	order by sum(C.punteggio) desc

--fare trigger su campionato insert
--aggiornare trigger di risultati aggiungendo le due query

create or replace function CREAZIONE_VIEW_CLASSIFICA_PILOTI() returns trigger as $$
begin
	
return null;
end $$ language plpgsql;