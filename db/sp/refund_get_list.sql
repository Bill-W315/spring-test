DELIMITER $$
DROP PROCEDURE IF EXISTS refund_get_list $$
CREATE PROCEDURE refund_get_list(
    IN in_company_id      INT UNSIGNED,
    IN in_emp_user_id     INT UNSIGNED,
    IN in_emp_data_rights INT UNSIGNED,
    IN in_order_id        INT UNSIGNED
)
beginLabel:BEGIN
    # 1. Lists refunds of one order of the caller's company.
    # 2. Requires data right VIEW (bit 1).
    IF (in_emp_data_rights & 1) = 0 THEN
        SELECT 40300 AS code, 'no permission to view orders' AS err_msg, 'NO_VIEW_RIGHT' AS sub_code;
        LEAVE beginLabel;
    END IF;

    SELECT 20000 AS code, '' AS err_msg, NULL AS sub_code,
           r.id AS refund_id, r.order_id, r.amount, r.reason, r.created_at
    FROM refunds r
    JOIN orders o ON o.id = r.order_id
    WHERE o.company_id = in_company_id
      AND r.order_id = in_order_id
    ORDER BY r.id;
END $$
DELIMITER ;
