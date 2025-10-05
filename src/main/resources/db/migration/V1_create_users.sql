use quizzer;
alter table question
add column is_multiple_choice boolean not null default false;


update question q
set is_multiple_choice = true
where q.id in (select question_id from answer where answer.is_correct = true group by question_id having count(*) > 1);


create table if not exists users (
    id int auto_increment primary key,
    username varchar(50) not null unique,
    password varchar(255) ,
    email varchar(100) not null unique,
    enabled boolean not null default true,
    profile_picture_url varchar(2048),
    role enum('USER', 'ADMIN') not null default 'USER'
);

create table if not exists quiz (
    id int auto_increment primary key,
    title varchar(255) not null,
    description text,
    user_id int not null,
    questions_count int not null default 0,
    foreign key (user_id) references users(id) on delete cascade
);
/* TODO aggiungere feedback in backend */
create table feedbacks (
    id int auto_increment primary key,
    user_id int ,
    username varchar(50) not null,
    quiz_id int ,
    rating int not null check (rating >= 1 and rating <= 5),
    comment text,
    created_at timestamp default current_timestamp,
    foreign key (user_id) references users(id) on delete set null ,
    foreign key (quiz_id) references quiz(id) on delete set null
);
INSERT INTO users( username, password, email)
value ('Claudia','{bcrypt}$2a$12$h3J.W9HPGt.MCdhgnYNDiuFuqvn1yfDmZIJFOJTD5VLCmXPzxNP4C', 'david.cascella.5@gmail.com');

INSERT INTO users( username, password, email, role)
    value ('Admin','{bcrypt}$2a$12$h3J.W9HPGt.MCdhgnYNDiuFuqvn1yfDmZIJFOJTD5VLCmXPzxNP4C', 'david.cascellaa.5@gmail.com', 'ADMIN');

INSERT INTO quiz (title, description, user_id,questions_count)
VALUES ('Sample Quiz', 'This is a sample quiz description.', (SELECT id FROM users where users.username like "Claudia" LIMIT 1),(SELECT COUNT(*) FROM question));

ALTER TABLE question
    ADD COLUMN quiz_id INT ,
    ADD COLUMN user_id INT ,
    ADD CONSTRAINT fk_question_user
        FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    ADD CONSTRAINT fk_question_quiz
        FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE;

update question
set quiz_id = (select id from quiz limit1), user_id = (select id from users where users.username like "Claudia" limit 1);


ALTER TABLE question
MODIFY COLUMN quiz_id INT NOT NULL,
MODIFY COLUMN user_id INT NOT NULL;

CREATE TABLE IF NOT EXISTS issued_quiz(
    token_id BINARY(32) PRIMARY KEY NOT NULL UNIQUE ,
    issuer_id INT NOT NULL,
    quiz_id INT ,
    number_of_questions INT NOT NULL,
    issued_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    expires_at DATETIME DEFAULT NULL,
    duration TIME DEFAULT NULL,
    FOREIGN KEY (issuer_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE SET NULL
);



CREATE TABLE IF NOT EXISTS user_quiz_attempt (
    id INT AUTO_INCREMENT PRIMARY KEY,
    user_id INT NOT NULL,
    token BINARY(32) NOT NULL,
    score INT,
    attempted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    finished_at TIMESTAMP DEFAULT NULL,
    status ENUM('IN_PROGRESS', 'COMPLETED') NOT NULL DEFAULT 'IN_PROGRESS',
    questions JSON ,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (token) REFERENCES issued_quiz(token_id) ON DELETE CASCADE
);
