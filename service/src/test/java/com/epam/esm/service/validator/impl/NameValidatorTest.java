package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class NameValidatorTest {

    @InjectMocks
    NameValidator nameValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValidPassedTest() {
        String name = "Certificate and Chromium Picolinate for Accountant and Food Short Order Cook";

        boolean actual = nameValidator.isValid(name);

        assertTrue(actual);
    }

    @Test
    void isValidLongTest() {
        String name = "Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for " +
                "the best price and only on our site! Buy Certificate and Chromium Picolinate for Accountant and Food " +
                "Short Order Cook for Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook " +
                "for Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for Buy";

        boolean actual = nameValidator.isValid(name);

        assertFalse(actual);
    }

    @Test
    void isValidShortTest() {
        String name = "";

        boolean actual = nameValidator.isValid(name);

        assertFalse(actual);
    }
}