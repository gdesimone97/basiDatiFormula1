--	15. Per ogni pilota, numero di vittorie consecutive dei campionati, {e di gare e pole position} (50 v/s)

/*	
	##################
		DA TESTARE 
	##################
*/

-- 	SOLO CAMPIONATO ->> {PER GARE E POLE POSITION DIJ U SAP E A MARONN O VER}

--	creo una vista che associa un campionato al codice del pilota che lo ha vinto
drop view if exists STORICO_VINCITORI cascade;
create view STORICO_VINCITORI(numero_campionato, codice_pilota) as
	select distinct CP.numero_campionato, (select codice_pilota 
								from CLASSIFICHE_PILOTI_PASSATI
								where punteggio = ( select max(punteggio)
													from CLASSIFICHE_PILOTI_PASSATI as CP2
													where CP2.numero_campionato = CP.numero_campionato)) as codice_pilota
	from CLASSIFICHE_PILOTI_PASSATI as CP
	group by CP.numero_campionato;

/*	
	#############
		QUERY
	#############
*/

--	seleziono i piloti che hanno vinto dei campionati consecutivi
drop view if exists PILOTI_VINCITORI_CONSECUTIVI;
create view PILOTI_VINCITORI_CONSECUTIVI(codice_pilota, numero_campionati_consecutivi) as
	select distinct SV.codice_pilota, count(*) + 1 as numero_campionati_consecutivi
	from STORICO_VINCITORI as SV join STORICO_VINCITORI as SV2 on (SV.codice_pilota = SV2.codice_pilota)
	where SV.numero_campionato = SV2.numero_campionato -1
	group by SV.codice_pilota;
	
	select * from PILOTI_VINCITORI_CONSECUTIVI
--	li unisco ai piloti che non hanno vinto campionati consecutivi (ma hanno vinto almeno un campionato)
		union
	select distinct SV.codice_pilota, 1 as numero_campionati_consecutivi
	from STORICO_VINCITORI as SV
	where SV.codice_pilota not in (select PVC.codice_pilota from PILOTI_VINCITORI_CONSECUTIVI as PVC)
	group by SV.codice_pilota
--	e si aggiungono tutti i piloti che non hanno mai vinto un campionato
		union
	select P.codice_pilota, 0 as numero_campionati_consecutivi
	from piloti as P
	where P.codice_pilota not in (select SV.codice_pilota
								from STORICO_VINCITORI as SV)
	order by numero_campionati_consecutivi desc;							