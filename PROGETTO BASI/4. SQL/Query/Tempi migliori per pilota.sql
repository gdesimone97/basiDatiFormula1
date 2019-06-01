--	Per ogni  pilota, il suo tempo migliore per ogni pista, indicando se è o meno il record della relativa pista (100 v/s)

/*	
	Creazione di una vista temporanea per avere tutti i risultati, attuali e passati. 
	Di questi si porta solo la chiave composta e il miglior_tempo
*/
	drop view if exists tmp_RISULTATI cascade;

	 create view tmp_RISULTATI(nome_pista, sede_pista, codice_pilota, miglior_tempo, numero_campionato) as
		select RA.nome_pista, RA.sede_pista, RA.codice_pilota, RA.miglior_tempo, (select max(numero_campionato) from campionati) as numero_campionato
		from risultati_attuali as RA	
			union
		select nome_pista, sede_pista, codice_pilota, miglior_tempo, numero_campionato
		from risultati_passati;



/*
	#############
		QUERY
	#############
*/
--	Della vista precedente, si raggruppa per pilota e per pista (nome e sede) selezionando solo il record su ogni pista
--	si usa il costrutto CASE-WHEN-THEN per settare il campo RECORD
	select codice_pilota, tR.nome_pista, tR.sede_pista, min(miglior_tempo) as RECORD_PERSONALE, 
			CASE WHEN min(miglior_tempo) = P.giro_veloce THEN 'RECORD' ELSE 'NO RECORD' END as RECORD
	from tmp_RISULTATI as tR, piste as P
	where tR.nome_pista = P.nome_pista and tR.sede_pista = P.sede_pista
	group by codice_pilota, tR.nome_pista, tR.sede_pista, P.giro_veloce