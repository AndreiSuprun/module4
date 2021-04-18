package com.epam.esm.service.validator;

import com.epam.esm.entity.Tag;
import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.validator.impl.NameValidator;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TagValidator extends EntityValidator {

    private final static String NAME_FIELD = "name";

    @Override
    protected void validateObject(List<String> invalidFields, Object object) {
        if (!(object instanceof Tag)) {
            throw new IllegalArgumentException("The object has not Periodical type");
        }
        Tag tag = (Tag) object;
        validateField(new NameValidator(), tag.getName(),
                invalidFields, NAME_FIELD, tag.getName());
    }
}
