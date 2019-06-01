--Eventuali gare in cui si è avuto Primo e Secondo posto della stessa scuderia (campionato attuale)

select r.numero_campionato, r.sede_pista, r.nome_pista, p.nome_scuderia
from risultati_attuali r join afferenza_piloti p 
	on r.codice_pilota = p.codice_pilota AND r.numero_campionato = p.numero_campionato
where (r.punteggio = 26 or r.punteggio = 25 or r.punteggio = 18 or r.punteggio = 19)
group by r.numero_campionato, r.sede_pista, r.nome_pista, p.nome_scuderia
having count(*)=2