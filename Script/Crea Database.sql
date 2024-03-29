begin transaction;
drop table if exists SCUDERIE cascade;
drop table if exists PILOTI cascade;
drop table if exists PISTE cascade;
drop table if exists PERSONALE cascade;
drop table if exists CAMPIONATI cascade;
drop table if exists AFFERENZA_PERSONALE cascade;
drop table if exists AFFERENZA_PILOTI cascade;
drop table if exists CALENDARIO cascade;
drop table if exists DIRIGENZA cascade;
drop table if exists RISULTATI_ATTUALI cascade;
drop table if exists RISULTATI_PASSATI cascade;
drop domain if exists TempoGiro cascade;
commit work;

create domain TempoGiro as int check (value > 0);

/*==============================================================*/
/* Table: AFFERENZA_PERSONALE                                   */
/*==============================================================*/
create table AFFERENZA_PERSONALE (
   NOME_SCUDERIA        VARCHAR(50)          not null,
   CODICE_PERSONALE     CHAR(8)              not null,
   NUMERO_CAMPIONATO    INT                  not null,
   constraint PK_AFFERENZA_PERSONALE primary key (NOME_SCUDERIA, CODICE_PERSONALE, NUMERO_CAMPIONATO)
);

/*==============================================================*/
/* Table: AFFERENZA_PILOTI                                      */
/*==============================================================*/
create table AFFERENZA_PILOTI (
   CODICE_PILOTA        CHAR(8)              not null,
   NUMERO_CAMPIONATO    INT                  not null,
   NOME_SCUDERIA        VARCHAR(50)          not null,
   constraint PK_AFFERENZA_PILOTI primary key (CODICE_PILOTA, NUMERO_CAMPIONATO, NOME_SCUDERIA)
);

/*==============================================================*/
/* Table: CALENDARIO                                            */
/*==============================================================*/
create table CALENDARIO (
   SEDE_PISTA           VARCHAR(50)          not null,
   NOME_PISTA           VARCHAR(50)          not null,
   NUMERO_CAMPIONATO    INT                  not null,
   DATA                 DATE                 not null,
   NUMERO_GIORNATA      INT                  null,
   constraint PK_CALENDARIO primary key (SEDE_PISTA, NOME_PISTA, NUMERO_CAMPIONATO),
   check(NUMERO_GIORNATA between 1 and 21)
);

/*==============================================================*/
/* Table: CAMPIONATI                                            */
/*==============================================================*/
create table CAMPIONATI (
   NUMERO_CAMPIONATO    INT                  not null,
   DATA_INIZIO          DATE                 not null,
   DATA_FINE            DATE                 not null,
   MOTORE               VARCHAR(50)          not null,
   GOMME                VARCHAR(50)          not null,
   constraint PK_CAMPIONATI primary key (NUMERO_CAMPIONATO)
);

/*==============================================================*/
/* Table: DIRIGENZA                                             */
/*==============================================================*/
create table DIRIGENZA (
   NUMERO_CAMPIONATO    INT                  not null,
   CODICE_PERSONALE     CHAR(8)              not null,
   NOME_SCUDERIA        VARCHAR(50)          not null,
   constraint PK_DIRIGENZA primary key (NUMERO_CAMPIONATO, CODICE_PERSONALE, NOME_SCUDERIA)
);

/*==============================================================*/
/* Table: PERSONALE                                             */
/*==============================================================*/
create table PERSONALE (
   CODICE_PERSONALE     CHAR(8)              not null,
   NOME_PERSONALE       VARCHAR(50)          not null,
   COGNOME_PERSONALE    VARCHAR(50)          not null,
   NAZIONALITA_PERSONALE VARCHAR(50)          not null,
   DATA_NASCITA         DATE                 not null,
   PROFESSIONE          VARCHAR(50)          not null,
   constraint PK_PERSONALE primary key (CODICE_PERSONALE)
);

/*==============================================================*/
/* Table: PILOTI                                                */
/*==============================================================*/
create table PILOTI (
   CODICE_PILOTA        CHAR(8)              not null,
   NOME_PILOTA          VARCHAR(50)          not null,
   COGNOME_PILOTA       VARCHAR(50)          not null,
   NAZIONALITA          VARCHAR(50)          not null,
   DATA_NASCITA         DATE                 not null,
   TITOLI_VINTI         INT                  not null,
   ATTIVO               BOOL                 not null,
   DATA_RITIRO          INT                  null,
   constraint PK_PILOTI primary key (CODICE_PILOTA),
   check((attivo and data_ritiro is null) or (data_ritiro <> null and not attivo))
);

/*==============================================================*/
/* Table: PISTE                                                 */
/*==============================================================*/
create table PISTE (
   SEDE_PISTA           VARCHAR(50)          not null,
   NOME_PISTA           VARCHAR(50)          not null,
   LUNGHEZZA            INT                  not null,
   NUM_CURVE           INT                  not null,
   GIRO_VELOCE          TempoGiro            null,
   ANNO_INAUGURAZIONE   INT                  not null,
   constraint PK_PISTE primary key (SEDE_PISTA, NOME_PISTA)
);

/*==============================================================*/
/* Table: RISULTATI_ATTUALI                                     */
/*==============================================================*/
create table RISULTATI_ATTUALI (
   SEDE_PISTA           VARCHAR(50)          not null,
   NOME_PISTA           VARCHAR(50)          not null,
   CODICE_PILOTA        CHAR(8)              not null,
   NUMERO_CAMPIONATO    INT                  not null,
   PUNTEGGIO            INT                  not null,
   MIGLIOR_TEMPO        TempoGiro            null,
   TEMPO_QUALIFICA      TempoGiro            null,
   RITIRO               BOOL                 not null,
   constraint PK_RISULTATI_ATTUALI primary key (SEDE_PISTA, NOME_PISTA, CODICE_PILOTA, NUMERO_CAMPIONATO),
	check(punteggio between 0 and 26),
	check(punteggio = 0 or not ritiro)
);

/*==============================================================*/
/* Table: RISULTATI_PASSATI                                     */
/*==============================================================*/
create table RISULTATI_PASSATI (
   SEDE_PISTA           VARCHAR(50)          not null,
   NOME_PISTA           VARCHAR(50)          not null,
   CODICE_PILOTA        CHAR(8)              not null,
   NUMERO_CAMPIONATO    INT                  not null,
   PUNTEGGIO            INT                  not null,
   MIGLIOR_TEMPO        TempoGiro            null,
   TEMPO_QUALIFICA      TempoGiro            null,
   RITIRO               BOOL                 not null,
   constraint PK_RISULTATI_PASSATI primary key (SEDE_PISTA, NOME_PISTA, CODICE_PILOTA, NUMERO_CAMPIONATO),
	check(punteggio between 0 and 26),
	check(punteggio = 0 or not ritiro)
	);

/*==============================================================*/
/* Table: SCUDERIE                                              */
/*==============================================================*/
create table SCUDERIE (
   NOME_SCUDERIA        VARCHAR(50)          not null,
   NAZIONALITA_SCUDERIA VARCHAR(50)          not null,
   NUM_CAMPIONATI_VINTI INT                  not null,
   constraint PK_SCUDERIE primary key (NOME_SCUDERIA)
);

alter table AFFERENZA_PERSONALE
   add constraint FK_AFFERENZ_AFFERENZA_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict deferrable initially deferred;

alter table AFFERENZA_PERSONALE
   add constraint FK_AFFERENZ_REFERENCE_SCUDERIE foreign key (NOME_SCUDERIA)
      references SCUDERIE (NOME_SCUDERIA)
      on delete restrict on update restrict deferrable initially deferred;

alter table AFFERENZA_PERSONALE
   add constraint FK_AFFERENZ_REFERENCE_PERSONAL foreign key (CODICE_PERSONALE)
      references PERSONALE (CODICE_PERSONALE)
      on delete restrict on update restrict deferrable initially deferred;

alter table AFFERENZA_PILOTI
   add constraint FK_AFFERENZ_REFERENCE_PILOTI foreign key (CODICE_PILOTA)
      references PILOTI (CODICE_PILOTA)
      on delete restrict on update restrict;

alter table AFFERENZA_PILOTI
   add constraint FK_AFFERENZ_REFERENCE_SCUDERIE foreign key (NOME_SCUDERIA)
      references SCUDERIE (NOME_SCUDERIA)
      on delete restrict on update restrict;

alter table AFFERENZA_PILOTI
   add constraint FK_AFFERENZ_REFERENCE_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict;

alter table CALENDARIO
   add constraint FK_CALENDAR_REFERENCE_PISTE foreign key (SEDE_PISTA, NOME_PISTA)
      references PISTE (SEDE_PISTA, NOME_PISTA)
      on delete restrict on update restrict;

alter table CALENDARIO
   add constraint FK_CALENDAR_REFERENCE_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict;

alter table DIRIGENZA
   add constraint FK_DIRIGENZ_REFERENCE_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict deferrable initially deferred;

alter table DIRIGENZA
   add constraint FK_DIRIGENZ_REFERENCE_PERSONAL foreign key (CODICE_PERSONALE)
      references PERSONALE (CODICE_PERSONALE)
      on delete restrict on update restrict deferrable initially deferred;

alter table DIRIGENZA
   add constraint FK_DIRIGENZ_REFERENCE_SCUDERIE foreign key (NOME_SCUDERIA)
      references SCUDERIE (NOME_SCUDERIA)
      on delete restrict on update restrict deferrable initially deferred;

alter table RISULTATI_ATTUALI
   add constraint FK_RISULTAT_REFERENCE_PISTE foreign key (SEDE_PISTA, NOME_PISTA)
      references PISTE (SEDE_PISTA, NOME_PISTA)
      on delete restrict on update restrict;

alter table RISULTATI_ATTUALI
   add constraint FK_RISULTAT_REFERENCE_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict;

alter table RISULTATI_ATTUALI
   add constraint FK_RISULTAT_REFERENCE_PILOTI foreign key (CODICE_PILOTA)
      references PILOTI (CODICE_PILOTA)
      on delete restrict on update restrict;

alter table RISULTATI_PASSATI
   add constraint FK_RISULTAT_REFERENCE_PISTE foreign key (SEDE_PISTA, NOME_PISTA)
      references PISTE (SEDE_PISTA, NOME_PISTA)
      on delete restrict on update restrict;

alter table RISULTATI_PASSATI
   add constraint FK_RISULTAT_REFERENCE_PILOTI foreign key (CODICE_PILOTA)
      references PILOTI (CODICE_PILOTA)
      on delete restrict on update restrict;

alter table RISULTATI_PASSATI
   add constraint FK_RISULTAT_REFERENCE_CAMPIONA foreign key (NUMERO_CAMPIONATO)
      references CAMPIONATI (NUMERO_CAMPIONATO)
      on delete restrict on update restrict;
