package com.example.mock.order.persistence.dao.dto;

import com.example.mock.order.common.dao.StoredProcedureResultTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.Record;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** One row of the result set returned by stored procedure {@code order_get_list}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class OrderGetResult extends StoredProcedureResultTemplate {

    private Long orderId;
    private Long customerId;
    private Integer status;
    private BigDecimal totalAmount;
    private BigDecimal refundAmount;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Override
    public void doSuccessProcess(Record record) {
        this.setOrderId(getLongField(record, "order_id"));
        this.setCustomerId(getLongField(record, "customer_id"));
        this.setStatus(getIntegerField(record, "status"));
        this.setTotalAmount(getBigDecimalField(record, "total_amount"));
        this.setRefundAmount(getBigDecimalField(record, "refund_amount"));
        this.setCreatedAt(getLocalDateTimeField(record, "created_at"));
        this.setUpdatedAt(getLocalDateTimeField(record, "updated_at"));
    }
}
