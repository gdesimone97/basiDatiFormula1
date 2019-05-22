--Vista che consente di vedere tutti i ritiri dei campionati passati
create view Ritiro_Piloti as
  select Codice_Pilota,Numero_Campionato
  from Risultati_Passati
  where Ritirato = true;

--Quante volte ogni singola scuderia si è ritirata nel passato
select Nome_Scuderia, count(*) as Ritiro_Scuderie
from (select Codice_Pilota,Numero_Campionato, Nome_Scuderia
  from Ritiro_Piloti RP join Afferenza_Piloti AP
  on (RP.Codice_Pilota = AP.Codice_Pilota and
  RP.Numero_Campionato = AP.Numero_Campionato)) as Tmp
group by Nome_Scuderia;

--Quante volte si è ritirato ogni singolo pilota nel passato
select Nome_Pilota, count(*) as Ritiro_Pilota
from Piloti P join Ritiro_Piloti RP on (P.Codice_Pilota = RP.Codice_Pilota)
group by Nome_Pilota;





--Vista che consente di vedere tutti i ritiri del campionato attuale
create view Ritiro_Piloti as
  select Codice_Pilota,Numero_Campionato
  from Risultati_Attuali
  where Ritirato = true;

  --Quante volte ogni singola scuderia si è ritirata nel campionato attuale
  select Nome_Scuderia, count(*) as Ritiro_Scuderie
  from (select Codice_Pilota,Numero_Campionato, Nome_Scuderia
    from Ritiro_Piloti RP join Afferenza_Piloti AP
    on (RP.Codice_Pilota = AP.Codice_Pilota and
    RP.Numero_Campionato = AP.Numero_Campionato)) as Tmp
  group by Nome_Scuderia;

  --Quante volte si è ritirato ogni singolo pilota nel campionato attuale
  select Nome_Pilota, count(*) as Ritiro_Pilota
  from Piloti P join Ritiro_Piloti RP on (P.Codice_Pilota = RP.Codice_Pilota)
  group by Nome_Pilota;
