USE quizzer;
ALTER TABLE user_quiz_attempt
    MODIFY COLUMN status ENUM('IN_PROGRESS', 'COMPLETED', 'EXPIRED') NOT NULL;
ALTER TABLE issued_quiz
    ADD CONSTRAINT chk_expires_duration CHECK ( expires_at - issued_at > duration OR (expires_at IS NULL) );
