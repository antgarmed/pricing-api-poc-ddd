CREATE TABLE prices (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  brand_id   BIGINT    NOT NULL,
  product_id BIGINT    NOT NULL,
  start_date TIMESTAMP NOT NULL,
  end_date   TIMESTAMP NOT NULL,
  price_list BIGINT    NOT NULL,
  priority   BIGINT    NOT NULL,
  price      DECIMAL(10,2) NOT NULL,
  curr       CHAR(3)   NOT NULL
);
