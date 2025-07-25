# --- !Ups
CREATE TABLE employees (
  id               INT AUTO_INCREMENT PRIMARY KEY,
  first_name       VARCHAR(100) NOT NULL,
  last_name        VARCHAR(100) NOT NULL,
  email            VARCHAR(255) NOT NULL,
  mobile_number    VARCHAR(50),
  address          VARCHAR(500),
  contract_start   DATE NOT NULL,
  contract_type    VARCHAR(255) NOT NULL,
  full_time        VARCHAR(255) NOT NULL,
  contract_end     DATE,
  hours_per_week   INT          NOT NULL,
  created_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
  updated_at       TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

# --- !Downs
DROP TABLE employees;
