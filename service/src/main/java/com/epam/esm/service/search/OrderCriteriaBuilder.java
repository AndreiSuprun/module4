package com.epam.esm.service.search;

import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchOperation;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderCriteriaBuilder {

    private List<OrderCriteria> params;
    private final String sortParameters;

    public OrderCriteriaBuilder(String sortParameters) {
        this.sortParameters = sortParameters;
    }

    public final OrderCriteriaBuilder with(final String key, final String direction) {
        params.add(new OrderCriteria(key, direction));
        return this;
    }

    public List<OrderCriteria> build() {
        params = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+?)(,)(\\w+?);");
        Matcher matcher = pattern.matcher(sortParameters + ";");
        while (matcher.find()) {
            with(matcher.group(1),matcher.group(3));
        }
        return params;
    }

    public final OrderCriteriaBuilder with(OrderCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
