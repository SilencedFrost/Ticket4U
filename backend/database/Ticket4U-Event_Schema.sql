DROP TABLE IF EXISTS public.seats;
DROP TABLE IF EXISTS public.zones;
DROP TABLE IF EXISTS public.events;
DROP TABLE IF EXISTS public.categories;
DROP SEQUENCE IF EXISTS categories_id_seq;

-- Sequence for categories.id (managed via @SequenceGenerator in Category entity)
CREATE SEQUENCE IF NOT EXISTS categories_id_seq START WITH 1 INCREMENT BY 1;

-- Table: categories
CREATE TABLE IF NOT EXISTS public.categories (
	id 		INTEGER,
	name 	VARCHAR(255) NOT NULL UNIQUE,
	PRIMARY KEY (id)
);

-- Table: events
-- status: editing, premier ,selling, paused (stop selling),ongoing ,finished ,cancelled
CREATE TABLE IF NOT EXISTS public.events (
	id 						UUID PRIMARY KEY,
	name 					VARCHAR(255) NOT NULL UNIQUE,
	organizer_id 			UUID NOT NULL,
	category_id 			INTEGER, -- null vì có thể chưa phân loại ngay khi tạo sự kiện
	address_line 			VARCHAR(255) NOT NULL,
	start_date 				TIMESTAMPTZ,
	end_date 				TIMESTAMPTZ,
	status 					VARCHAR(50) NOT NULL,
	banner_url 				TEXT,
	description 			TEXT,

	created_at 				TIMESTAMPTZ NOT NULL,
	updated_at 				TIMESTAMPTZ,
	cancelled_at 			TIMESTAMPTZ,

	about_vi 				TEXT,
	about_en				TEXT,
	terms_and_conditions 	TEXT,
	policy_refund 			TEXT,
	seating_plan_image_url 	TEXT,

	CONSTRAINT event_fk_category FOREIGN KEY (category_id)
		REFERENCES public.categories (id)
);

-- Table: zones
-- purchase_limit: null means no limit
-- perks: Save the perks of each zone in JSON format, for example: ["Free drink", "Lightstick", "Fansign"]
CREATE TABLE IF NOT EXISTS public.zones (
	id 					UUID PRIMARY KEY,
	event_id 			UUID NOT NULL,
	name 				VARCHAR(255) NOT NULL,
	is_standing 		BOOLEAN NOT NULL,
	capacity 			INTEGER,
	quantity_sold 		INTEGER,
	purchase_limit 		INTEGER,
	price 				DECIMAL(10, 2) NOT NULL,

	description 		TEXT,
	gift_image_url 		VARCHAR(512),
    perks 				JSONB,
    created_at 			TIMESTAMPTZ NOT NULL,
	updated_at 			TIMESTAMPTZ,

	CONSTRAINT zone_fk_event FOREIGN KEY (event_id)
		REFERENCES public.events (id)
);

-- Table: seats
-- price_override: if you want to set a different price for a specific seat in a zone
CREATE TABLE IF NOT EXISTS public.seats (
	id 				UUID PRIMARY KEY,
	zone_id 		UUID NOT NULL,
	name 			VARCHAR(255),
	row_name 		VARCHAR(5),
	col_name 		VARCHAR(5),
	seat_code 		VARCHAR(20),
	status 			VARCHAR(50) NOT NULL,
	price_override 	DECIMAL(10, 2),
	CONSTRAINT seat_fk_zone FOREIGN KEY (zone_id)
		REFERENCES public.zones (id)
);