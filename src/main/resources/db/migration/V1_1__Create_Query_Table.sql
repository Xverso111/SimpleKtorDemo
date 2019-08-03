CREATE TABLE Query (
  "IdQuery" UUID PRIMARY KEY,
  "NameQuery" VARCHAR(50) NOT NULL UNIQUE,
  "StartDateQuery" TIMESTAMP,
  "EndDateQuery" TIMESTAMP,
  "AllowRetweetsQuery" BOOLEAN NOT NULL
);
