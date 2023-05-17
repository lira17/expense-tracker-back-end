DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS income_categories;
DROP TABLE IF EXISTS expense_categories;
DROP TABLE IF EXISTS expenses;
DROP TABLE IF EXISTS incomes;

CREATE TABLE income_categories (
	id bigserial NOT NULL,
	title varchar(255) NULL,
	"type" varchar(255) NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT income_categories_pkey PRIMARY KEY (id),
	CONSTRAINT uk_cewn1bjdqc6m9lo904b4fukuc UNIQUE (title)
);


CREATE TABLE incomes (
	id bigserial NOT NULL,
	amount float8 NULL,
	amountinmaincurrency float8 NULL,
	currency varchar(255) NULL,
	"date" date NULL,
	description varchar(255) NULL,
	maincurrency varchar(255) NULL,
	"month" int4 NULL,
	rate float8 NULL,
	title varchar(255) NULL,
	"year" int4 NULL,
	category int8 NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT incomes_pkey PRIMARY KEY (id)
);


CREATE TABLE expenses (
	id bigserial NOT NULL,
	amount float8 NULL,
	amountinmaincurrency float8 NULL,
	currency varchar(255) NULL,
	"date" date NULL,
	description varchar(255) NULL,
	maincurrency varchar(255) NULL,
	"month" int4 NULL,
	rate float8 NULL,
	title varchar(255) NULL,
	"year" int4 NULL,
	category int8 NOT NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT expenses_pkey PRIMARY KEY (id)
);

CREATE TABLE expense_categories (
	id bigserial NOT NULL,
	title varchar(255) NULL,
	"type" varchar(255) NULL,
	created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
	CONSTRAINT expense_categories_pkey PRIMARY KEY (id),
	CONSTRAINT uk_htkl7kj4ew9qguaedk5a1a9at UNIQUE (title)
);

CREATE TABLE users (
    id  bigserial not null,
    email varchar(255) unique,
    name varchar(255) unique,
    password varchar(255),
    created_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    updated_at TIMESTAMPTZ NOT NULL DEFAULT NOW(),
    primary key (id)
);


ALTER TABLE expenses
ADD CONSTRAINT fkix9wbn3mpo58mwos30u2abgjk
FOREIGN KEY (category) REFERENCES expense_categories(id);

ALTER TABLE incomes
ADD CONSTRAINT fk9o0ivtuy46dxlbnkujp47ur0b
FOREIGN KEY (category) REFERENCES income_categories(id);

CREATE OR REPLACE FUNCTION trigger_set_timestamp()
RETURNS TRIGGER AS $$
BEGIN
  NEW.updated_at = NOW();
  RETURN NEW;
END;
$$ LANGUAGE plpgsql;

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON expense_categories
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON income_categories
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON expenses
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON incomes
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();

CREATE TRIGGER set_timestamp
BEFORE UPDATE ON users
FOR EACH ROW
EXECUTE PROCEDURE trigger_set_timestamp();




