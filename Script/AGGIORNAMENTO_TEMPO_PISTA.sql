create or replace function AGGIORNAMENTO_TEMPO_PISTA() returns trigger as $$
begin
	-- controllare che ci siano 20 risultati
	if( 20 = (	select count(*)
				from risultati) 
			and 
		exists (select min ( all miglior_tempo)
			  	from risultati r join piste p on (r.sede_pista=p.sede_pista and r.nome_pista=p.nome_pista)
				where miglior_tempo <= giro_veloce)
	   )						  
	then
		update piste
		set giro_veloce =  (select min( all miglior_tempo)
			  					  from risultati r join piste p on (r.sede_pista=p.sede_pista and r.nome_pista=p.nome_pista)
								  where miglior_tempo <= giro_veloce)
			  where piste.sede_pista = all (select sede_pista
									   	from risultati) and piste.nome_pista = all(select nome_pista
									   											    from risultati);
			-- Si potrebbe trovare un modo per non rifare la query? 
	end if;
			   
return NULL;
end $$ language plpgsql;

drop trigger if exists AGGIORNAMENTO_TEMPO_PISTA on risultati cascade;

create trigger AGGIORNAMENTO_TEMPO_PISTA
after insert on risultati
for each statement
execute procedure AGGIORNAMENTO_TEMPO_PISTA();