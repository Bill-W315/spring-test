DELIMITER $$
DROP PROCEDURE IF EXISTS order_create $$
CREATE PROCEDURE order_create(
    IN in_company_id      INT UNSIGNED,
    IN in_emp_user_id     INT UNSIGNED,
    IN in_emp_data_rights INT UNSIGNED,
    IN in_customer_id     INT UNSIGNED,
    IN in_item_list       JSON,
    IN auto_commit        TINYINT
)
beginLabel:BEGIN
    # 1. Creates a PAID order with its items for a customer of the caller's company.
    # 2. Requires data right EDIT (bit 2).
    # 3. total_amount = sum(quantity * unit_price) of in_item_list.
    # 4. Result: code, err_msg, sub_code, order_id.
    DECLARE v_total DECIMAL(12,2);
    DECLARE v_order_id INT UNSIGNED;
    DECLARE EXIT HANDLER FOR SQLEXCEPTION
    BEGIN
        IF auto_commit = 1 THEN ROLLBACK; END IF;
        RESIGNAL;
    END;

    IF (in_emp_data_rights & 2) = 0 THEN
        SELECT 40300 AS code, 'no permission to edit orders' AS err_msg, 'NO_EDIT_RIGHT' AS sub_code;
        LEAVE beginLabel;
    END IF;

    IF NOT EXISTS (SELECT 1 FROM customers WHERE id = in_customer_id AND company_id = in_company_id) THEN
        SELECT 40401 AS code, 'customer not found' AS err_msg, 'CUSTOMER_NOT_FOUND' AS sub_code;
        LEAVE beginLabel;
    END IF;

    SELECT SUM(j.quantity * j.unit_price) INTO v_total
    FROM JSON_TABLE(in_item_list, '$[*]' COLUMNS (
        quantity   INT           PATH '$.quantity',
        unit_price DECIMAL(12,2) PATH '$.unit_price')) j;

    IF auto_commit = 1 THEN START TRANSACTION; END IF;

    INSERT INTO orders (company_id, customer_id, status, total_amount, created_by)
    VALUES (in_company_id, in_customer_id, 2, v_total, in_emp_user_id);
    SET v_order_id = LAST_INSERT_ID();

    INSERT INTO order_items (order_id, sku, quantity, unit_price)
    SELECT v_order_id, j.sku, j.quantity, j.unit_price
    FROM JSON_TABLE(in_item_list, '$[*]' COLUMNS (
        sku        VARCHAR(64)   PATH '$.sku',
        quantity   INT           PATH '$.quantity',
        unit_price DECIMAL(12,2) PATH '$.unit_price')) j;

    IF auto_commit = 1 THEN COMMIT; END IF;

    SELECT 20000 AS code, '' AS err_msg, NULL AS sub_code, v_order_id AS order_id;
END $$
DELIMITER ;
