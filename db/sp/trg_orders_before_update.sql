DELIMITER $$
DROP TRIGGER IF EXISTS trg_orders_before_update $$
-- Cancelling a PAID order refunds the full amount.
CREATE TRIGGER trg_orders_before_update
BEFORE UPDATE ON orders
FOR EACH ROW
BEGIN
    IF NEW.status = 4 AND OLD.status = 2 THEN
        SET NEW.refund_amount = OLD.total_amount;
    END IF;
END $$
DELIMITER ;
