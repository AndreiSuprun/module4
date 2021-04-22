package com.epam.esm.service.validator.impl;

import com.epam.esm.entity.Query;
import com.epam.esm.service.exception.ErrorCode;
import com.epam.esm.service.validator.EntityValidator;
import org.springframework.stereotype.Service;

@Service
public class QueryValidator extends EntityValidator<Query> {

    private final static String TAG_FIELD = "tag";
    private final static String CONTAINS_FIELD = "contains";
    private final static String ORDER_FIELD = "order";

    @Override
    public void validate(Query query) {
        if (query.getTag() != null) {
            validateField(new NameValidator(),
                    query.getTag(), ErrorCode.QUERY_PARAMETER_INVALID,
                    TAG_FIELD, query.getTag());
        }
        if (query.getContains() != null) {
            validateField(new NameValidator(),
                    query.getContains(), ErrorCode.QUERY_PARAMETER_INVALID,
                    CONTAINS_FIELD, query.getContains());
        }
        if (query.getOrder() != null) {
            validateField(new OrderParameterValidator(),
                    query.getOrder(), ErrorCode.QUERY_PARAMETER_INVALID,
                    ORDER_FIELD, query.getOrder());
        }
    }
}
