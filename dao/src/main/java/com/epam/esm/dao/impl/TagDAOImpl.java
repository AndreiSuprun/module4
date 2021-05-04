package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.entity.Tag;
import com.epam.esm.service.search.OrderCriteria;
import com.epam.esm.service.search.SearchCriteria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowCallbackHandler;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class TagDAOImpl implements TagDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private CriteriaUtil<Tag> criteriaUtil;

    @Override
    public Tag findOne(Long id) {
        return entityManager.find(Tag.class, id);
    }

    @Override
    public Tag findByName(String name) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        final Root<Tag> root = query.from(Tag.class);
        query.select(root).where(builder.equal(root.get("name"), name));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        return typedQuery.getSingleResult();
    }

    @Override
    public List<Tag> findAll(Integer page, Integer size) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        final Root<Tag> root = query.from(Tag.class);
        query.select(root);
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }

    @Override
    public Tag insert(Tag tag) {
        entityManager.persist(tag);
        return tag;
    }

    @Override
    public Tag update(Tag obj, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean delete(Long id) {
        Tag tag = entityManager.find(Tag.class, id);
        if(tag != null) {
            entityManager.remove(tag);
            return true;
        }
        return false;
    }

    public Long count(List<SearchCriteria>... searchParams) {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Long> query = builder.createQuery(Long.class);
        if (searchParams.length > 0)  {
            query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams[0], builder,
                    builder.createQuery().from(Tag.class)));
        }
        query.select(builder.count(query.from(Tag.class)));
        return entityManager.createQuery(query).getSingleResult();
    }

    @Override
    public List<Tag> findByQuery(List<SearchCriteria> searchParams, List<OrderCriteria> sortParams, Integer page, Integer size) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Tag> query = builder.createQuery(Tag.class);
        Root<Tag> root = query.from(Tag.class);
        query.select(root);
        query.where(criteriaUtil.buildSearchCriteriaPredicate(searchParams, builder, root));
        query.orderBy(criteriaUtil.addSortCriteria(sortParams, builder, root));
        TypedQuery<Tag> typedQuery = entityManager.createQuery(query);
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        return typedQuery.getResultList();
    }
}

