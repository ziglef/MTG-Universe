# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Cards (
  id                        integer auto_increment not null,
  layout                    varchar(255),
  name                      varchar(255),
  manaCost                  varchar(255),
  cmc                       integer,
  type                      varchar(255),
  rarity                    varchar(255),
  text                      TEXT(1023),
  flavor                    TEXT(1023),
  artist                    varchar(255),
  number                    varchar(255),
  power                     varchar(255),
  toughness                 varchar(255),
  loyalty                   integer,
  multiverseid              integer,
  imageName                 varchar(255),
  watermark                 varchar(255),
  border                    varchar(255),
  timeshifted               varchar(255),
  hand                      integer,
  life                      integer,
  reserved                  varchar(255),
  releaseDate               varchar(255),
  originalText              TEXT(1023),
  originalType              varchar(255),
  source                    varchar(255),
  constraint pk_Cards primary key (id))
;

create table cardcolors (
  id                        integer not null,
  CCOLORS_ID                integer,
  color                     varchar(255),
  constraint pk_cardcolors primary key (id))
;

create table cardnames (
  id                        integer not null,
  CNAMES_ID                 integer,
  name                      varchar(255),
  constraint pk_cardnames primary key (id))
;

create table Sets (
  id                        integer auto_increment not null,
  name                      varchar(255),
  code                      varchar(255),
  releaseDate               varchar(255),
  border                    varchar(255),
  type                      varchar(255),
  block                     varchar(255),
  gathererCode              varchar(255),
  constraint pk_Sets primary key (id))
;

create table Users (
  id                        integer auto_increment not null,
  name                      varchar(255) not null,
  username                  varchar(255) not null,
  email                     varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_Users primary key (id))
;

create sequence cardcolors_seq;

create sequence cardnames_seq;

alter table cardcolors add constraint fk_cardcolors_cColors_1 foreign key (CCOLORS_ID) references Cards (id) on delete restrict on update restrict;
create index ix_cardcolors_cColors_1 on cardcolors (CCOLORS_ID);
alter table cardnames add constraint fk_cardnames_cNames_2 foreign key (CNAMES_ID) references Cards (id) on delete restrict on update restrict;
create index ix_cardnames_cNames_2 on cardnames (CNAMES_ID);



# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists Cards;

drop table if exists cardcolors;

drop table if exists cardnames;

drop table if exists Sets;

drop table if exists Users;

SET REFERENTIAL_INTEGRITY TRUE;

drop sequence if exists cardcolors_seq;

drop sequence if exists cardnames_seq;

