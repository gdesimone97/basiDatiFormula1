-- query per aggiornare punteggi passati
create or replace function AGGIORNAMENTO_TITOLI() returns trigger as $$
begin	
	if(((select max(numero_campionto) from campionati) = new.numero_campionato-1)  
	and ( 420 <= (select count(*) from risultati_attuali)))
	then
	
	update Piloti
	set titoli_vinti =  titoli_vinti +1
			   			
	where codice_pilota =  (select codice_pilota
							from risultati_attuali
							where punteggio = (select max(punteggio)
											   from risultati_attuali));
											   
	update scuderie
    set num_campionati_vinti = num_campionati_vinti +1
							    											   
	where nome_scuderia =   (select nome_scudeia
							 from CLASSIFICA_COSTRUTTORI_ATTUALE
							 where punti = (select max(punti)
							  			    from CLASSIFICA_COSTRUTTORI_ATTUALE ));
	
											
											
	insert into risultati_passati select * from risultati_attuali;
	delete from risultati_attuali;
end if;
return NEW;
end $$ language plpgsql;
											   
create trigger aggiornamento_titoli
after insert on campionati
for each statement
execute procedure aggiornamento_titoli()
											   