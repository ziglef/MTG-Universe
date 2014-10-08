# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Cards (
  id                        integer auto_increment not null,
  layout                    integer,
  name                      varchar(255),
  manaCost                  varchar(255),
  cmc                       float,
  type                      varchar(255),
  supertypes                varchar(255),
  types                     varchar(255),
  subtypes                  varchar(255),
  rarity                    varchar(255),
  text                      varchar(255),
  flavor                    varchar(255),
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
  foreignNames              varchar(255),
  originalText              varchar(255),
  originalType              varchar(255),
  source                    varchar(255),
  constraint ck_Cards_layout check (layout in (0,1,2,3,4,5,6,7,8,9)),
  constraint pk_Cards primary key (id))
;

create table Users (
  id                        integer auto_increment not null,
  name                      varchar(255) not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_Users primary key (id))
;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists Cards;

drop table if exists Users;

SET REFERENTIAL_INTEGRITY TRUE;

