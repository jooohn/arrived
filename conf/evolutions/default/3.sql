# event

# --- !Ups

CREATE TABLE event (
  id SERIAL PRIMARY KEY,
  uid INTEGER NOT NULL,
  user_area_id INTEGER NOT NULL,
  trigger_type INTEGER NOT NULL,
  action_type INTEGER NOT NULL,
  options TEXT
);

CREATE INDEX "event__uid" ON "event" (uid);
CREATE INDEX "event__user_area_id" ON "event" (user_area_id);

# --- !Downs

DROP TABLE event;
