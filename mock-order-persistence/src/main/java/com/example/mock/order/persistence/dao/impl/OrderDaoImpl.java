package com.example.mock.order.persistence.dao.impl;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.OrderCriteria;
import com.example.mock.order.entity.OrderItem;
import com.example.mock.order.persistence.dao.OrderDao;
import com.example.mock.order.persistence.dao.dto.BasicSpResult;
import com.example.mock.order.persistence.dao.dto.OrderCreateResult;
import com.example.mock.order.persistence.dao.dto.OrderGetResult;
import com.example.mock.order.persistence.dao.jooq.AbstractJooqDao;
import com.example.mock.order.persistence.dao.jooq.mock_order.routines.OrderCancel;
import com.example.mock.order.persistence.dao.jooq.mock_order.routines.OrderCreate;
import com.example.mock.order.persistence.dao.jooq.mock_order.routines.OrderGetList;
import com.example.mock.order.persistence.dao.translator.OrderMapper;
import com.example.mock.order.persistence.dao.util.SnakeCaseGsonUtil;
import com.example.mock.order.common.dao.StoredProcedureUtil;
import org.jooq.JSON;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class OrderDaoImpl extends AbstractJooqDao implements OrderDao {

    @Override
    public List<Order> findByCriteria(OrderCriteria criteria) {
        OrderGetList routine = createRoutinesWithAuth(OrderGetList.class, ORDER_DATA_TYPE);
        routine.setInOrderId(StoredProcedureUtil.longToUInt(criteria.getOrderId()));
        routine.setInStatus(Optional.ofNullable(criteria.getStatus())
                .map(s -> s.getCode().byteValue())
                .orElse(null));
        executeWithReadConfig(routine);

        return getListResultWithValidation(routine, OrderGetResult.class).stream()
                .map(OrderMapper.INSTANCE::toEntity)
                .collect(Collectors.toList());
    }

    @Override
    public Long create(Long customerId, List<OrderItem> items) {
        OrderCreate routine = createRoutinesWithAuth(OrderCreate.class, ORDER_DATA_TYPE);
        routine.setInCustomerId(StoredProcedureUtil.longToUInt(customerId));
        routine.setInItemList(JSON.json(SnakeCaseGsonUtil.toJson(items)));
        routine.setAutoCommit((byte) 1);
        executeWithWriteConfig(routine);

        OrderCreateResult result = getOneResultWithValidation(routine, OrderCreateResult.class);
        return result == null ? null : result.getOrderId();
    }

    @Override
    public void cancel(Long orderId, String reason) {
        OrderCancel routine = createRoutinesWithAuth(OrderCancel.class, ORDER_DATA_TYPE);
        routine.setInOrderId(StoredProcedureUtil.longToUInt(orderId));
        routine.setInReason(reason);
        routine.setAutoCommit((byte) 1);
        executeWithWriteConfig(routine);

        getOneResultWithValidation(routine, BasicSpResult.class);
    }
}
