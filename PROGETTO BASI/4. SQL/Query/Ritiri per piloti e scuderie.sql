drop view if exists ritiro_pilota;
--Vista che consente di vedere tutti i ritiri dei campionati passati
create view Ritiro_Piloti as
  select Codice_Pilota,Numero_Campionato
  from Risultati_Passati
  where ritiro = true;

--Quante volte ogni singola scuderia si è ritirata nel passato
select Nome_Scuderia, count(*) as Ritiro_Scuderie
from (select RP.Codice_Pilota,RP.Numero_Campionato, Nome_Scuderia
  from Ritiro_Piloti RP join Afferenza_Piloti AP
  on (RP.Codice_Pilota = AP.Codice_Pilota and
  RP.Numero_Campionato = AP.Numero_Campionato)) as Tmp
group by Nome_Scuderia;

--Quante volte si è ritirato ogni singolo pilota nel passato
select Nome_Pilota,Cognome_pilota,count(*) as Ritiro_Pilota
from Piloti P join Ritiro_Piloti RP on (P.Codice_Pilota = RP.Codice_Pilota)
group by Nome_Pilota, Cognome_pilota
order by ritiro_pilota desc;




drop view if exists ritiro_piloti_attuali;
--Vista che consente di vedere tutti i ritiri del campionato attuale
create view Ritiro_Piloti_Attuali as
  select Codice_Pilota,Numero_Campionato
  from Risultati_Attuali
  where Ritiro = true;

  --Quante volte ogni singola scuderia si è ritirata nel campionato attuale
  select Nome_Scuderia, count(*) as Ritiri_Scuderie
  from (select RPA.Codice_Pilota,RPA.Numero_Campionato, Nome_Scuderia
    from Ritiro_Piloti_Attuali RPA join Afferenza_Piloti AP
    on (RPA.Codice_Pilota = AP.Codice_Pilota and
    RPA.Numero_Campionato = AP.Numero_Campionato)) as Tmp
  group by Nome_Scuderia;

  --Quante volte si è ritirato ogni singolo pilota nel campionato attuale
  select Nome_Pilota, Cognome_Pilota, count(*) as Ritiro_Pilota
  from Piloti P join Ritiro_Piloti_Attuali RPA on (P.Codice_Pilota = RPA.Codice_Pilota)
  group by Nome_Pilota, cognome_pilota
  order by ritiro_pilota desc;
