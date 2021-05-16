package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class QuantityValidatorTest {

    @InjectMocks
    QuantityValidator quantityValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValidPassedTest() {
        Integer quantity = 60;

        boolean actual = quantityValidator.isValid(quantity);

        assertTrue(actual);
    }

    @Test
    void isValidLongTest() {
        Integer quantity = 20000;

        boolean actual = quantityValidator.isValid(quantity);

        assertFalse(actual);
    }

    @Test
    void isValidShortTest() {
        Integer quantity = 0;

        boolean actual = quantityValidator.isValid(quantity);

        assertFalse(actual);
    }
}