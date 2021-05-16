package com.epam.esm.service.validator.impl;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DurationValidatorTest {

    @InjectMocks
    DurationValidator durationValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void isValidPassedTest() {
        Integer duration = 60;

        boolean actual = durationValidator.isValid(duration);

        assertTrue(actual);
    }

    @Test
    void isValidLongTest() {
        Integer duration = 200;

        boolean actual = durationValidator.isValid(duration);

        assertFalse(actual);
    }

    @Test
    void isValidShortTest() {
        Integer duration = 0;

        boolean actual = durationValidator.isValid(duration);

        assertFalse(actual);
    }
}