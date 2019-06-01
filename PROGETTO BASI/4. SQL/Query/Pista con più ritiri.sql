--La pista che ha registrato più numero di ritiri

select p.sede_pista, p.nome_pista, count(*) as num_ritiri
	from (
		--Numero di ritiri per ogni pista
		select sede_pista, nome_pista
		from risultati_attuali 
		where ritiro = true
		union all
		select sede_pista, nome_pista
		from risultati_passati
		where ritiro = true ) p
	group by sede_pista, nome_pista
	order by num_ritiri desc
limit 1;

