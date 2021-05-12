package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.OrderItem;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class OrderItemValidator extends EntityValidator<OrderItem> {

    private final static String QUANTITY_FIELD = "quantity";

    private final QuantityValidator quantityValidator;

    @Autowired
    public OrderItemValidator(QuantityValidator quantityValidator){
        this.quantityValidator = quantityValidator;
    }

    @Override
    public void validate(OrderItem orderItem) {
        validateField(quantityValidator,
                orderItem.getQuantity(), ErrorCode.ORDER_FIELD_INVALID,
                QUANTITY_FIELD, orderItem.getQuantity());
        }
}
