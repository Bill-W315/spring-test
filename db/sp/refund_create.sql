DELIMITER $$
DROP PROCEDURE IF EXISTS refund_create $$
CREATE PROCEDURE refund_create(
    IN in_company_id      INT UNSIGNED,
    IN in_emp_user_id     INT UNSIGNED,
    IN in_emp_data_rights INT UNSIGNED,
    IN in_order_id        INT UNSIGNED,
    IN in_amount          DECIMAL(12,2),
    IN in_reason          VARCHAR(255),
    IN auto_commit        TINYINT
)
beginLabel:BEGIN
    # 1. Refunds part of a PAID order of the caller's company.
    # 2. Requires data right REFUND (bit 4).
    # 3. The total refunded amount must not exceed total_amount.
    # 4. orders.refund_amount accumulates all refunds of the order.
    DECLARE v_status TINYINT;
    DECLARE v_total DECIMAL(12,2);
    DECLARE v_refunded DECIMAL(12,2);

    IF (in_emp_data_rights & 4) = 0 THEN
        SELECT 40300 AS code, 'no permission to refund' AS err_msg, 'NO_REFUND_RIGHT' AS sub_code;
        LEAVE beginLabel;
    END IF;

    SELECT status, total_amount, IFNULL(refund_amount, 0)
    INTO v_status, v_total, v_refunded
    FROM orders WHERE id = in_order_id AND company_id = in_company_id;

    IF v_status IS NULL THEN
        SELECT 40401 AS code, 'order not found' AS err_msg, 'ORDER_NOT_FOUND' AS sub_code;
        LEAVE beginLabel;
    ELSEIF v_status <> 2 THEN
        SELECT 40003 AS code, 'only paid orders can be refunded' AS err_msg, 'ORDER_NOT_REFUNDABLE' AS sub_code;
        LEAVE beginLabel;
    ELSEIF in_amount > v_total - v_refunded THEN
        SELECT 40004 AS code, 'refund exceeds remaining amount' AS err_msg, 'REFUND_EXCEEDS_TOTAL' AS sub_code;
        LEAVE beginLabel;
    END IF;

    IF auto_commit = 1 THEN START TRANSACTION; END IF;

    INSERT INTO refunds (order_id, amount, reason, created_by)
    VALUES (in_order_id, in_amount, in_reason, in_emp_user_id);

    UPDATE orders
    SET refund_amount = IFNULL(refund_amount, 0) + in_amount, updated_by = in_emp_user_id
    WHERE id = in_order_id;

    IF auto_commit = 1 THEN COMMIT; END IF;

    SELECT 20000 AS code, '' AS err_msg, NULL AS sub_code;
END $$
DELIMITER ;
