
	-- creazione di viste necessarie alla corretta determinazione delle posizioni in classifica
	drop view if exists tmp_RISULTATI cascade;
	create view tmp_RISULTATI(nome_pista, sede_pista, codice_pilota, miglior_tempo, numero_campionato, punteggio) as
		select RA.nome_pista, RA.sede_pista, RA.codice_pilota, RA.miglior_tempo, (select max(numero_campionato) from campionati) as numero_campionato, RA.punteggio
		from risultati_attuali as RA	
			union
		select nome_pista, sede_pista, codice_pilota, miglior_tempo, numero_campionato, punteggio
		from risultati_passati;

	drop view if exists numero_vittorie_tmp cascade;
	create view numero_vittorie_tmp (codice_pilota, numero_vittorie, numero_campionato) as
		select codice_pilota, count(*) as numero_vittorie, numero_campionato
		from tmp_RISULTATI
		where punteggio = 25 or punteggio = 26
		group by numero_campionato, codice_pilota;
	
	drop view if exists numero_vittorie cascade;
	create view numero_vittorie(codice_pilota, numero_vittorie, numero_campionato) as
		select AP.codice_pilota, 0, AP.numero_campionato
		from afferenza_piloti as AP
		where AP.codice_pilota not in (select codice_pilota from numero_vittorie_tmp as NVT where NVT.numero_campionato = AP.numero_campionato )
		union
		select * from numero_vittorie_tmp;

-- le due viste "attuali" vengono aggiornate ad ogni inserimento su risultati_attuali
	
create view CLASSIFICA_PILOTI_ATTUALE(codice_pilota, nome_pilota, cognome_pilota, punteggio) as 
	select P.codice_pilota, P.nome_pilota, P.cognome_pilota, sum(punteggio) as punti
	from risultati_attuali as A, piloti as P, numero_vittorie as NV
	where NV.numero_campionato = ( select max(numero_campionato)
								from campionati ) and
			A.codice_pilota = P.codice_pilota and
			A.codice_pilota = NV.codice_pilota
	group by P.codice_pilota, NV.numero_vittorie
	order by punti desc, NV.numero_vittorie desc;

create view CLASSIFICA_COSTRUTTORI_ATTUALE (nome_scuderia, punteggio) as
	select nome_scuderia, sum(C.punteggio) as punti
	from CLASSIFICA_PILOTI_ATTUALE as C, AFFERENZA_PILOTI as A, numero_vittorie as NV
	where A.numero_campionato = ( select max(numero_campionato)
								from campionati ) 
			and C.codice_pilota = A.codice_pilota and 
			NV.codice_pilota  = C.codice_pilota and
			NV.numero_campionato = A.numero_campionato
	group by nome_scuderia
	order by punti desc, sum(NV.numero_vittorie) desc;
	
-- le due viste "passate" vengono ricalcolate ogni volta che il campionato termina
-- (ovvero dopo che il trigger sposta i 420 risultati_attuali in risultati_passati) (operazione costosa ma fatta 1 V/A)

create view CLASSIFICHE_PILOTI_PASSATI(numero_campionato, codice_pilota,nome_pilota,cognome_pilota, punteggio) as 
	select A.numero_campionato, p.codice_pilota, P.nome_pilota, P.cognome_pilota, sum(punteggio) as punti
	from risultati_passati as A, piloti as P, numero_vittorie as NV
	where	A.codice_pilota = P.codice_pilota and
			A.codice_pilota = NV.codice_pilota and
			A.numero_campionato = NV.numero_campionato
	group by A.numero_campionato, p.codice_pilota, NV.numero_vittorie
	order by punti desc, NV.numero_vittorie desc;
	
create view CLASSIFICHE_COSTRUTTORI_PASSATE (numero_campionato, nome_scuderia, punteggio) as
	select C.numero_campionato, A.nome_scuderia, sum(cast(C.punteggio as INT)) as punti
	from CLASSIFICHE_PILOTI_PASSATI as C, AFFERENZA_PILOTI as A, numero_vittorie as NV
	where	C.codice_pilota = A.codice_pilota and
			C.numero_campionato = A.numero_campionato and
			C.codice_pilota = NV.codice_pilota and
			C.numero_campionato = NV.numero_campionato
	group by C.numero_campionato, A.nome_scuderia
	order by punti desc, sum(NV.numero_vittorie) desc;


