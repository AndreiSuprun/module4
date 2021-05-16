package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PriceValidatorTest {

    @InjectMocks
    PriceValidator priceValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValidPassedTest() {
        BigDecimal price = BigDecimal.valueOf(10);

        boolean actual = priceValidator.isValid(price);

        assertTrue(actual);
    }

    @Test
    void isValidLongTest() {
        BigDecimal price = BigDecimal.valueOf(0.5);

        boolean actual = priceValidator.isValid(price);

        assertFalse(actual);
    }

    @Test
    void isValidShortTest() {
        BigDecimal price = BigDecimal.valueOf(1000000);

        boolean actual = priceValidator.isValid(price);

        assertFalse(actual);
    }
}