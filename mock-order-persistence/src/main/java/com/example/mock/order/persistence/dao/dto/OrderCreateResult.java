package com.example.mock.order.persistence.dao.dto;

import com.example.mock.order.common.dao.StoredProcedureResultTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.Record;

/** Status row returned by stored procedure {@code order_create}, including the new order id. */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderCreateResult extends StoredProcedureResultTemplate {

    private Long orderId;

    @Override
    public void doSuccessProcess(Record record) {
        this.setOrderId(getLongField(record, "order_id"));
    }
}
