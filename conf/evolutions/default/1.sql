# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table Users (
  id                        integer auto_increment not null,
  name                      varchar(255) not null,
  username                  varchar(255) not null,
  password                  varchar(255) not null,
  constraint pk_Users primary key (id))
;




# --- !Downs

SET REFERENTIAL_INTEGRITY FALSE;

drop table if exists Users;

SET REFERENTIAL_INTEGRITY TRUE;

