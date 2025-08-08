
create table if not exists users (
    id int auto_increment primary key,
    username varchar(50) not null unique,
    password varchar(255) not null,
    email varchar(100) not null unique,
    role enum('USER', 'ADMIN') not null default 'USER'
);

create table if not exists quiz (
    id int auto_increment primary key,
    title varchar(255) not null,
    description text,
    created_by int not null,
    foreign key (created_by) references users(id) on delete cascade
);

INSERT INTO users( username, password, email)
value ('Claudia','$2a$12$0qnIRgp6WUmmgddbXlSfGeyjHVdoWoYI/OZh3nZf0PYm6A5wl34iW', 'david.cascella.5@gmail.com');

ALTER TABLE question
    ADD COLUMN quiz_id INT ,
    ADD COLUMN user_id INT ,
    ADD CONSTRAINT fk_question_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE;

update question
set quiz_id = null, user_id = null;
