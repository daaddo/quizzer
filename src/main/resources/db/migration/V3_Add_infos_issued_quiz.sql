
ALTER TABLE issued_quiz
ADD COLUMN required_details BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE user_quiz_attempt
ADD COLUMN user_name VARCHAR(40),
ADD COLUMN middle_name VARCHAR(40),
ADD COLUMN surname VARCHAR(40),
ADD CONSTRAINT issued_quiz_required_details_chk CHECK (
    (SELECT required_details FROM issued_quiz WHERE issued_quiz.token_id = user_quiz_attempt.token)
     && (user_quiz_attempt.name IS NOT NULL AND user_quiz_attempt.surname IS NOT NULL)
    );