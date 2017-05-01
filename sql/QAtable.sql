create table question_and_answer(
	id int primary key auto_increment,
	child_topic_id int not null,
	question_id int not null,
	question_name varchar(50) not null,
	answer_id int not null,
	username varchar(50) not null
);