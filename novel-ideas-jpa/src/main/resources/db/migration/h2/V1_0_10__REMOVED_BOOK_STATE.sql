ALTER TABLE book
    ADD state VARCHAR(255);

ALTER TABLE book
DROP
CONSTRAINT fk_book_on_state;

DROP TABLE book_state CASCADE;

ALTER TABLE book
DROP
COLUMN state_id;