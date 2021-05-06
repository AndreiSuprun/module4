package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDao;
import com.epam.esm.dao.UserDao;
import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class OrderDaoImpl implements OrderDao {

    @Autowired
    private CriteriaUtil<Order> criteriaUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
    public List<Order> findAll(Long page, Integer size) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Order> query = builder.createQuery(Order.class);
        final Root<Order> root = query.from(Order.class);
        query.select(root);
        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) ((page - 1) * size));
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    public List<Order> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Long page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> query = builder.createQuery(Order.class);
        Root<Order> root = query.from(Order.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<Order> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) ((page - 1) * size));
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public Order findByName(String name) {
        throw new UnsupportedOperationException();
    }

    public List<Order> getOrdersForUser (Long orderId) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = builder.createQuery(Order.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.select(userRoot.get("orders"));
        criteria.where(builder.equal(userRoot.get("id"), userId));
        return entityManager.createQuery(criteria).getResultList();
    }

    public Order getOrder(Long userId, Long orderId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Order> criteria = criteriaBuilder.createQuery(Order.class);
        Root<Order> orderRoot = criteria.from(Order.class);
        Root<User> userRoot = criteria.from(User.class);
        criteria.where(criteriaBuilder.and(
                criteriaBuilder.equal(userRoot.get("id"), userId),
                criteriaBuilder.equal(orderRoot.get("id"), orderId)));
        criteria.select(orderRoot);
        List<Order> orders = entityManager.createQuery(criteria).getResultList();
        return orders.iterator().next();
    }

    public User addOrder(Long userId, Order order){
        User user = findOne(userId);
        user.addOrder(order);
        entityManager.merge(user);
        return user;
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

    @Override
    public User insert(User obj) {
        throw new UnsupportedOperationException();
    }

    @Override
    public User update(User obj, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        throw new UnsupportedOperationException();
    }
}
 