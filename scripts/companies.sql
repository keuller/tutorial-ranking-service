create table companies (
  id varchar(36) not null,
  name varchar(100) not null,
  site varchar(150),
  foundation date not null,
  score char(1) not null,
  likes int default 0,
  dislikes int default 0,
  created_at timestamp not null default current_timestamp,
  primary key (id)
);
