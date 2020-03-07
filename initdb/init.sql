create table users
(
  id serial primary key,
  name varchar(50) unique not null,
  password varchar(50) not null,
  email varchar(255) unique not null,
  employment_status_id varchar(2) not null,
  department_id varchar(2) not null,
  word_base_id varchar(2) not null,
  create_date timestamp,
  update_date timestamp
);

insert into users (name, password, email, employment_status_id, department_id, word_base_id) values ('user1', 'password', 'user1@developer.com', '1', '1', '1');
insert into users (name, password, email, employment_status_id, department_id, word_base_id) values ('user2', 'password', 'user2@developer.com', '1', '1', '1');
insert into users (name, password, email, employment_status_id, department_id, word_base_id) values ('user3', 'password', 'user3@developer.com', '1', '1', '1');


create table attendance
(
  user_id bigint not null,
  attendance_date varchar(8) not null,
  kind varchar(8),
  day_of_week varchar(10),
  start_time time,
  end_time time,
  break_start_time time,
  break_end_time time,
  all_time time,
  break_all_time time,
  overtime time,
  remarks varchar(255),
  aproval_kind varchar(8),
  create_date timestamp,
  update_date timestamp
);