INSERT INTO role (name) VALUES ('PM');
INSERT INTO role (name) VALUES ('DOMAIN EXPERT');
INSERT INTO role (name) VALUES ('TRUSTEE');
INSERT INTO role (name) VALUES ('CEO');
INSERT INTO role (name) VALUES ('AO');

INSERT INTO admin (email, username, is_already_validated)
VALUES ('rajdeep108pal@gmail.com', 'Admin', false);



-- Beneficiaries Init-----

select * from m_beneficiary;
select * from m_component;
select * from m_activity;
select * from m_task;

select * from project;

INSERT into project(id, project_name, terminate, user_id)
VALUES
(1, 'BT', 'N', 1),
(2, 'Vodafone', 'N', 1),
(3, 'Airtel', 'N', 1)

select * from users;

INSERT into users(id, domain, emailid, emp_id, is_active, password, username) VALUES
(1, 'domain1', 'debjit16.dc@gmail.com', 1898635, 'Y',
'$2a$12$Jt47x27jaRLMOcokEygMeuQToIvROdaS4QpmRoIiwnAM5XxZjLRPe','debjit31')


