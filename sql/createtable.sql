create table main_topic(
	id int primary key auto_increment,
	main_topic_id int not null,
	main_topic_name varchar(50) not null
);

create table child_topic(
	id int primary key auto_increment,
	main_topic_id int not null,
	child_topic_id int not null,
	child_topic_name varchar(50) not null
);