DELIMITER $$
DROP TRIGGER IF EXISTS trg_orders_after_update $$
-- Records every status change in order_status_log.
CREATE TRIGGER trg_orders_after_update
AFTER UPDATE ON orders
FOR EACH ROW
BEGIN
    IF NEW.status <> OLD.status THEN
        INSERT INTO order_status_log (order_id, from_status, to_status, changed_by)
        VALUES (NEW.id, OLD.status, NEW.status, NEW.updated_by);
    END IF;
END $$
DELIMITER ;
