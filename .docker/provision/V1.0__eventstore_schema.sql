CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE IF NOT EXISTS ES_AGGREGATE (
  ID                        UUID        DEFAULT uuid_generate_v4() PRIMARY KEY,
  AGGREGATE_TYPE            TEXT        NOT NULL,
  AGGREGATE_VERSION         INTEGER     NOT NULL
);

CREATE INDEX IF NOT EXISTS IDX_ES_AGGREGATE_AGGREGATE_TYPE ON ES_AGGREGATE (AGGREGATE_TYPE);

CREATE TABLE IF NOT EXISTS ES_EVENT (
  ID                        UUID        DEFAULT uuid_generate_v4() PRIMARY KEY,
  OFFSET_TXID               XID8        NOT NULL DEFAULT pg_current_xact_id(),
  AGGREGATE_ID              UUID        NOT NULL REFERENCES ES_AGGREGATE (ID),
  AGGREGATE_VERSION         BIGINT      NOT NULL,
  EVENT_TYPE                TEXT        NOT NULL,
  DATA_CONTENT_TYPE         TEXT,
  DATA                      JSON        NOT NULL,
  DATA_BASE64               TEXT,
  OFFSET_DATE_TIME          TIMESTAMP   WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP, -- Set default to current timestamp
  EXTENSION                 JSON,
  UNIQUE (AGGREGATE_ID, AGGREGATE_VERSION)
);

CREATE INDEX IF NOT EXISTS IDX_ES_EVENT_TRANSACTION_ID_ID ON ES_EVENT (OFFSET_TXID, ID);
CREATE INDEX IF NOT EXISTS IDX_ES_EVENT_AGGREGATE_ID ON ES_EVENT (AGGREGATE_ID);
CREATE INDEX IF NOT EXISTS IDX_ES_EVENT_VERSION ON ES_EVENT (AGGREGATE_VERSION);

CREATE TABLE IF NOT EXISTS ES_AGGREGATE_SNAPSHOT (
  ID                        UUID        DEFAULT uuid_generate_v4() PRIMARY KEY,
  AGGREGATE_ID              UUID        NOT NULL REFERENCES ES_AGGREGATE (ID),
  AGGREGATE_VERSION         INTEGER     NOT NULL,
  JSON_DATA                 JSON        NOT NULL,
  UNIQUE (AGGREGATE_ID, AGGREGATE_VERSION)
);

CREATE INDEX IF NOT EXISTS IDX_ES_AGGREGATE_SNAPSHOT_AGGREGATE_ID ON ES_AGGREGATE_SNAPSHOT (AGGREGATE_ID);
CREATE INDEX IF NOT EXISTS IDX_ES_AGGREGATE_SNAPSHOT_VERSION ON ES_AGGREGATE_SNAPSHOT (AGGREGATE_VERSION);

CREATE TABLE IF NOT EXISTS ES_EVENT_SUBSCRIPTION (
  ID                    UUID        DEFAULT uuid_generate_v4() PRIMARY KEY,
  SUBSCRIPTION_NAME     TEXT        NOT NULL,
  OFFSET_TXID           XID8        NOT NULL,
  EVENT_ID              BIGINT      NOT NULL,
  UNIQUE (SUBSCRIPTION_NAME)
);
