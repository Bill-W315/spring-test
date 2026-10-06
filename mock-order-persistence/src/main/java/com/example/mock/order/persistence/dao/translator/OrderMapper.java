package com.example.mock.order.persistence.dao.translator;

import com.example.mock.order.entity.Order;
import com.example.mock.order.entity.enumeration.OrderStatus;
import com.example.mock.order.persistence.dao.dto.OrderGetResult;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(imports = OrderStatus.class)
public interface OrderMapper {
    OrderMapper INSTANCE = Mappers.getMapper(OrderMapper.class);

    @Mapping(target = "status", expression = "java(OrderStatus.fromCode(source.getStatus()))")
    Order toEntity(OrderGetResult source);
}
