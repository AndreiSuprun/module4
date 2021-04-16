package com.epam.esm.service.validator;

import com.epam.esm.service.dto.TagDTO;
import com.epam.esm.service.exception.ProjectException;
import com.epam.esm.service.validator.impl.NameValidator;

import java.util.List;

public class TagValidator extends EntityValidator {

    private final static String NAME_FIELD = "name";

    @Override
    protected void validateObject(List<ProjectException> errors, Object object) {
        if (!(object instanceof TagDTO)) {
            throw new IllegalArgumentException("The object has not Periodical type");
        }
        TagDTO tagDTO = (TagDTO) object;
        validateField(new NameValidator(), tagDTO.getName(),
                errors, NAME_FIELD, tagDTO.getName());
    }
}
