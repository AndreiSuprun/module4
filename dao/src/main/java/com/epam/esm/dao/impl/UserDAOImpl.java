package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.UserDAO;
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
public class UserDAOImpl implements UserDAO {

    @Autowired
    private CriteriaUtil<User> criteriaUtil;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public User findOne(Long id) {
        return entityManager.find(User.class, id);
    }

//    @Override
//    public List<User> findAll(Long page, Integer size) {
//        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//        final CriteriaQuery<User> query = builder.createQuery(User.class);
//        final Root<User> root = query.from(User.class);
//        query.select(root);
//        TypedQuery<User> typedQuery = entityManager.createQuery(query);
//        typedQuery.setFirstResult((int) ((page - 1) * size));
//        typedQuery.setMaxResults(size);
//        return typedQuery.getResultList();
//    }

    @Override
    public List<User> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Long page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> root = query.from(User.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<User> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((int) ((page - 1) * size));
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public User findByName(String name) {
        throw new UnsupportedOperationException();
    }

    @Override
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
 