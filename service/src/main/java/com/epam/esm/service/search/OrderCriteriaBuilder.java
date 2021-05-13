package com.epam.esm.service.search;

import com.epam.esm.dao.criteria.OrderCriteria;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Service class responsible for processing order parameter in request
 *
 * @author Andrei Suprun
 */

public class OrderCriteriaBuilder {

    private List<OrderCriteria> params;
    private final String sortParameters;

    public OrderCriteriaBuilder(String sortParameters) {
        this.sortParameters = sortParameters;
    }

    /**
     * Adds OrderCriteria object to list of OrderCriteria for order request parameter.
     *
     * @param key key for ordering
     * @param direction direction of ordering
     * @return OrderCriteriaBuilder this object for OrderCriteriaBuilder
     */
    public OrderCriteriaBuilder with(String key, String direction) {
        params.add(new OrderCriteria(key, direction));
        return this;
    }

    /**
     * Parses order request parameter to OrderCriteria object and adds them to list of OrderCriteria for order request
     * parameter.
     *
     * @return List of OrderCriteria fot order request parameter
     */
    public List<OrderCriteria> build() {
        params = new ArrayList<>();
        Pattern pattern = Pattern.compile("(\\w+?)(,)(\\w+?);");
        Matcher matcher = pattern.matcher(sortParameters + ";");
        while (matcher.find()) {
            with(matcher.group(1),matcher.group(3));
        }
        return params;
    }

    /**
     * Adds existing OrderCriteria object to list of OrderCriteria for order request parameter.
     *
     * @param criteria OrderCriteria for order request parameter to add to list of OrderCriteria
     * @return OrderCriteriaBuilder this object for OrderCriteriaBuilder
     */
    public OrderCriteriaBuilder with(OrderCriteria criteria) {
        params.add(criteria);
        return this;
    }
}
