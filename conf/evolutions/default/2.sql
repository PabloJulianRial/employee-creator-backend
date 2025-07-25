# --- !Ups
CREATE TABLE contracts (
  id            BIGINT AUTO_INCREMENT PRIMARY KEY,
  employee_id   BIGINT       NOT NULL,
  start_date    TIMESTAMP    NOT NULL,
  end_date      TIMESTAMP,
  contract_type VARCHAR(20)  NOT NULL,
  contract_time VARCHAR(20)  NOT NULL,
  hours_week    INT          NOT NULL,
  FOREIGN KEY (employee_id) REFERENCES employees(id) ON DELETE CASCADE
);
# --- !Downs
DROP TABLE contracts;
