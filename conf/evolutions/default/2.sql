# user_location

# --- !Ups

CREATE TABLE user_area (
    id SERIAL PRIMARY KEY,
    uid integer NOT NULL,
    name varchar(255) NOT NULL,
    latitude numeric(21, 2) NOT NULL,
    longitude numeric(21, 2) NOT NULL,
    distance numeric(21, 2) NOT NULL
);
CREATE INDEX key_01 ON user_area (uid)

# --- !Downs

DROP TABLE user_area;
