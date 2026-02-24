-- Drops
DROP TABLE IF EXISTS public.ticket;
DROP TABLE IF EXISTS public.order;

-- Table: order

CREATE TABLE IF NOT EXISTS public.order
(
	id uuid PRIMARY KEY,
	user_id uuid NOT NULL,
	total_amount decimal(10, 2) NOT NULL,
	discount_code varchar(50),
	discount_amount decimal(10, 2),
	fees decimal(10, 2),
	email varchar(255) NOT NULL,
	status varchar(32) NOT NULL,
	payment_method varchar(50),
	payment_status varchar(32) NOT NULL,
	transaction_id varchar(255),
	created_at timestamptz NOT NULL,
	purchased_at timestamptz,
	expires_at timestamptz,
	updated_at timestamptz NOT NULL,
	cancelled_at timestamptz,
	refund_amount decimal(10, 2),
	notes text
);

ALTER TABLE IF EXISTS public.order
    OWNER to postgres;

-- Table: ticket

CREATE TABLE IF NOT EXISTS public.ticket
(
	id uuid PRIMARY KEY,
	order_id uuid UNIQUE,
	event_id uuid NOT NULL,
	seat_id uuid NOT NULL,
	seat_name varchar(32) NOT NULL,
	zone_id uuid NOT NULL,
	zone_name VARCHAR(255) NOT NULL,
	ticket_type varchar(32) NOT NULL,
	base_price decimal(10, 2) NOT NULL,
	status varchar(32) NOT NULL,
	created_at timestamptz NOT NULL,
	used_at timestamptz,
	currency char(3) NOT NULL,
	qr_secret varchar(64) NOT NULL,
	FOREIGN KEY (order_id) REFERENCES public.order(id) ON DELETE CASCADE
);

ALTER TABLE IF EXISTS public.ticket
    OWNER to postgres;