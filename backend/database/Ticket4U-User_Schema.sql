-- Drops

DROP TABLE IF EXISTS public.organizer;

DROP TABLE IF EXISTS public.session;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.role;

-- Table: role

CREATE TABLE IF NOT EXISTS public.role
(
	id int PRIMARY KEY,
    role_name varchar(32) NOT NULL
);

ALTER TABLE IF EXISTS public.role
    OWNER to postgres;

-- Data: roles
-- Basic roles, in a hierachy, can access base page, each having their own features + the lower role's
-- Can access the homepage, buy tickets, view events
insert into public.role(id, role_name) values (0, 'ROLE_CUSTOMER');
-- Manage an organizer's events, seating charts, ticket prices, etc..
insert into public.role(id, role_name) values (1, 'ROLE_EVENT_MANAGER');
-- Set up payout bank details, manage other event managers within their organization
insert into public.role(id, role_name) values (2, 'ROLE_ORGANIZER_ADMIN');
-- Monitor the system, create new organizer accounts
insert into public.role(id, role_name) values (3, 'ROLE_ADMIN');
-- Omnipotent
insert into public.role(id, role_name) values (4, 'ROLE_SYSTEM_ADMIN');

-- Special roles, have different pages and no hierachy
insert into public.role(id, role_name) values (10, 'ROLE_GATEKEEPER');
insert into public.role(id, role_name) values (11, 'ROLE_SUPPORT_AGENT');
insert into public.role(id, role_name) values (12, 'ROLE_FINANCE_MANAGER');

-- Table: users

CREATE TABLE IF NOT EXISTS public.users
(
    id uuid PRIMARY KEY,
    email varchar(254) NOT NULL UNIQUE,
	role_id int NOT NULL,
	username varchar(64) NOT NULL,
	first_name varchar(32),
	last_name varchar(32),
	birthday date,
	password_hash varchar(255) NOT NULL,
	is_active boolean NOT NULL,
	is_deleted boolean NOT NULL,
	phone_number varchar(15),
	updated_at timestamptz,
    created_at timestamptz NOT NULL,
	CONSTRAINT user_fk_role FOREIGN KEY (role_id) 
		REFERENCES public.role (id)
);

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

-- Table: session

CREATE TABLE IF NOT EXISTS public.session
(
    id uuid PRIMARY KEY,
    user_id uuid NOT NULL,
	session_hash char(64) NOT NULL,
	updated_at timestamptz NOT NULL,
	created_at timestamptz NOT NULL,
	user_agent text,
	CONSTRAINT session_fk_user FOREIGN KEY (user_id) 
		REFERENCES public.users (id)
);

ALTER TABLE IF EXISTS public.session
    OWNER to postgres;

-- Table: organizer

CREATE TABLE IF NOT EXISTS public.organizer
(
    id uuid PRIMARY KEY,
	name varchar(64) NOT NULL,
	description text,
	rating decimal(3,2) CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT organizer_fk_user FOREIGN KEY (id) 
        REFERENCES public.users (id)
);

ALTER TABLE IF EXISTS public.organizer
    OWNER to postgres;
