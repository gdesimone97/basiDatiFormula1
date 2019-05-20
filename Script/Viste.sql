
-- le due viste "attuali" vengono aggiornate ad ogni inserimento su risultati_attuali

create view CLASSIFICA_PILOTI_ATTUALE(codice_pilota, punteggio) as 
		select codice_pilota, sum(punteggio) as punti
		from risultati_attuali
		where numero_campionato = ( select max(numero_campionato)
									from campionati )
		group by codice_pilota
		order by punti desc;

create view CLASSIFICA_COSTRUTTORI_ATTUALE (nome_scuderia, punteggio) as
	select nome_scuderia, sum(C.punteggio) as punti
	from CLASSIFICA_PILOTI_ATTUALE as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
	where A.numero_campionato = ( select max(numero_campionato)
								from campionati )
	group by nome_scuderia
	order by punti desc;
	
-- le due viste "passate" vengono ricalcolate ogni volta che il campionato termina
-- (ovvero dopo che il trigger sposta i 420 risultati_attuali in risultati_passati) (operazione costosa ma fatta 1 V/A)

create view CLASSIFICHE_PILOTI_PASSATI(numero_campionato, codice_pilota, punteggio) as 
		select codice_pilota, sum(punteggio) as punti
		from risultati_passati
		group by numero_campionato, codice_pilota
		order by punti desc;
	
create view CLASSIFICHE_COSTRUTTORI_PASSATE (numero_campionato, nome_scuderia, punteggio) as
	select nome_scuderia, sum(C.punteggio) as punti
	from CLASSIFICHE_PILOTI_PASSATI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
	where A.numero_campionato = C.numero_campionato
	group by numero_campionato, nome_scuderia
	order by punti desc;
	
	
-- aggiungere codice DCL per limitare l'accesso alle viste (SOLA LETTURA)