--liquibase formatted sql

--changeset vulpecula:1 labels:v0.0.1
CREATE TABLE "entries" (
    "id" text primary key constraint entries_id_length_ctr check (length("id") < 64),
    "user_id" text not null constraint entries_user_id_length_ctr check (length("user_id") < 64),
    "movie_id" text not null constraint entries_movie_id_length_ctr check (length("movie_id") < 64),
    "viewing_date" date not null,
    "rating" integer not null constraint entries_rating_range_ctr check ("rating" between 1 and 10),
    "comment" text constraint entries_comment_length_ctr check (length("comment") <= 2000),
    "lock" text not null constraint entries_lock_length_ctr check (length("lock") < 64),
    "created_at" timestamp with time zone not null,
    "updated_at" timestamp with time zone not null
);

CREATE INDEX entries_user_id_idx on "entries" using hash ("user_id");
CREATE INDEX entries_movie_id_idx on "entries" using hash ("movie_id");
CREATE INDEX entries_viewing_date_idx on "entries" ("viewing_date");
