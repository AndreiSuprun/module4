package com.epam.esm.dao.criteria;

import com.epam.esm.service.search.SearchCriteria;
import com.epam.esm.service.search.OrderCriteria;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

public class CriteriaUtil<T> {

     public Predicate buildSearchCriteriaPredicate(List<SearchCriteria> searchCriteriaList, CriteriaBuilder builder, Root<T> root){
        Predicate predicate = builder.conjunction();
        for (SearchCriteria criteria : searchCriteriaList){
            switch (criteria.getOperation()) {
                case EQUALITY:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.equal(root.get(criteria.getKey()), criteria.getValue()))) :
                            builder.and(predicate, (builder.equal(root.get(criteria.getKey()), criteria.getValue())));
                case NEGATION:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.notEqual(root.get(criteria.getKey()), criteria.getValue()))) :
                            builder.and(predicate, (builder.notEqual(root.get(criteria.getKey()), criteria.getValue())));
                case GREATER_THAN:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.greaterThan(root.get(criteria.getKey()), criteria.getValue().toString())));
                case LESS_THAN:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.lessThan(root.get(criteria.getKey()), criteria.getValue().toString())));
                case LIKE:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(root.get(criteria.getKey()), criteria.getValue().toString()))) :
                            builder.and(predicate, (builder.like(root.get(criteria.getKey()), criteria.getValue().toString())));
                case STARTS_WITH:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(root.get(criteria.getKey()), criteria.getValue() + "%"))) :
                            builder.and(predicate, (builder.like(root.get(criteria.getKey()), criteria.getValue() + "%")));
                case ENDS_WITH:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(root.get(criteria.getKey()), "%" + criteria.getValue()))) :
                            builder.and(predicate, (builder.like(root.get(criteria.getKey()), "%" + criteria.getValue())));
                case CONTAINS:
                    predicate = criteria.isOrPredicate()?
                            builder.or(predicate, (builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%"))) :
                            builder.and(predicate, (builder.like(root.get(criteria.getKey()), "%" + criteria.getValue() + "%")));
                default:
            }
        }
        return predicate;
    }

    public List<Order> addSortCriteria(List<OrderCriteria> sortCriteriaList, CriteriaBuilder builder, Root<T> root){
        List<Order> orders = new ArrayList<>();
        for (OrderCriteria criteria : sortCriteriaList){
            if(criteria.getDirection().equalsIgnoreCase("decs")){
                orders.add(builder.desc(root.get(criteria.getKey())));
            } else {
                orders.add(builder.asc(root.get(criteria.getKey())));
            }
        }
        return orders;
    }
}
