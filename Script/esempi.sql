start transaction;
insert into scuderie values ('Ferrari', 'EN', 4);
insert into personale values ('AC001', 'Afonso', 'Cazzone', 'IT', current_date, 'meccanico');
insert into campionati values (1, current_date, current_date, 'motori', 'gomme');
insert into afferenza_personale values ('Ferrari', 'AC001', 1);
insert into dirigenza values (1, 'AC001', 'Ferrari');
commit work;

select * from campionati;
select * from risultati;
delete from afferenza_personale cascade;
delete from dirigenza cascade;
delete from risultati cascade;
delete from personale cascade;
delete from campionati cascade;
delete from scuderie cascade;
delete from piloti cascade;
delete from piste cascade;

start transaction;
insert into piloti values ('PI001', 'xx', 'dsa', 'it', current_date, 5, true);
insert into piloti values ('PI002', 'xddx', 'dadwsa', 'it', current_date, 8, true);
insert into campionati values (1, current_date, current_date, 'motori', 'gomme');
insert into piste values ('roma', 'romann', 30, 2, 500, 2018);
insert into risultati values('roma', 'romann', 'PI001', 1, 5, 600, 500, false);
insert into risultati values('roma', 'romann', 'PI002', 1, 5, 600, 500, false);
commit work;