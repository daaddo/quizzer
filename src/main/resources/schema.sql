create database if not exists quizzer;
use quizzer;
create table if not exists question (
    id int auto_increment primary key,
    title varchar(255) not null,
    question text not null
);
create table answer (
    id int auto_increment primary key,
    question_id int not null,
    answer text not null,
    is_correct boolean not null default false,
    foreign key (question_id) references question(id) on delete cascade
);


select * from question;
select * from answer;