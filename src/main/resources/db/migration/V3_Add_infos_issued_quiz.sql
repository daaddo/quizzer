
ALTER TABLE issued_quiz
ADD COLUMN required_details BOOLEAN NOT NULL DEFAULT FALSE;

ALTER TABLE user_quiz_attempt
ADD COLUMN user_name VARCHAR(40),
ADD COLUMN middle_name VARCHAR(40),
ADD COLUMN surname VARCHAR(40);

DELIMITER $$

CREATE TRIGGER user_quiz_attempt_before_insert
    BEFORE INSERT ON user_quiz_attempt
    FOR EACH ROW
BEGIN
    DECLARE req TINYINT DEFAULT 0;

    -- leggi il flag required_details dalla tabella issued_quiz
    SELECT required_details INTO req
    FROM issued_quiz
    WHERE token_id = NEW.token
    LIMIT 1;

    -- se required_details = 1 e nome/cognome mancanti -> blocca con errore
    IF req = 1 AND (COALESCE(NEW.user_name,'') = '' OR COALESCE(NEW.surname,'') = '') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User details required for this quiz';
    END IF;
END$$

CREATE TRIGGER user_quiz_attempt_before_update
    BEFORE UPDATE ON user_quiz_attempt
    FOR EACH ROW
BEGIN
    DECLARE req TINYINT DEFAULT 0;

    SELECT required_details INTO req
    FROM issued_quiz
    WHERE token_id = NEW.token
    LIMIT 1;

    IF req = 1 AND (COALESCE(NEW.user_name,'') = '' OR COALESCE(NEW.surname,'') = '') THEN
        SIGNAL SQLSTATE '45000' SET MESSAGE_TEXT = 'User details required for this quiz';
    END IF;
END$$

DELIMITER ;
