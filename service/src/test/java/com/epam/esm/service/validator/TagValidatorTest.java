package com.epam.esm.service.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.ValidationException;
import com.epam.esm.service.validator.impl.NameValidator;
import com.epam.esm.service.validator.impl.TagValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class TagValidatorTest {

    @InjectMocks
    private TagValidator tagValidator;
    @Mock
    private NameValidator nameValidator;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void validateCorrectTest() {
        Tag tag = new Tag();
        tag.setName("Tag");

        when(nameValidator.isValid(tag.getName())).thenReturn(true);
        tagValidator.validate(tag);
        verify(nameValidator).isValid(tag.getName());
    }

    @Test
    void validatorNotCorrectTest() {
        Tag tag = new Tag();
        tag.setName("Tag");

        when(nameValidator.isValid(tag.getName())).thenReturn(false);
        assertThrows(ValidationException.class,() -> {tagValidator.validate(tag);});
        verify(nameValidator).isValid(tag.getName());
    }
}