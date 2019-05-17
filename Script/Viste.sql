-- Script per la generazione e l'aggiornamento delle viste principali


create or replace function CREAZIONE_VIEW_CLASSIFICA_PILOTI() returns trigger as $$
begin

	create view CLASSIFICA_PILOTI(numero_campionato, codice_pilota, punteggio) as 
		select numero_campionato, codice_pilota, sum(punteggio)
		from risultati
		where numero_campionato = NEW.numero_campionato
		group by codice_pilota
		order by sum(punteggio) desc;
	
return null;
end $$ language plpgsql;

create or replace function CREAZIONE_VIEW_CLASSIFICA_COSTRUTTORI() returns trigger as $$
begin

create view CLASSIFICA_COSTRUTTORI (numero_campionato, nome_scuderia, punteggio) as

	select A.numero_campionato, nome_scuderia, sum(C.punteggio)
	from CLASSIFICA_PILOTI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
	where A.numero_campionato = NEW.numero_campionato
	group by nome_scuderia
	order by sum(C.punteggio) desc;
	
return null;
end $$ language plpgsql;

create trigger CREAZIONE_VIEW_CLASSIFICA_PILOTI 
after insert on campionati
for each statement
execute procedure CREAZIONE_VIEW_CLASSIFICA_PILOTI();

create trigger CREAZIONE_VIEW_CLASSIFICA_COSTRUTTORI
after insert on campionati
for each statement
execute procedure CREAZIONE_VIEW_CLASSIFICA_COSTRUTTORI();

