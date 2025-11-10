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

show create table quiz;