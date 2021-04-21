package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Query;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

@Service
public class QueryValidator extends EntityValidator<Query> {

    private final static String TAG_FIELD = "tag";
    private final static String NAME_FIELD = "name";
    private final static String DESCRIPTION_FIELD = "name";
    private final static String ORDER_FIELD = "order";

    @Override
    public void validate(Query query) {
        if (query.getTag() != null) {
            validateField(new NameValidator(),
                    query.getTag(), ErrorCode.QUERY_PARAMETER_INVALID,
                    TAG_FIELD, query.getTag());
        }
        if (query.getName() != null) {
            validateField(new NameValidator(),
                    query.getName(), ErrorCode.QUERY_PARAMETER_INVALID,
                    NAME_FIELD, query.getName());
        }
        if (query.getDescription() != null) {
            validateField(new DescriptionValidator(),
                    query.getDescription(), ErrorCode.QUERY_PARAMETER_INVALID,
                    DESCRIPTION_FIELD, query.getDescription());
        }
        if (query.getOrder() != null) {
            validateField(new OrderParameterValidator(),
                    query.getOrder(), ErrorCode.QUERY_PARAMETER_INVALID,
                    ORDER_FIELD, query.getOrder());
        }
    }
}
