CREATE TABLE query (
  id UUID PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  start_date TIMESTAMP,
  end_date TIMESTAMP,
  allow_retweets BOOLEAN NOT NULL
);
