-- Script per la generazione e l'aggiornamento delle viste principali


create view CLASSIFICA_PILOTI(numero_campionato, codice_pilota, punteggio) as 
	select numero_campionato, codice_pilota, sum(punteggio) as punti
	from risultati
	group by numero_campionato, codice_pilota
	order by punti desc;



create view CLASSIFICA_COSTRUTTORI (numero_campionato, nome_scuderia, punteggio) as
	select A.numero_campionato, nome_scuderia, sum(C.punteggio) as punti
	from CLASSIFICA_PILOTI as C join AFFERENZA_PILOTI as A on (C.codice_pilota = A.codice_pilota)
	group by  C.numero_campionato, nome_scuderia
	order by punti desc;
	
/*	
	le viste si aggiornano in automatico, ad ogni modifica su risultati (che può avvenire solo a gruppi di 20 per via del trigger)
	e contengono le classifiche di ogni campionato
	-> problema => calcola ogni volta la classifica di tutto , quindi su tanti campionati è lenta (anche se viene fatta 1 V/S)
*/


-- comandi DDL per definire l'accesso in sola lettura sulle viste