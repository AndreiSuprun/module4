package com.epam.esm.dao.impl;

import com.epam.esm.dao.CriteriaUtil;
import com.epam.esm.dao.UserDao;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import com.epam.esm.service.search.OrderCriteria;
import com.epam.esm.service.search.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

public class UserDaoImpl implements UserDao {

    @Autowired
    private CriteriaUtil<User> criteriaUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findOne(Long id) {
        return entityManager.find(User.class, id);
    }

    @Override
    public List<User> findAll(Integer page, Integer size) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<User> query = builder.createQuery(User.class);
        final Root<User> root = query.from(User.class);
        query.select(root);
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    public List<User> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Integer page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    public List<Order> getOrders(Long userId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot.get("orders"));
        criteria.where(builder.equal(userRoot.get("id"), userId));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Order getOrder(Long userId, Long orderId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot.get("orders"));
        criteria.where(builder.equal(userRoot.get("id"), userId));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Long count(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        if (searchParams.length > 0) {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    builder.createQuery().from(User.class)));
        }
        query.select(builder.count(query.from(User.class)));
        return entityManager.createQuery(query).getSingleResult();
    }
}
 