INSERT INTO register DEFAULT VALUES;
INSERT INTO register DEFAULT VALUES;
INSERT INTO register DEFAULT VALUES;
INSERT INTO register DEFAULT VALUES;

INSERT INTO movie (name, description, media)
VALUES ('Harry Potter and the Sorcerer''s Stone', NULL, array_to_json(
    '{https://www.youtube.com/watch?v=eKSB0gXl9dw, http://www.imdb.com/title/tt0241527/}' :: TEXT [])),
  ('The Dark Knight',
   'When the menace known as the Joker emerges from his mysterious past, he wreaks havoc and chaos on the people of Gotham, the Dark Knight must accept one of the greatest psychological and physical tests of his ability to fight injustice.',
   array_to_json('{https://www.youtube.com/watch?v=eKSB0gXl9dw, http://www.imdb.com/title/tt0468569/}' :: TEXT [])),
  ('Star Wars: Episode IV - A New Hope',
   'Luke Skywalker joins forces with a Jedi Knight, a cocky pilot, a wookiee and two droids to save the galaxy from the Empire''s world-destroying battle-station, while also attempting to rescue Princess Leia from the evil Darth Vader.',
   NULL),
  ('It Comes at Night', NULL, NULL);

INSERT INTO seat (seat_type)
VALUES ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'),
  ('parquet'),
  ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'), ('parquet'),
  ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'), ('loge'),
  ('loge'),
  ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'), ('VIP'),
  ('VIP');

INSERT INTO theater (floor, seat_count, "3d_ready")
VALUES ('3', '42', TRUE), ('1', '42', FALSE), ('2', '42', TRUE), ('3', '42', TRUE), ('2', '42', FALSE),
  ('1', '42', TRUE);

INSERT INTO ticket (price)
VALUES ('10.50'), ('$10.50'), ('$10.50'), ('$10.50'), ('$10.50'),
  ('$7.30'), ('$7.30'), ('$7.30'), ('$7.30'), ('$9.75'), ('$9.75'), ('$9.75'),
  ('$9.75'), ('$9.75'), ('$9.75'), ('$9.75'), ('$9.75');

INSERT INTO sells (register_id, ticket_id)
VALUES ('2', '1'), ('3', '2'), ('1', '3'), ('4', '4'), ('2', '5'),
  ('1', '6'), ('4', '7'), ('4', '8'), ('2', '9'), ('1', '10'),
  ('2', '11'), ('3', '12'), ('2', '13'), ('3', '14'), ('4', '15'),
  ('1', '16'), ('4', '17');

INSERT INTO screening (datetime, movie_id, theater_id)
VALUES (to_timestamp('21-07-2017 17:00:00', 'dd-mm-yyyy hh24:mi:ss'), '1', '6'),
  (to_timestamp('18-07-2017 19:45:00', 'dd-mm-yyyy hh24:mi:ss'), '3', '2'),
  (to_timestamp('19-07-2017 20:15:00', 'dd-mm-yyyy hh24:mi:ss'), '4', '4'),
  (to_timestamp('23-07-2017 21:30:00', 'dd-mm-yyyy hh24:mi:ss'), '2', '1');

INSERT INTO reserves (ticket_id, seat_id, screening_id)
VALUES ('1', '32', '1'), ('2', '42', '2'), ('3', '33', '4'), ('4', '29', '2'), ('5', '39', '2'),
  ('6', '2', '2'), ('7', '4', '3'), ('8', '7', '4'), ('9', '12', '1'),
  ('10', '17', '1'), ('11', '19', '1'), ('12', '23', '3'), ('13', '28', '2'),
  ('14', '20', '4'), ('15', '21', '2'), ('16', '27', '1'), ('17', '25', '4');