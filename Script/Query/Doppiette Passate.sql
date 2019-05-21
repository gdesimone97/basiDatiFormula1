select nome_scuderia,count(*)
from	(select codice_pilota,sede_pista,nome_pista
		from risultati_passati
		where punteggio = 26 or punteggio = 25
		union 
		select codice_pilota,sede_pista,nome_pista
		from risultati_passati
		where punteggio = 19 or punteggio = 18) as CP, afferenza_piloti as AP
where cp.codice_pilota=ap.codice_pilota
group by nome_scuderia
