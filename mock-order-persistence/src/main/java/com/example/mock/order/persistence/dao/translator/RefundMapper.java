package com.example.mock.order.persistence.dao.translator;

import com.example.mock.order.entity.Refund;
import com.example.mock.order.persistence.dao.dto.RefundGetResult;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper
public interface RefundMapper {
    RefundMapper INSTANCE = Mappers.getMapper(RefundMapper.class);

    Refund toEntity(RefundGetResult source);
}
