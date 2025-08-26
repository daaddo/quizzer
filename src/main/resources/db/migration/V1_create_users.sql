
create table if not exists users (
    id int auto_increment primary key,
    username varchar(50) not null unique,
    password varchar(255) not null,
    email varchar(100) not null unique,
    enabled boolean not null default true,
    role enum('USER', 'ADMIN') not null default 'USER'
);

create table if not exists quiz (
    id int auto_increment primary key,
    title varchar(255) not null,
    description text,
    created_by int not null,
    questions_count int not null default 0,
    foreign key (created_by) references users(id) on delete cascade
);

INSERT INTO users( username, password, email)
value ('Claudia','{bcrypt}$2a$12$h3J.W9HPGt.MCdhgnYNDiuFuqvn1yfDmZIJFOJTD5VLCmXPzxNP4C', 'david.cascella.5@gmail.com');

INSERT INTO quiz (title, description, created_by,questions_count)
VALUES ('Sample Quiz', 'This is a sample quiz description.', (SELECT id FROM users LIMIT 1),(SELECT COUNT(*) FROM question));

ALTER TABLE question
    ADD COLUMN quiz_id INT ,
    ADD COLUMN user_id INT ,
    ADD CONSTRAINT fk_question_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE;

update question
set quiz_id = (select id from quiz limit1), user_id = (select id from users limit1);


ALTER TABLE question
MODIFY COLUMN quiz_id INT NOT NULL,
MODIFY COLUMN user_id INT NOT NULL;

