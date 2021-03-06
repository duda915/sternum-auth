
CREATE TABLE IF NOT EXISTS service_user (
  id SERIAL PRIMARY KEY,
  user_name VARCHAR(255) UNIQUE NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  password CHAR(60) NOT NULL,
  image_link VARCHAR(255),
  active boolean NOT NULL
);

CREATE TABLE IF NOT EXISTS service_user_authority (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES service_user(id),
  authority VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS registration_token (
  id SERIAL PRIMARY KEY,
  user_id INTEGER REFERENCES service_user(id),
  token VARCHAR(255) NOT NULL UNIQUE,
  registration_date DATE NOT NULL
);