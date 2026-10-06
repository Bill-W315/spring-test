DELIMITER $$
DROP PROCEDURE IF EXISTS order_get_list $$
CREATE PROCEDURE order_get_list(
    IN in_company_id      INT UNSIGNED,
    IN in_emp_user_id     INT UNSIGNED,
    IN in_emp_data_rights INT UNSIGNED,
    IN in_order_id        INT UNSIGNED,
    IN in_status          TINYINT
)
beginLabel:BEGIN
    # 1. Lists orders of the caller's company.
    # 2. Requires data right VIEW (bit 1).
    # 3. Result: code, err_msg, sub_code, then order columns.
    # 4. in_order_id and in_status are optional filters.
    IF (in_emp_data_rights & 1) = 0 THEN
        SELECT 40300 AS code, 'no permission to view orders' AS err_msg, 'NO_VIEW_RIGHT' AS sub_code;
        LEAVE beginLabel;
    END IF;

    SELECT 20000 AS code, '' AS err_msg, NULL AS sub_code,
           o.id AS order_id, o.customer_id, o.status, o.total_amount,
           o.refund_amount, o.created_at, o.updated_at
    FROM orders o
    WHERE o.company_id = in_company_id
      AND (in_order_id IS NULL OR o.id = in_order_id)
      AND (in_status IS NULL OR o.status = in_status)
    ORDER BY o.id;
END $$
DELIMITER ;
