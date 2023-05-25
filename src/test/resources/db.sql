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
END
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


INSERT INTO income_categories
(title, "type")
VALUES
('Salary', 'REGULAR'),
('Investment', 'REGULAR'),
('Gift', 'ONE_TIME');

INSERT INTO expense_categories
(title, "type")
VALUES
('Rent', 'ESSENTIAL'),
('Concerts', 'FUN'),
('Food', 'ESSENTIAL'),
('Travel', 'FUN'),
('Transport', 'ESSENTIAL');


INSERT INTO incomes
(amount, amountinmaincurrency, currency, "date", description, maincurrency, "month", rate, title, "year", category)
VALUES
(1000, 120000, 'EUR', '2022-01-03', 'Salary', 'RSD', 1, 1.2, 'January salary', 2022, 1),
(1000, 120000, 'EUR', '2022-02-03', 'investment', 'RSD', 2, 1.2, 'February investment', 2022, 2),
(1000, 120000, 'EUR', '2022-03-03', 'Salary', 'RSD', 3, 1.2, 'March salary', 2022, 1),
(1000, 120000, 'EUR', '2022-04-03', 'Huge gift', 'RSD', 4, 1.2, 'April gift', 2022, 3);



INSERT INTO expenses
(amount, amountinmaincurrency, currency, "date", description, maincurrency, "month", rate, title, "year", category)
VALUES
(100, 12000, 'EUR', '2022-01-07', 'Rent', 'RSD', 1, 1.2, 'January rent', 2022, 1),
(100, 12000, 'EUR', '2022-02-07', 'Groceries', 'RSD', 2, 1.2, 'Groceries', 2022, 3),
(100, 12000, 'EUR', '2022-03-07', 'Taxi', 'RSD', 3, 1.2, 'Taxi', 2022, 5),
(100, 12000, 'EUR', '2022-04-07', 'Green day concert', 'RSD', 4, 1.2, 'Green day concert', 2022, 2);




