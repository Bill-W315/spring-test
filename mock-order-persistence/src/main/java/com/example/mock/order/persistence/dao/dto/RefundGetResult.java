package com.example.mock.order.persistence.dao.dto;

import com.example.mock.order.common.dao.StoredProcedureResultTemplate;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.jooq.Record;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/** One row of the result set returned by stored procedure {@code refund_get_list}. */
@Data
@EqualsAndHashCode(callSuper = true)
public class RefundGetResult extends StoredProcedureResultTemplate {

    private Long refundId;
    private Long orderId;
    private BigDecimal amount;
    private String reason;
    private LocalDateTime createdAt;

    @Override
    public void doSuccessProcess(Record record) {
        this.setRefundId(getLongField(record, "refund_id"));
        this.setOrderId(getLongField(record, "order_id"));
        this.setAmount(getBigDecimalField(record, "amount"));
        this.setReason(getStringField(record, "reason"));
        this.setCreatedAt(getLocalDateTimeField(record, "created_at"));
    }
}
