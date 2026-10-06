package com.example.mock.order.persistence.dao.dto;

import com.example.mock.order.common.dao.StoredProcedureResultTemplate;
import org.jooq.Record;

/** Status-only row (code, err_msg, sub_code) returned by write procedures. */
public class BasicSpResult extends StoredProcedureResultTemplate {

    @Override
    public void doSuccessProcess(Record record) {
        // status fields are mapped by the base class
    }
}
