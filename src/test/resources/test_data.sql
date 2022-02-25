insert into ingredient values ('REG_BUN', 0.25, 'Regular Bun', 'BUN');
insert into ingredient values ('SES_BUN', 0.40, 'Sesame Bun', 'BUN');
insert into ingredient values ('GLU_BUN', 0.35, 'Gluten Free Bun', 'BUN');

insert into ingredient values ('B_PAT', 1.20, 'Beef Patty', 'BURGER');
insert into ingredient values ('CH_PAT', 0.99, 'Chicken Patty', 'BURGER');
insert into ingredient values ('VEG_PAT', 1.20, 'Vegetarian Patty', 'BURGER');

insert into ingredient values ('LETC', 0.10, 'Lettuce', 'VEGETABLE');
insert into ingredient values ('TOMA', 0.20, 'Tomato', 'VEGETABLE');

insert into ingredient values ('KETCH', 0.10, 'Ketchup', 'SAUCE');
insert into ingredient values ('MAYO', 0.10, 'Mayo', 'SAUCE');
insert into ingredient values ('CHIPO', 0.20, 'Chipotle', 'SAUCE');

insert into ingredient values ('BAC', 0.80, 'Bacon', 'OTHER');
insert into ingredient values ('CHEE', 0.30, 'Cheese', 'OTHER');
insert into ingredient values ('PIC', 0.25, 'Pickles', 'OTHER');

insert into burger(name) values ('Chicken Bacon Master');
select @burger_id:=id from burger where burger.name='Chicken Bacon Master';
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'REG_BUN', 1);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'CH_PAT',1);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'LETC',2);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'CHIPO',3);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'BAC',2);


insert into burger(name) values ('The Vegetarian');
select @burger_id:=id from burger where burger.name='The Vegetarian';
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'GLU_BUN',1);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'VEG_PAT',1);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'LETC',3);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'MAYO',3);


insert into burger(name) values ('The Classic');
select @burger_id:=id from burger where burger.name='The Classic';
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'SES_BUN',1);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'B_PAT',2);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'LETC',3);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'TOMA',4);
insert into burger_ingredients(burger_id, ingredients_key, amount) values (@burger_id, 'CHEE',2);