CREATE TABLE query (
  id UUID PRIMARY KEY,
  name VARCHAR(50) NOT NULL UNIQUE,
  start_date TIMESTAMP,
  end_date TIMESTAMP,
  hash_tags JSONB NOT NULL,
  allow_retweets BOOLEAN NOT NULL
);
