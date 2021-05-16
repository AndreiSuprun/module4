package com.epam.esm.service.validator.impl;

import com.epam.esm.service.mapper.impl.GiftCertificateMapper;
import com.epam.esm.service.mapper.impl.OrderItemMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class DescriptionValidatorTest {

    @InjectMocks
    DescriptionValidator descriptionValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValidPassedTest() {
        String description = "Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for " +
                "the best price and only on our site!";

        boolean actual = descriptionValidator.isValid(description);

        assertTrue(actual);
    }

    @Test
    void isValidLongTest() {
        String description = "Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for " +
                "the best price and only on our site! Buy Certificate and Chromium Picolinate for Accountant and Food " +
                "Short Order Cook for Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook " +
                "for Buy Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for Buy " +
                "Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for Buy Certificate and " +
                "Chromium Picolinate for Accountant and Food Short Order Cook for the best price and only on our site! " +
                "Buy Certificate and Chromium Picolinate for Accountant and Food \" +\n" + "Short Order Cook for Buy " +
                "Certificate and Chromium Picolinate for Accountant and Food Short Order Cook for Buy Certificate and " +
                "Chromium Picolinate for Accountant and Food Short Order Cook for Buy Certificate and Chromium " +
                "Picolinate for Accountant and Food Short Order Cook for";

        boolean actual = descriptionValidator.isValid(description);

        assertFalse(actual);
    }

    @Test
    void isValidShortTest() {
        String description = "Buy";

        boolean actual = descriptionValidator.isValid(description);

        assertFalse(actual);
    }
}