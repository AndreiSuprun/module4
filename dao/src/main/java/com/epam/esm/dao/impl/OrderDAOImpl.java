package com.epam.esm.dao.impl;

import com.epam.esm.dao.OrderDAO;
import com.epam.esm.dao.UserDAO;
import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Order;
import com.epam.esm.entity.OrderItem;
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
public class OrderDAOImpl implements OrderDAO {

    @Autowired
    private CriteriaUtil<Order> criteriaUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private UserDAO userDAO;

    @Override
    public Order findOne(Long id) {
        return entityManager.find(Order.class, id);
    }

    @Override
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

    @Override
    public Order insert(Order order){
        entityManager.persist(order);
        for(OrderItem orderItem : order.getOrderCertificates()){
            orderItem.setOrder(order);
            entityManager.persist(orderItem);
            order.addOrderCertificate(orderItem);
        };
        return entityManager.merge(order);
    }

    @Override
    public Long count(List<SearchCriteria>... searchParams) {
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

    @Override
    public Order update(Order order, Long id) {
        order.setId(id);
        return entityManager.merge(order);
    }

    @Override
    public boolean delete(Long id) {
        Order order = entityManager.find(Order.class, id);
        if(order != null) {
            entityManager.remove(order);
            return true;
        }
        return false;
    }
}
 