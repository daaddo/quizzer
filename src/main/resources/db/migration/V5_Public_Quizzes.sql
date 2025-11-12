#TODO AGGIUNGERE PARTE DELLA MIGRAZIONE DOVE SI CAMBIANO I JSON, IMPORTANTE!
#JSON INIZIALE DELLA TABELLA user_quiz_attempt
# {"252": {"correctOptions": [608], "selectedOptions": [609]}, "302": {"correctOptions": [611], "selectedOptions": [610]}, "304": {"correctOptions": [617], "selectedOptions": [616]}}
# JSON MODIFICATO DELLA TABELLA user_quiz_attempt
#[{"titolo": "23423423", "risposte": [{"testo": "423423423", "chosen": false, "corretta": false}, {"testo": "423423", "chosen": true, "corretta": true}], "descrizione": "43242343"}, {"titolo": "23423432", "risposte": [{"testo": "324234324", "chosen": false, "corretta": false}, {"testo": "23423432423423", "chosen": true, "corretta": true}], "descrizione": "342423432"}, {"titolo": "24324234", "risposte": [{"testo": "4444", "chosen": true, "corretta": false}, {"testo": "3333", "chosen": false, "corretta": true}], "descrizione": "22233"}, {"titolo": "432423423", "risposte": [{"testo": "32432423423", "chosen": true, "corretta": false}, {"testo": "234234234", "chosen": false, "corretta": true}], "descrizione": "4324324234"}, {"titolo": "werwerwer", "risposte": [{"testo": "werwerwerw", "chosen": false, "corretta": true}, {"testo": "werwerwer", "chosen": true, "corretta": false}], "descrizione": "ewrwrwe"}, {"titolo": "weeewww", "risposte": [{"testo": "errrerrr", "chosen": true, "corretta": false}, {"testo": "eerwwerwer", "chosen": false, "corretta": true}], "descrizione": "weeewww"}, {"titolo": "dfddfd", "risposte": [{"testo": "dddddd", "chosen": true, "corretta": false}, {"testo": "ffffff", "chosen": false, "corretta": true}], "descrizione": "dddfff"}]
ALTER TABLE quiz
    ADD COLUMN is_public BOOLEAN NOT NULL DEFAULT FALSE;
CREATE TABLE IF NOT EXISTS public_quiz_infos(
    quiz_id INT PRIMARY KEY ,
    review_count INT NOT NULL DEFAULT 0,
    average_rating DOUBLE NOT NULL DEFAULT 0,
    last_updated DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    last_update_comment TEXT,
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS public_quiz_modifications_log(
    id INT ,
    quiz_id INT NOT NULL,
    modified_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    details TEXT
)ENGINE ARCHIVE;
CREATE TABLE IF NOT EXISTS review(
    quiz_id INT NOT NULL,
    reviewer_id INT NOT NULL,
    rating INT NOT NULL CHECK (rating >= 1 AND rating <= 10),
    comment TEXT,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (quiz_id, reviewer_id),
    FOREIGN KEY (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
    FOREIGN KEY (reviewer_id) REFERENCES users(id) ON DELETE CASCADE
);

ALTER TABLE user_quiz_attempt
MODIFY COLUMN score DOUBLE DEFAULT 0;


CREATE TABLE IF NOT EXISTS quiz_results(
    id INT PRIMARY KEY AUTO_INCREMENT,
    quiz_id INT NOT NULL,
    user_id INT NOT NULL,
    score DOUBLE NOT NULL,
    token BINARY(32) DEFAULT NULL,
    results JSON,
    taken_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY  (quiz_id) REFERENCES quiz(id) ON DELETE CASCADE,
    FOREIGN KEY  (user_id) REFERENCES users(id) ON DELETE CASCADE
);