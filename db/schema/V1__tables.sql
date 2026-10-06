-- Tables of schema Mock_Order (reference copy kept in the repository).
CREATE TABLE customers (
    id          INT UNSIGNED NOT NULL AUTO_INCREMENT,
    company_id  INT UNSIGNED NOT NULL,
    name        VARCHAR(64)  NOT NULL,
    email       VARCHAR(128) NULL,
    phone       VARCHAR(32)  NULL,
    created_at  DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_customers_company (company_id)
) COMMENT = 'Customers per company; email and phone are personal data';

CREATE TABLE orders (
    id             INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    company_id     INT UNSIGNED  NOT NULL,
    customer_id    INT UNSIGNED  NOT NULL,
    status         TINYINT       NOT NULL COMMENT '1 CREATED, 2 PAID, 3 SHIPPED, 4 CANCELLED',
    total_amount   DECIMAL(12,2) NOT NULL,
    refund_amount  DECIMAL(12,2) NULL COMMENT 'Sum of refunds; NULL when never refunded',
    cancel_reason  VARCHAR(255)  NULL,
    created_by     INT UNSIGNED  NOT NULL,
    updated_by     INT UNSIGNED  NULL,
    created_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at     DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_orders_company (company_id),
    CONSTRAINT fk_orders_customer FOREIGN KEY (customer_id) REFERENCES customers (id)
) AUTO_INCREMENT = 1001 COMMENT = 'Orders';

CREATE TABLE order_items (
    id          INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    order_id    INT UNSIGNED  NOT NULL,
    sku         VARCHAR(64)   NOT NULL,
    quantity    INT           NOT NULL,
    unit_price  DECIMAL(12,2) NOT NULL,
    PRIMARY KEY (id),
    CONSTRAINT fk_items_order FOREIGN KEY (order_id) REFERENCES orders (id)
) COMMENT = 'Order lines';

CREATE TABLE refunds (
    id          INT UNSIGNED  NOT NULL AUTO_INCREMENT,
    order_id    INT UNSIGNED  NOT NULL,
    amount      DECIMAL(12,2) NOT NULL,
    reason      VARCHAR(255)  NULL,
    created_by  INT UNSIGNED  NOT NULL,
    created_at  DATETIME      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    CONSTRAINT fk_refunds_order FOREIGN KEY (order_id) REFERENCES orders (id)
) AUTO_INCREMENT = 5001 COMMENT = 'Partial or full refunds of an order';

CREATE TABLE order_status_log (
    id           INT UNSIGNED NOT NULL AUTO_INCREMENT,
    order_id     INT UNSIGNED NOT NULL,
    from_status  TINYINT      NULL,
    to_status    TINYINT      NOT NULL,
    changed_by   INT UNSIGNED NULL,
    changed_at   DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (id),
    KEY idx_status_log_order (order_id)
) COMMENT = 'Status history, written only by triggers on orders';
