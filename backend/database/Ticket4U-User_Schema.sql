-- Drops

DROP TABLE IF EXISTS public.customer;
DROP TABLE IF EXISTS public.organizer;
DROP TABLE IF EXISTS public.admin;

DROP TABLE IF EXISTS public.session;
DROP TABLE IF EXISTS public.users;
DROP TABLE IF EXISTS public.role;

-- Table: role

CREATE TABLE IF NOT EXISTS public.role
(
	role_id int PRIMARY KEY,
    role_name varchar(32) NOT NULL
);

ALTER TABLE IF EXISTS public.role
    OWNER to postgres;

-- Table: users

CREATE TABLE IF NOT EXISTS public.users
(
    user_id uuid PRIMARY KEY,
    email varchar(254) NOT NULL UNIQUE,
	role_id int,
	username varchar(64),
	first_name varchar(32),
	last_name varchar(32),
	birthday date,
	password_hash varchar(255) NOT NULL,
	is_active boolean NOT NULL,
	deleted boolean NOT NULL,
	phone_number varchar(15),
	updated_at timestamptz,
    created_at timestamptz NOT NULL,
	CONSTRAINT user_fk_role FOREIGN KEY (role_id) 
		REFERENCES public.role (role_id)
);

ALTER TABLE IF EXISTS public.users
    OWNER to postgres;

-- Table: session

CREATE TABLE IF NOT EXISTS public.session
(
    session_id bigint GENERATED ALWAYS AS IDENTITY ( INCREMENT 1 START 1 CACHE 1 ) PRIMARY KEY,
    user_id uuid NOT NULL,
	session_hash char(64) NOT NULL,
	last_accessed timestamptz NOT NULL,
	created_at timestamptz NOT NULL,
	is_active boolean NOT NULL,
	revoke_reason varchar(128),
	user_agent text,
	CONSTRAINT session_fk_user FOREIGN KEY (user_id) 
		REFERENCES public.users (user_id)
);

ALTER TABLE IF EXISTS public.session
    OWNER to postgres;

-- Table: admin

CREATE TABLE IF NOT EXISTS public.admin
(
    user_id uuid PRIMARY KEY,
    CONSTRAINT admin_fk_user FOREIGN KEY (user_id) 
        REFERENCES public.users (user_id)
);

ALTER TABLE IF EXISTS public.admin
    OWNER to postgres;

-- Table: organizer

CREATE TABLE IF NOT EXISTS public.organizer
(
    user_id uuid PRIMARY KEY,
	organizer_name varchar(64) NOT NULL,
	organizer_description text,
	rating decimal(3,2) CHECK (rating >= 0 AND rating <= 5),
    CONSTRAINT organizer_fk_user FOREIGN KEY (user_id) 
        REFERENCES public.users (user_id)
);

ALTER TABLE IF EXISTS public.organizer
    OWNER to postgres;

-- Table: customer

CREATE TABLE IF NOT EXISTS public.customer
(
    user_id uuid PRIMARY KEY,
    CONSTRAINT customer_fk_user FOREIGN KEY (user_id)
        REFERENCES public.users (user_id)
);

ALTER TABLE IF EXISTS public.customer
    OWNER to postgres;
