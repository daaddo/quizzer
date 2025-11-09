CREATE TABLE necessary_questions(
    id INT,
    issued_quiz_token BINARY(32),
    PRIMARY KEY (id, issued_quiz_token),
    FOREIGN KEY (id) REFERENCES question(id) ON DELETE CASCADE,
    FOREIGN KEY (issued_quiz_token) REFERENCES issued_quiz(token_id) ON DELETE CASCADE
);