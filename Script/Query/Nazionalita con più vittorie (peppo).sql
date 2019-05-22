--La nazione che ha avuto più piloti e scuderie campioni 
create temporary view NAZIONALITA_VINCITRICI (nazionalita,vittorie) as
select nazionalita,count(*)
from	(select nazionalita
		 from	(select c.numero_campionato, p.nazionalita
				from CLASSIFICHE_PILOTI_PASSATI C join piloti P on (c.codice_pilota = p.codice_pilota)
				where punteggio = any (select max(punteggio)
									   from CLASSIFICHE_PILOTI_PASSATI)

				union all

				select c.numero_campionato, s.nazionalita_scuderia
				from CLASSIFICHE_COSTRUTTORI_PASSATE C join scuderie as S on (c.nome_scuderia = s.nome_scuderia)
				where punteggio = any (select max(punteggio)
									   from CLASSIFICHE_COSTRUTTORI_PASSATE)) as t1
		group by nazionalita,numero_campionato					   
		having count(*)=2) as t2   
group by nazionalita;			
	 
select nazionalita, max(vittorie)				 
from NAZIONALITA_VINCITRICI
where vittorie = (select max(vittorie)
				  from NAZIONALITA_VINCITRICI)
group by nazionalita
