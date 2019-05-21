--Il numero di volte in cui i piloti della scuderia hanno fatto prima e seconda posizione nella stessa gara 
select nome_scuderia,count(*)
 from	    (select codice_pilota,sede_pista,nome_pista,numero_campionato
			from risultati_passati
			where punteggio = 26 or punteggio = 25
			union 
			select codice_pilota,sede_pista,nome_pista,numero_campionato
			from risultati_passati
			where punteggio = 19 or punteggio = 18) as CP, afferenza_piloti as AP
where cp.codice_pilota=ap.codice_pilota and cp.numero_campionato=ap.numero_campionato
group by nome_scuderia

