package com.epam.esm.service.validator;

import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.OrderItem;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.impl.DescriptionValidator;
import com.epam.esm.service.validator.impl.DurationValidator;
import com.epam.esm.service.validator.impl.GiftCertificateValidator;
import com.epam.esm.service.validator.impl.NameValidator;
import com.epam.esm.service.validator.impl.OrderItemValidator;
import com.epam.esm.service.validator.impl.PriceValidator;
import com.epam.esm.service.validator.impl.QuantityValidator;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class GiftCertificateValidatorTest {

    @InjectMocks
    private GiftCertificateValidator giftCertificateValidator;
    @Mock
    private NameValidator nameValidator;
    @Mock
    private DescriptionValidator descriptionValidator;
    @Mock
    private PriceValidator priceValidator;
    @Mock
    private DurationValidator durationValidator;
    @Mock
    private TagValidator tagValidator;


    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateCorrectTest() {
        GiftCertificate giftCertificate = new GiftCertificate();

        when(nameValidator.isValid(giftCertificate.getName())).thenReturn(true);
        when(descriptionValidator.isValid(giftCertificate.getDescription())).thenReturn(true);
        when(priceValidator.isValid(giftCertificate.getPrice())).thenReturn(true);
        when(durationValidator.isValid(giftCertificate.getDuration())).thenReturn(true);
        when(tagValidator.validate(giftCertificate.getName())user i;).thenReturn(true);
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