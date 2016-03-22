insert into user(id,  login, password) values (1,'ais','spring');
insert into user(id,  login, password) values (2,'admin','spring');
insert into user(id,  login, password) values (3,'guest','spring');

insert into role(id, name) values (1,'ROLE_USER');
insert into role(id, name) values (2,'ROLE_ADMIN');
insert into role(id, name) values (3,'ROLE_GUEST');

insert into user_role(user_id, role_id) values (1,1);
insert into user_role(user_id, role_id) values (2,1);
insert into user_role(user_id, role_id) values (2,2);
insert into user_role(user_id, role_id) values (3,1);
