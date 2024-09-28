--- after performing Database refresh or after initial project setup, execute the below
--- queries in this order

INSERT INTO role (name) VALUES ('PM');
INSERT INTO role (name) VALUES ('DOMAIN EXPERT');
INSERT INTO role (name) VALUES ('TRUSTEE');
INSERT INTO role (name) VALUES ('CEO');
INSERT INTO role (name) VALUES ('AO');
INSERT INTO role (name) VALUES ('EADMIN');

--- create a user from swagger

select * from project;

INSERT into project(id, project_name, terminate, user_id)
VALUES
(1, 'BT', 'N', 1),
(2, 'Vodafone', 'N', 1),
(3, 'Airtel', 'N', 1)

select * from users;



