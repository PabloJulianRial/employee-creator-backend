# --- !Ups
CREATE TABLE employees (
  id INT PRIMARY KEY AUTO_INCREMENT,
  first_name VARCHAR(255) NOT NULL,
  last_name VARCHAR(255) NOT NULL,
  email VARCHAR(255),
  mobile INT NOT NULL,
  address VARCHAR(255),
);

# --- !Downs
DROP TABLE employees;