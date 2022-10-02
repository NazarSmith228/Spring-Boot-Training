CREATE TABLE IF NOT EXISTS shortened_urls
(
    id           BIGSERIAL
        CONSTRAINT shortened_urls_pk PRIMARY KEY,
    url_id       TEXT      NOT NULL
        CONSTRAINT shortened_urls_url_id_uq UNIQUE,
    original_url TEXT      NOT NULL
        CONSTRAINT shortened_urls_original_url_uq UNIQUE,
    title        TEXT      NOT NULL,
    created_at   TIMESTAMP NOT NULL DEFAULT now()
);