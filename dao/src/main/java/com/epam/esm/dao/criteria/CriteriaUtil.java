package com.epam.esm.dao.criteria;

import org.springframework.stereotype.Component;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@Component
public class CriteriaUtil<T> {

    private static String UNDERSCORE_SIGN = "_";

     public Predicate buildSearchCriteriaPredicate(List<SearchCriteria> searchCriteriaList, CriteriaBuilder builder, Root<T> root){
        Predicate predicate = builder.conjunction();
        if(searchCriteriaList != null){
        for (SearchCriteria criteria : searchCriteriaList){
            String joinProperties = null;
            String newKey = null;
            if(criteria.isNestedProperty()){
                String complexKey = criteria.getKey();
                joinProperties = complexKey.substring(0, complexKey.indexOf(UNDERSCORE_SIGN));
                newKey = complexKey.substring(complexKey.indexOf(UNDERSCORE_SIGN) + 1);
            }
            switch (criteria.getOperation()) {
                case EQUALITY:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.equal(criteria.isNestedProperty() ?
                                            root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue()))) :
                            builder.and(predicate, (builder.equal(criteria.isNestedProperty() ?
                                            root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue())));
                    break;
                case NEGATION:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.notEqual(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue()))) :
                            builder.and(predicate, (builder.notEqual(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue())));
                    break;
                case GREATER_THAN:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.greaterThan(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.greaterThan(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString())));
                    break;
                case LESS_THAN:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.lessThan(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.lessThan(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString())));
                    break;
                case LIKE:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue().toString())));
                    break;
                case STARTS_WITH:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue() + "%"))) :
                            builder.and(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    criteria.getValue() + "%")));
                    break;
                case ENDS_WITH:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    "%" + criteria.getValue()))) :
                            builder.and(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    "%" + criteria.getValue())));
                    break;
                case CONTAINS:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    "%" + criteria.getValue() + "%"))) :
                            builder.and(predicate, (builder.like(criteria.isNestedProperty() ?
                                    root.join(joinProperties).get(newKey) : root.get(criteria.getKey()),
                                    "%" + criteria.getValue() + "%")));
                    break;
                default:
            }
        }}
        return predicate;
    }

    public List<Order> addSortCriteria(List<OrderCriteria> sortCriteriaList, CriteriaBuilder builder, Root<T> root){
        List<Order> orders = new ArrayList<>();
        if(sortCriteriaList != null){
        for (OrderCriteria criteria : sortCriteriaList){
            String joinProperties = null;
            String newKey = null;
            if(criteria.isNestedProperty()){
                String complexKey = criteria.getKey();
                joinProperties = complexKey.substring(0, complexKey.indexOf(UNDERSCORE_SIGN));
                newKey = complexKey.substring(complexKey.indexOf(UNDERSCORE_SIGN) + 1);
            }
            if(criteria.getDirection().equalsIgnoreCase("desc")){
                orders.add(builder.desc(criteria.isNestedProperty() ?
                        root.join(joinProperties).get(newKey) : root.get(criteria.getKey())));
            } else {
                orders.add(builder.asc(criteria.isNestedProperty() ?
                        root.join(joinProperties).get(newKey) : root.get(criteria.getKey())));
            }
        }}
        return orders;
    }
}
