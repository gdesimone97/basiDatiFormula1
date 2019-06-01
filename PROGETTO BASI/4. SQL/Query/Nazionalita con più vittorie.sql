--Per ogni nazione il numero di piloti campioni e scuderie campioni

select t.nazionalita,vittorie_piloti as Vittorie_Piloti,coalesce(vittorie_scuderie,0) as Vittorie_Scuderie
from ((select nazionalita, sum(titoli_vinti) as Vittorie_Piloti
		from piloti 
		group by nazionalita) p left join (select nazionalita_scuderia, sum(Num_campionati_vinti) as Vittorie_Scuderie
									from scuderie 
									group by nazionalita_scuderia) s on (p.nazionalita=s.nazionalita_scuderia) ) as t
union
select t.nazionalita_scuderia,coalesce(vittorie_piloti,0) as Vittorie_Piloti,vittorie_scuderie
from ((select nazionalita, sum(titoli_vinti) as Vittorie_Piloti
		from piloti 
		group by nazionalita) p right join (select nazionalita_scuderia, sum(Num_campionati_vinti) as Vittorie_Scuderie
									from scuderie 
									group by nazionalita_scuderia) s on (p.nazionalita=s.nazionalita_scuderia) ) as t
order by nazionalita
