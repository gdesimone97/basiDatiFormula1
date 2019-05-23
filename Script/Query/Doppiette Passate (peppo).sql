--Il numero di volte in cui i piloti della scuderia hanno fatto prima e seconda posizione nella stessa gara 
select nome_scuderia, count(*) as "Numero di volte"
from   (select r.numero_campionato, r.sede_pista, r.nome_pista, p.nome_scuderia
		from risultati_passati r join afferenza_piloti p 
		on r.codice_pilota = p.codice_pilota and r.numero_campionato = p.numero_campionato
		where (r.punteggio = 26 or r.punteggio = 25 or r.punteggio = 18 or r.punteggio = 19)
		group by r.numero_campionato, r.sede_pista, r.nome_pista, p.nome_scuderia
		having count(*)=2) as temp
group by nome_scuderia