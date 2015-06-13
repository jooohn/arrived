# user_location

# --- !Ups

CREATE TABLE user_location (
    uid integer NOT NULL PRIMARY KEY,
    latitude numeric(21, 2) NOT NULL,
    longitude numeric(21, 2) NOT NULL,
    updated_at timestamp without time zone NOT NULL
);

# --- !Downs

DROP TABLE user_location;
