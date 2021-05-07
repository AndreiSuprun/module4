package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
public class TagValidator extends EntityValidator<Tag> {

    private final static String NAME_FIELD = "name";

    @Override
    public void validate(Tag tag) {
        if (Objects.isNull(tag.getId()) && Objects.isNull(tag.getName())){
           throw new ProjectException(ErrorCode.TAG_IS_EMPTY);
        }
        validateField(new NameValidator(), tag.getName(), ErrorCode.TAG_FIELD_INVALID, NAME_FIELD, tag.getName());
    }
}
