-- Script per la generazione e l'aggiornamento delle viste principali


create view CLASSIFICHE_PILOTI(numero_campionato, codice_pilota, punteggio) as 
	select numero_campionato, codice_pilota, sum(punteggio) as punti
	from risultati R
	group by numero_campionato, codice_pilota
	order by punti desc;



create view CLASSIFICHE_COSTRUTTORI (numero_campionato, nome_scuderia, punteggio) as
	select A.numero_campionato, nome_scuderia, sum(C.punti) as punti_scuderia
	from CLASSIFICHE_PILOTI as C join AFFERENZA_PILOTI as A
	where C.codice_pilota = A.codice_pilota and A.numero_campionato = C.numero_campionato
	group by  C.numero_campionato, nome_scuderia
	order by punti_scuderia desc;
	
/*	
	le viste si aggiornano in automatico, ad ogni modifica su risultati (che può avvenire solo a gruppi di 20 per via del trigger)
	e contengono le classifiche di ogni campionato
	-> problema => calcola ogni volta la classifica di tutto , quindi su tanti campionati è lenta (anche se viene fatta 1 V/S)
*/


-- comandi DDL per definire l'accesso in sola lettura sulle viste

--> queste viste servirebbero solo per le classifiche attuali (cerca automaticamente l'ultimo campionato in corso)
create view CLASSIFICA_PILOTI_ATTUALE(numero_campionato, codice_pilota, punteggio) as 
	select numero_campionato, codice_pilota, sum(punteggio) as punti
	from risultati R
	where R.numero_campionato = (select max(numero_campionato)
								from campionati)
	group by numero_campionato, codice_pilota
	order by punti desc;

create view CLASSIFICA_COSTRUTTORI (numero_campionato, nome_scuderia, punteggio) as
	select A.numero_campionato, nome_scuderia, sum(C.punti) as punti_scuderia
	from CLASSIFICA_PILOTI_ATTUALE as C join AFFERENZA_PILOTI as A
	where C.codice_pilota = A.codice_pilota and A.numero_campionato = C.numero_campionato
	group by  C.numero_campionato, nome_scuderia
	order by punti_scuderia desc;