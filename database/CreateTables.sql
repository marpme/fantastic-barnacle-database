DROP TABLE IF EXISTS register;
DROP TABLE IF EXISTS ticket;
DROP TABLE IF EXISTS seat;
DROP TABLE IF EXISTS movie;
DROP TABLE IF EXISTS theater;
DROP TABLE IF EXISTS screening;
DROP TABLE IF EXISTS sells;
DROP TABLE IF EXISTS reserves;
DROP TYPE IF EXISTS SEAT_CATEGORY;

CREATE TABLE register
(
  id SERIAL PRIMARY KEY NOT NULL
);
CREATE UNIQUE INDEX Unique_Kassen_index
  ON register (id);

CREATE TABLE ticket
(
  id    SERIAL PRIMARY KEY NOT NULL,
  price MONEY DEFAULT 0    NOT NULL
);
CREATE UNIQUE INDEX Unique_Ticket_index
  ON ticket (id);

CREATE TYPE SEAT_CATEGORY AS ENUM ('parquet', 'loge', 'VIP');
CREATE TABLE seat
(
  id        SERIAL PRIMARY KEY              NOT NULL,
  seat_type SEAT_CATEGORY DEFAULT 'parquet' NOT NULL
);
CREATE UNIQUE INDEX Unique_Seat_index
  ON seat (id);

CREATE TABLE movie
(
  id          SERIAL PRIMARY KEY NOT NULL,
  name        VARCHAR(100)       NOT NULL,
  description VARCHAR(1000),
  media       JSON
);
CREATE UNIQUE INDEX Unique_movie_index
  ON movie (id);

CREATE TABLE theater
(
  id         SERIAL PRIMARY KEY NOT NULL,
  floor      INT                NOT NULL,
  seat_count INT                NOT NULL,
  "3d_ready" BOOLEAN DEFAULT FALSE
);
CREATE UNIQUE INDEX Unique_theater_index
  ON theater (id);

CREATE TABLE screening
(
  id         SERIAL PRIMARY KEY NOT NULL,
  datetime   TIMESTAMP          NOT NULL,
  movie_id   INT                NOT NULL,
  theater_id INT                NOT NULL,
  CONSTRAINT movie_id FOREIGN KEY (movie_id) REFERENCES movie (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT theater_id FOREIGN KEY (theater_id) REFERENCES theater (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE UNIQUE INDEX Unique_screening_index
  ON screening (id);

CREATE TABLE sells
(
  register_id INT NOT NULL,
  ticket_id   INT NOT NULL,
  CONSTRAINT register_id FOREIGN KEY (register_id) REFERENCES register (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT ticket_id FOREIGN KEY (ticket_id) REFERENCES ticket (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE UNIQUE INDEX Unique_sells_index
  ON sells (ticket_id);

CREATE TABLE reserves
(
  ticket_id    INT NOT NULL,
  seat_id      INT NOT NULL,
  screening_id INT NOT NULL,
  CONSTRAINT ticket_id FOREIGN KEY (ticket_id) REFERENCES ticket (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT seat_id FOREIGN KEY (seat_id) REFERENCES seat (id) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT screening_id FOREIGN KEY (screening_id) REFERENCES screening (id) ON DELETE CASCADE ON UPDATE CASCADE
);
CREATE UNIQUE INDEX Unique_reserves_index
  ON reserves (ticket_id);