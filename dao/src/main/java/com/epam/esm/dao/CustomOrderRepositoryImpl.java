package com.epam.esm.dao;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class CustomOrderRepositoryImpl implements CustomOrderRepository{

    @PersistenceContext
    private EntityManager entityManager;
    private final CriteriaUtil<Order> criteriaUtil;

    @Autowired
    public CustomOrderRepositoryImpl(CriteriaUtil<Order> criteriaUtil) {
        this.criteriaUtil = criteriaUtil;
    }

    @Override
    public Page<Order> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Pageable pageable) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((pageable.getPageNumber()) * pageable.getPageSize());
        typedQuery.setMaxResults(pageable.getPageSize());
        List<Order> orders = typedQuery.getResultList();
        Long count = countByQuery(searchParams);
        return new PageImpl<>(orders, pageable, count);
    }

    @Override
    public Long countByQuery(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Order> root = query.from(Order.class);
        if (searchParams.length > 0) {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    root));
        }
        query.select(builder.count(root));
        return entityManager.createQuery(query).getSingleResult();
    }
}
