
-- Create a new schema
CREATE SCHEMA IF NOT EXISTS account;

-- Set search path to the new schema
SET search_path TO account;

-- Create the ACCOUNT table in the new schema
CREATE TABLE IF NOT EXISTS account.account (
  ID UUID PRIMARY KEY,
  OWNER VARCHAR NOT NULL,
  BALANCE DECIMAL(19, 2) DEFAULT 0.00, -- Balance with precision
  CREATION_DATE TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  VERSION BIGINT DEFAULT 1
);
