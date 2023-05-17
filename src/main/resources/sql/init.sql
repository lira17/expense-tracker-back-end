INSERT INTO users (email, name, password)
VALUES ('mail.com', 'Viktoriia', '$2a$10$aNbyIp3PNPqObqjmRHhgje26scOwFWfVAQYJjRJhvHASHzIQ02jBK' );


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
(1000, 120000, 'EUR', '2022-02-03', 'Salary', 'RSD', 2, 1.2, 'February salary', 2022, 1),
(1000, 120000, 'EUR', '2022-03-03', 'Salary', 'RSD', 3, 1.2, 'March salary', 2022, 1),
(1000, 120000, 'EUR', '2022-04-03', 'Salary', 'RSD', 4, 1.2, 'April salary', 2022, 1);



INSERT INTO expenses
(amount, amountinmaincurrency, currency, "date", description, maincurrency, "month", rate, title, "year", category)
VALUES
(100, 12000, 'EUR', '2022-01-07', 'Salary', 'RSD', 1, 1.2, 'January rent', 2022, 1),
(100, 12000, 'EUR', '2022-02-07', 'Salary', 'RSD', 2, 1.2, 'February rent', 2022, 1),
(100, 12000, 'EUR', '2022-03-07', 'Salary', 'RSD', 3, 1.2, 'March rent', 2022, 1),
(100, 12000, 'EUR', '2022-04-07', 'Salary', 'RSD', 4, 1.2, 'April rent', 2022, 1);