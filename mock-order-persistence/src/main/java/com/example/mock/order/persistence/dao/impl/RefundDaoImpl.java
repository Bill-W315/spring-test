package com.example.mock.order.persistence.dao.impl;

import com.example.mock.order.entity.Refund;
import com.example.mock.order.persistence.dao.RefundDao;
import com.example.mock.order.persistence.dao.dto.BasicSpResult;
import com.example.mock.order.persistence.dao.dto.RefundGetResult;
import com.example.mock.order.persistence.dao.jooq.AbstractJooqDao;
import com.example.mock.order.persistence.dao.jooq.mock_order.routines.RefundCreate;
import com.example.mock.order.persistence.dao.jooq.mock_order.routines.RefundGetList;
import com.example.mock.order.persistence.dao.translator.RefundMapper;
import com.example.mock.order.common.dao.StoredProcedureUtil;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class RefundDaoImpl extends AbstractJooqDao implements RefundDao {

    @Override
    public List<Refund> findByOrderId(Long orderId) {
        RefundGetList routine = createRoutinesWithAuth(RefundGetList.class, ORDER_DATA_TYPE);
        routine.setInOrderId(StoredProcedureUtil.longToUInt(orderId));
        executeWithReadConfig(routine);

        return getListResultWithValidation(routine, RefundGetResult.class).stream()
                .map(RefundMapper.INSTANCE::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void create(Long orderId, BigDecimal amount, String reason) {
        RefundCreate routine = createRoutinesWithAuth(RefundCreate.class, ORDER_DATA_TYPE);
        routine.setInOrderId(StoredProcedureUtil.longToUInt(orderId));
        routine.setInAmount(amount);
        routine.setInReason(reason);
        routine.setAutoCommit((byte) 1);
        executeWithWriteConfig(routine);

        getOneResultWithValidation(routine, BasicSpResult.class);
    }
}
