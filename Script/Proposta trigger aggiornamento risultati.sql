-- query per aggiornare punteggi passati
select codice_pilota as "Codice Pilota", sum(punteggio) as "Punteggi Totali"
from    (select codice_pilota, R.punteggio
		from risultati_attuali R
		union
		select codice_pilota, RP.punteggio
		from risultati_passati RP
		) as piloti_punteggi
group by codice_pilota