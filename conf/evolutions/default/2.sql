# --- !Ups
CREATE TABLE contracts (
  start_date TIMESTAMP NOT NULL,
  end_date TIMESTAMP
  contract_type BOOLEAN NOT NULL,
  contract_time BOOLEAN NOT NULL,
  hours_week INT NOT NULL

);

# --- !Downs
DROP TABLE contracts;