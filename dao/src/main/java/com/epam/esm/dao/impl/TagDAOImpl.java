package com.epam.esm.dao.impl;

import com.epam.esm.dao.criteria.CriteriaUtil;
import com.epam.esm.dao.TagDAO;
import com.epam.esm.dao.criteria.OrderCriteria;
import com.epam.esm.dao.criteria.SearchCriteria;
import com.epam.esm.entity.GiftCertificate;
import com.epam.esm.entity.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.*;
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
        List<Tag> tags = typedQuery.getResultList();
        if (!tags.isEmpty()){
            return tags.iterator().next();
        }
        return null;
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
        if (findByName(tag.getName()) != null){
            entityManager.persist(tag);
            return tag;
        }
        return null;
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

    public List<GiftCertificate> getCertificatesForTag(Long tagId) {
        CriteriaBuilder criteriaBuilder = entityManager.getCriteriaBuilder();
        CriteriaQuery<GiftCertificate> criteriaQuery = criteriaBuilder.createQuery(GiftCertificate.class);
        Root<GiftCertificate> certificateRoot = criteriaQuery.from(GiftCertificate.class);
        Root<Tag> tagRoot = criteriaQuery.from(Tag.class);
        criteriaQuery.where(criteriaBuilder.equal(tagRoot.get("id"), tagId));
        ListJoin<GiftCertificate, Tag> tags = certificateRoot.joinList("tags");
        //CriteriaQuery<GiftCertificate> criteriaQuery1 = criteriaQuery.select(tags);
//        TypedQuery<GiftCertificate> query = entityManager.createQuery(criteriaQuery1);
        return null; //query.getResultList();
    }
}

