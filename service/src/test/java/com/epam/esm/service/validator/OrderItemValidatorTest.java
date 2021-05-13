package com.epam.esm.service.validator;

import com.epam.esm.entity.OrderItem;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import com.epam.esm.service.validator.impl.QuantityValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class OrderItemValidatorTest {

    @InjectMocks
    private OrderItemValidator orderItemValidator;
    @Mock
    private QuantityValidator quantityValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateCorrectTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);

        when(quantityValidator.isValid(orderItem.getQuantity())).thenReturn(true);
        orderItemValidator.validate(orderItem);
        verify(quantityValidator).isValid(orderItem.getQuantity());
    }

    @Test
    void validatorNotCorrectTest() {
        OrderItem orderItem = new OrderItem();
        orderItem.setQuantity(2);
        
        when(quantityValidator.isValid(orderItem.getQuantity())).thenReturn(false);
        assertThrows(ValidationException.class,() -> {orderItemValidator.validate(orderItem);});
        verify(quantityValidator).isValid(orderItem.getQuantity());
    }
}