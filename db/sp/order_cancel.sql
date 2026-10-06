DELIMITER $$
DROP PROCEDURE IF EXISTS order_cancel $$
CREATE PROCEDURE order_cancel(
    IN in_company_id      INT UNSIGNED,
    IN in_emp_user_id     INT UNSIGNED,
    IN in_emp_data_rights INT UNSIGNED,
    IN in_order_id        INT UNSIGNED,
    IN in_reason          VARCHAR(255),
    IN auto_commit        TINYINT
)
beginLabel:BEGIN
    # 1. Cancels a CREATED or PAID order of the caller's company.
    # 2. Requires data right EDIT (bit 2).
    # 3. Shipped orders cannot be cancelled; cancelled orders cannot be cancelled again.
    # 4. Refund of a PAID order is handled by trigger trg_orders_before_update.
    DECLARE v_status TINYINT;

    IF (in_emp_data_rights & 2) = 0 THEN
        SELECT 40300 AS code, 'no permission to edit orders' AS err_msg, 'NO_EDIT_RIGHT' AS sub_code;
        LEAVE beginLabel;
    END IF;

    SELECT status INTO v_status FROM orders WHERE id = in_order_id AND company_id = in_company_id;

    IF v_status IS NULL THEN
        SELECT 40401 AS code, 'order not found' AS err_msg, 'ORDER_NOT_FOUND' AS sub_code;
        LEAVE beginLabel;
    ELSEIF v_status = 3 THEN
        SELECT 40001 AS code, 'shipped orders cannot be cancelled' AS err_msg, 'ORDER_SHIPPED' AS sub_code;
        LEAVE beginLabel;
    ELSEIF v_status = 4 THEN
        SELECT 40002 AS code, 'order is already cancelled' AS err_msg, 'ALREADY_CANCELLED' AS sub_code;
        LEAVE beginLabel;
    END IF;

    IF auto_commit = 1 THEN START TRANSACTION; END IF;

    UPDATE orders
    SET status = 4, cancel_reason = in_reason, updated_by = in_emp_user_id
    WHERE id = in_order_id;

    IF auto_commit = 1 THEN COMMIT; END IF;

    SELECT 20000 AS code, '' AS err_msg, NULL AS sub_code;
END $$
DELIMITER ;
