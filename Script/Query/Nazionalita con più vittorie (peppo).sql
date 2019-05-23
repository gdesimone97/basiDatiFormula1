--La nazione che ha avuto più piloti e scuderie campioni 

create temporary view NAZIONALITA_VINCITRICI (nazionalita,vittorie) as
-- Raggruppando per nazionalita conto le occorrenze della stessa
		 select nazionalita, count(*)
		 from	(-- Leggo la scuderia e il pilota che hanno vinto i campionati passati
				select p.nazionalita
				from CLASSIFICHE_PILOTI_PASSATI C join piloti P on (c.codice_pilota = p.codice_pilota)
				where punteggio = any (select max(punteggio)
									   from CLASSIFICHE_PILOTI_PASSATI)
				group by numero_campionato
				union all

				select s.nazionalita_scuderia
				from CLASSIFICHE_COSTRUTTORI_PASSATE C join scuderie as S on (c.nome_scuderia = s.nome_scuderia)
				where punteggio = any (select max(punteggio)
									   from CLASSIFICHE_COSTRUTTORI_PASSATE)
				group by numero_campionato					               ) as t1
		group by nazionalita;			   	

--Predo la scuderia con il maggior numero di vittorie	 
select nazionalita, max(vittorie) as "Numero Vittorie"				 
from NAZIONALITA_VINCITRICI
where vittorie = (select max(vittorie)
				  from NAZIONALITA_VINCITRICI)
group by nazionalita


